package controllers;

import builders.CourseBuilder;
import builders.UserBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.ebean.DuplicateKeyException;
import models.entities.Course;
import models.entities.Currency;
import models.entities.User;
import org.junit.Test;
import play.mvc.Http;
import play.mvc.Result;
import utils.WithDatabase;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

public class UserControllerTest extends WithDatabase {
    @Test
    public void createUser() throws IOException {
        String jsonString =
                "{\n" +
                "  \"name\": \"John\",\n" +
                "  \"username\": \"JohnSmith\"\n" +
                "}";

        ObjectMapper mapper = new ObjectMapper();

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .bodyJson(mapper.readTree(jsonString))
                .uri("/api/user");

        Result result = route(app, request);
        assertEquals(OK, result.status());

        List<User> users = ebeanServer.find(User.class).findList();
        assertEquals(1, users.size());
        assertEquals("John", users.get(0).getName());
        assertEquals("JohnSmith", users.get(0).getUsername());
    }

    @Test(expected = DuplicateKeyException.class)
    public void cannotCreateUserWithSameUsername() throws IOException {
        String jsonString =
                "{\n" +
                "  \"name\": \"Hello\",\n" +
                "  \"username\": \"Exception\"\n" +
                "}";

        ObjectMapper mapper = new ObjectMapper();

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .bodyJson(mapper.readTree(jsonString))
                .uri("/api/user");

        route(app, request);
        route(app, request);
    }

    @Test
    public void bindUserToCourse() {
        User user = UserBuilder.builder().withName("John").withUsername("Smith").inject();
        Course course = CourseBuilder.builder().withTitle("Hello World").withPriceAmount(10.0f).withPriceCurrency(Currency.DKK).inject();

        String url =  String.format("/api/user/%d/bind/course/%d", user.getId(), course.getId());

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .uri(url);

        Result result = route(app, request);
        assertEquals(NO_CONTENT, result.status());

        user.refresh();
        assertEquals(1, user.getCourses().size());
        assertEquals("Hello World", user.getCourses().get(0).getTitle());

        course.refresh();
        assertEquals(1, course.getUsers().size());
        assertEquals("John", course.getUsers().get(0).getName());
    }

    @Test
    public void userCanHaveMoreCourses() {
        User user = UserBuilder.builder().withName("John").withUsername("Smith").inject();
        Course course1 = CourseBuilder.builder().withTitle("Training1").withPriceAmount(10.0f).withPriceCurrency(Currency.DKK).inject();
        Course course2 = CourseBuilder.builder().withTitle("Training2").withPriceAmount(10.0f).withPriceCurrency(Currency.DKK).inject();
        Course course3 = CourseBuilder.builder().withTitle("Training3").withPriceAmount(10.0f).withPriceCurrency(Currency.DKK).inject();

        Http.RequestBuilder request1 = new Http.RequestBuilder()
                .method(POST)
                .uri(String.format("/api/user/%d/bind/course/%d", user.getId(), course1.getId()));
        route(app, request1);

        Http.RequestBuilder request2 = new Http.RequestBuilder()
                .method(POST)
                .uri(String.format("/api/user/%d/bind/course/%d", user.getId(), course2.getId()));
        route(app, request2);

        Http.RequestBuilder request3 = new Http.RequestBuilder()
                .method(POST)
                .uri(String.format("/api/user/%d/bind/course/%d", user.getId(), course3.getId()));
        route(app, request3);

        user.refresh();
        assertEquals(3, user.getCourses().size());
    }

    @Test(expected = DuplicateKeyException.class)
    public void userCannotBindTwiceForTheSameCourse() {
        User user = UserBuilder.builder().withName("John").withUsername("Smith").inject();
        Course course = CourseBuilder.builder().withTitle("Training1").withPriceAmount(10.0f).withPriceCurrency(Currency.DKK).inject();
        String url =  String.format("/api/user/%d/bind/course/%d", user.getId(), course.getId());

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .uri(url);

        route(app, request);
        route(app, request);
    }

    @Test
    public void listOfCoursesForUser() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        User user = UserBuilder.builder().withName("John").withUsername("Smith").inject();
        CourseBuilder.builder().withTitle("Training1").withPriceAmount(10.0f).withPriceCurrency(Currency.DKK).withUser(user).inject();
        CourseBuilder.builder().withTitle("Training2").withPriceAmount(10.0f).withPriceCurrency(Currency.DKK).withUser(user).inject();
        CourseBuilder.builder().withTitle("Training3").withPriceAmount(10.0f).withPriceCurrency(Currency.DKK).withUser(user).inject();
        user.save();
        String url =  String.format("/api/user/%s/courses", user.getId());

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri(url);

        Result result = route(app, request);

        JsonNode jsonNode = mapper.readTree(contentAsString(result));
        assertEquals(3, jsonNode.size());
        assertEquals(1, jsonNode.get(0).get("id").asInt());
        assertEquals("Training1", jsonNode.get(0).get("title").asText());
        assertEquals(2, jsonNode.get(1).get("id").asInt());
        assertEquals("Training2", jsonNode.get(1).get("title").asText());
        assertEquals(3, jsonNode.get(2).get("id").asInt());
        assertEquals("Training3", jsonNode.get(2).get("title").asText());
    }
}
