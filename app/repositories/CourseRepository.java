package repositories;

import io.ebean.*;
import models.entities.Course;
import models.entities.User;
import play.db.ebean.EbeanConfig;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class CourseRepository {
    private final Map<String, String> validSortBy = new HashMap<String, String>() {{
        put("price",      "priceAmount");
        put("enrolments", "enrolments");
    }};

    private final EbeanServer ebeanServer;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public CourseRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
    }

    public CompletionStage<Course> single(long id) {
        return supplyAsync(() -> ebeanServer
                .find(Course.class)
                .fetch("modules")
                .fetch("modules.lessons")
                .where().idEq(id)
                .setMaxRows(1)  // I <3 eBean (don't remove this line)
                .findOne(), executionContext);
    }

    public CompletionStage<List<User>> getUsers(long id) {
        return supplyAsync(() -> Objects.requireNonNull(ebeanServer
                .find(Course.class)
                .fetch("users")
                .where().idEq(id)
                .setMaxRows(1)  // I <3 eBean (don't remove this line)
                .findOne())
                .getUsers(), executionContext);
    }

    public CompletionStage<List<Course>> page(int page, int pageSize, String sortBy, String order, String filter) {
        return supplyAsync(() ->
                ebeanServer
                        .find(Course.class)
                        .fetch("modules")
                        .fetch("modules.lessons")
                        .where()
                        .ilike("title", "%" + filter + "%")
                        .orderBy(validSortBy.getOrDefault(sortBy, "priceAmount") + " " + order)
                        .setFirstRow(page * pageSize)
                        .setMaxRows(pageSize)
                        .findList(), executionContext);
    }

    public CompletionStage<Course> insert(Course course) {
        return supplyAsync(() -> {
            ebeanServer.insert(course);
            return course;
        }, executionContext);
    }

    public CompletionStage<Optional<Course>> update(long id, Course newCourse) {
        return supplyAsync(() -> {
            Transaction txn = ebeanServer.beginTransaction();
            Optional<Course> value = Optional.empty();
            try {
                Course savedCourse = ebeanServer.find(Course.class).setId(id).findOne();
                if (savedCourse != null) {
                    savedCourse.setTitle(newCourse.getTitle());
                    savedCourse.setPriceAmount(newCourse.getPriceAmount());
                    savedCourse.setPriceCurrency(newCourse.getPriceCurrency());
                    savedCourse.update();
                    txn.commit();
                    value = Optional.of(savedCourse);
                }
            } finally {
                txn.end();
            }
            return value;
        }, executionContext);
    }
}
