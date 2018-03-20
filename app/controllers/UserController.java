package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.entities.User;
import models.view.CourseEnrolmentItem;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import repositories.UserRepository;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

public class UserController extends Controller {
    private UserRepository userRepository;

    @Inject
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> create() {
        JsonNode json = request().body().asJson();
        User user = Json.fromJson(json, User.class);
        return userRepository
                .insert(user)
                .thenApplyAsync(c -> ok(Json.toJson(c)));
    }

    public CompletionStage<Result> bindCourse(long userId, long courseId) {
        return userRepository
                .bindCourse(userId, courseId)
                .thenApplyAsync(c -> ok(Json.toJson(c)));
    }

    public CompletionStage<Result> courses(long userId) {
        return userRepository
                .getCourses(userId)
                .thenApplyAsync(courses -> {
                    List<CourseEnrolmentItem> result = courses.
                            stream()
                            .map(CourseEnrolmentItem::new)
                            .collect(Collectors.toList());
                    return ok(Json.toJson(result));
                });
    }
}
