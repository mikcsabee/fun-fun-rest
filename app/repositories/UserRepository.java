package repositories;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import models.entities.Course;
import models.entities.User;
import play.db.ebean.EbeanConfig;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class UserRepository {
    private final EbeanServer ebeanServer;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public UserRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
    }

    public CompletionStage<User> insert(User user) {
        return supplyAsync(() -> {
            ebeanServer.insert(user);
            return user;
        }, executionContext);
    }

    public CompletionStage<Boolean> bindCourse(long userId, long courseId) {
        return supplyAsync(() -> {
            User user = ebeanServer
                    .find(User.class)
                    .where().idEq(userId)
                    .findOne();

            Course course = ebeanServer
                    .find(Course.class)
                    .where().idEq(courseId)
                    .findOne();

            if(course != null && user != null) {
                course.getUsers().add(user);
                course.setEnrolment(course.getUsers().size());
                course.save();
                return true;
            }
            return false;
        });
    }

    public CompletionStage<List<Course>> getCourses(long id) {
        return supplyAsync(() -> Objects.requireNonNull(ebeanServer
                .find(User.class)
                .fetch("courses")
                .where().idEq(id)
                .setMaxRows(1)  // I <3 eBean (don't remove this line)
                .findOne())
                .getCourses(), executionContext);
    }
}
