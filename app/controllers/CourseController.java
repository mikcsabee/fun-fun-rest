package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.entities.Course;
import models.view.CourseEnrolmentItem;
import models.view.CourseListItem;
import models.view.UserEnrolmentItem;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import repositories.CourseRepository;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

public class CourseController extends Controller {
    private CourseRepository courseRepository;

    @Inject
    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public CompletionStage<Result> single(Long id) {
        return courseRepository
                .single(id)
                .thenApplyAsync(item -> ok(Json.toJson(new CourseListItem(item))))
                .exceptionally(e -> badRequest(e.getMessage()));
    }

    public CompletionStage<Result> users(long courseId) {
        return courseRepository
                .getUsers(courseId)
                .thenApplyAsync(courses -> {
                    List<UserEnrolmentItem> result = courses.
                            stream()
                            .map(UserEnrolmentItem::new)
                            .collect(Collectors.toList());
                    return ok(Json.toJson(result));
                });
    }

    public CompletionStage<Result> list(int page, String sortBy, String order, String filter) {
        return courseRepository
                .page(page, 10, sortBy, order.toUpperCase(), filter)
                .thenApplyAsync(list -> {
                    List<CourseListItem> output =
                            list
                            .stream()
                            .map(CourseListItem::new)
                            .collect(Collectors.toList());
                    return ok(Json.toJson(output));
                })
                .exceptionally(e -> badRequest(e.getMessage()));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> create() {
        JsonNode json = request().body().asJson();
        Course course = Json.fromJson(json, Course.class);
        return courseRepository
                .insert(course)
                .thenApplyAsync(c -> ok(Json.toJson(c)));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> update(Long id) {
        JsonNode json = request().body().asJson();
        Course course = Json.fromJson(json, Course.class);
        return courseRepository
                .update(id, course)
                .thenApplyAsync(c -> ok(Json.toJson(c)));
    }
}
