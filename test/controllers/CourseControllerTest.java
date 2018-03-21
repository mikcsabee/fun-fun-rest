package controllers;

import builders.CourseBuilder;
import builders.LessonBuilder;
import builders.ModuleBuilder;
import builders.UserBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import models.entities.*;
import org.junit.Before;
import org.junit.Test;
import play.mvc.Http;
import play.mvc.Result;
import utils.WithDatabase;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static play.test.Helpers.*;

public class CourseControllerTest extends WithDatabase {
    @Before
    public void setup() {
        magicSetup();
    }

    @Test
    public void getCourseById() throws IOException {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/api/course/1");

        String actualResult = contentAsString(route(app, request));
        String expectedResult = readFile("course1.json");

        assertEquals(actualResult, expectedResult);
    }

    @Test
    public void getCoursesOrderByPriceASC() throws IOException {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/api/course?s=price&o=asc");

        String actualResult = contentAsString(route(app, request));
        String expectedResult = readFile("courses_order_by_price_asc.json");

        assertEquals(actualResult, expectedResult);
    }

    @Test
    public void getCoursesOrderByPriceDESC() throws IOException {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/api/course?s=price&o=desc");

        String actualResult = contentAsString(route(app, request));
        String expectedResult = readFile("courses_order_by_price_desc.json");

        assertEquals(actualResult, expectedResult);
    }

    @Test
    public void getCoursesOrderByEnrolmentsASC() throws IOException {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/api/course?s=enrolments&o=asc");

        String actualResult = contentAsString(route(app, request));
        String expectedResult = readFile("courses_order_by_enrolments_asc.json");

        assertEquals(actualResult, expectedResult);
    }


    @Test
    public void getCoursesOrderByEnrolmentsDESC() throws IOException {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/api/course?s=enrolments&o=desc");

        String actualResult = contentAsString(route(app, request));
        String expectedResult = readFile("courses_order_by_enrolments_desc.json");

        assertEquals(actualResult, expectedResult);
    }

    @Test
    public void getCoursesPagination() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        // Need one more item to overflow to the second page
        CourseBuilder
                .builder()
                .withTitle("Course4")
                .withPriceAmount(50.0f)
                .withPriceCurrency(Currency.DKK)
                .inject();

        Http.RequestBuilder request1 = new Http.RequestBuilder()
                .method(GET)
                .uri("/api/course?s=price&o=asc");

        String actualResult = contentAsString(route(app, request1));
        String expectedResult = readFile("courses_order_by_price_asc.json");

        assertEquals(actualResult, expectedResult);

        Http.RequestBuilder request2 = new Http.RequestBuilder()
                .method(GET)
                .uri("/api/course?p=1&s=price&o=asc");

        JsonNode jsonNode = mapper.readTree(contentAsString(route(app, request2)));
        assertEquals(1, jsonNode.size());
        assertEquals("Course4", jsonNode.get(0).get("title").asText());
    }

    @Test
    public void getUsersByCourse() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/api/course/1/users");

        Result result = route(app, request);

        JsonNode jsonNode = mapper.readTree(contentAsString(result));
        assertEquals(3, jsonNode.size());
        assertEquals(1, jsonNode.get(0).get("id").asInt());
        assertEquals("user1", jsonNode.get(0).get("name").asText());
        assertEquals("user1", jsonNode.get(0).get("username").asText());
        assertEquals("user2", jsonNode.get(1).get("name").asText());
        assertEquals("user2", jsonNode.get(1).get("username").asText());
        assertEquals("user3", jsonNode.get(2).get("name").asText());
        assertEquals("user3", jsonNode.get(2).get("username").asText());
    }

    @Test
    public void createUser() throws IOException {
        String jsonString =
                "{\n" +
                "    \"priceAmount\": 10.00,\n" +
                "    \"priceCurrency\": \"USD\",\n" +
                "    \"title\": \"Course Title\"\n" +
                "}\n}";

        ObjectMapper mapper = new ObjectMapper();

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .bodyJson(mapper.readTree(jsonString))
                .uri("/api/course");

        Result result = route(app, request);
        assertEquals(OK, result.status());

        Course course = ebeanServer.find(Course.class).where().idEq(4).findOne();
        assertNotNull(course);
        assertEquals(10.0f, course.getPriceAmount(), 0);
        assertEquals(Currency.USD, course.getPriceCurrency());
        assertEquals("Course Title", course.getTitle());
    }

    @Test
    public void updateUser() throws IOException {
        String jsonString =
                "{\n" +
                "    \"priceAmount\": 15.00,\n" +
                "    \"priceCurrency\": \"USD\",\n" +
                "    \"title\": \"New Title\"\n" +
                "}\n}";

        ObjectMapper mapper = new ObjectMapper();

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(PUT)
                .bodyJson(mapper.readTree(jsonString))
                .uri("/api/course/1");

        Result result = route(app, request);
        assertEquals(OK, result.status());

        Course course = ebeanServer.find(Course.class).where().idEq(1).findOne();
        assertNotNull(course);
        assertEquals(15.0f, course.getPriceAmount(), 0);
        assertEquals(Currency.USD, course.getPriceCurrency());
        assertEquals("New Title", course.getTitle());
    }

    /**
     * MagicSetup:
     * - Course1 (user1, user2, user9
     *   - Module1
     *     - Lesson1, Lesson2, Lesson3
     *   - Module2
     *     - Lesson3, Lesson4, Lesson5
     * - Course2 (user4)
     *   - Module2
     *     - Lesson3, Lesson4, Lesson5
     *   - Module3
     *     - Lesson5, Lesson6, Lesson7
     * - Course3 (user4, user6)
     *   - Module3
     *     - Lesson5, Lesson6, Lesson7
     *
     */
    private void magicSetup() {
        User user1 = UserBuilder.builder().withName("user1").withUsername("user1").inject();
        User user2 = UserBuilder.builder().withName("user2").withUsername("user2").inject();
        User user3 = UserBuilder.builder().withName("user3").withUsername("user3").inject();
        User user4 = UserBuilder.builder().withName("user4").withUsername("user4").inject();
        User user5 = UserBuilder.builder().withName("user5").withUsername("user5").inject();
        User user6 = UserBuilder.builder().withName("user6").withUsername("user6").inject();

        Lesson lesson1 = LessonBuilder.builder().withTitle("Lesson1").withContent("Content1").withOrder(4).inject();
        Lesson lesson2 = LessonBuilder.builder().withTitle("Lesson2").withContent("Content2").withOrder(6).inject();
        Lesson lesson3 = LessonBuilder.builder().withTitle("Lesson3").withContent("Content3").withOrder(2).inject();
        Lesson lesson4 = LessonBuilder.builder().withTitle("Lesson4").withContent("Content4").withOrder(1).inject();
        Lesson lesson5 = LessonBuilder.builder().withTitle("Lesson5").withContent("Content5").withOrder(5).inject();
        Lesson lesson6 = LessonBuilder.builder().withTitle("Lesson6").withContent("Content6").withOrder(7).inject();
        Lesson lesson7 = LessonBuilder.builder().withTitle("Lesson7").withContent("Content7").withOrder(8).inject();

        Module module1 = ModuleBuilder.builder().withName("Module1").withLessons(Arrays.asList(lesson1, lesson2, lesson3)).inject();
        Module module2 = ModuleBuilder.builder().withName("Module2").withLessons(Arrays.asList(lesson3, lesson4, lesson5)).inject();
        Module module3 = ModuleBuilder.builder().withName("Module3").withLessons(Arrays.asList(lesson5, lesson6, lesson7)).inject();

        CourseBuilder
                .builder()
                .withTitle("Course1")
                .withPriceAmount(20.0f)
                .withPriceCurrency(Currency.DKK)
                .withEnrolment(3)
                .withUsers(Arrays.asList(user1, user2, user3))
                .withModules(Arrays.asList(module1, module2))
                .inject();

        CourseBuilder
                .builder()
                .withTitle("Course2")
                .withPriceAmount(10.0f)
                .withPriceCurrency(Currency.DKK)
                .withEnrolment(1)
                .withUser(user4)
                .withModules(Arrays.asList(module2, module3))
                .inject();

        CourseBuilder
                .builder()
                .withTitle("Course3")
                .withPriceAmount(30.0f)
                .withPriceCurrency(Currency.DKK)
                .withEnrolment(2)
                .withUsers(Arrays.asList(user5, user6))
                .withModule(module3)
                .inject();
    }

    private static String readFile(String filePath) throws IOException {
        return Resources.toString(Resources.getResource(filePath), Charsets.UTF_8)
                .replaceAll("\\s+","");
    }
}
