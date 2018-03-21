package models.view;

import models.entities.Course;
import models.entities.Currency;

import java.util.ArrayList;
import java.util.List;

public class CourseViewItem {
    final public Long id;
    final public Price price;
    final public Integer enrolments;
    final public String title;
    final public List<Module> modules;

    public CourseViewItem(Course course) {
        id = course.getId();
        price = new Price(course.getPriceAmount(), course.getPriceCurrency());
        enrolments = course.getEnrolments();
        title = course.getTitle();
        modules = new ArrayList<>();
        course.getModules().forEach(module -> modules.add(new Module(module)));
    }

    public static class Price {
        final public float amount;
        final public String currency;

        public Price(float amount, Currency currency) {
            this.amount = amount;
            this.currency = currency.name();
        }
    }

    public static class Module {
        public final Long id;
        public final Integer order;
        public final List<Lesson> lessons;

        public Module(models.entities.Module module) {
            id = module.getId();
            order = module.getOrder();
            lessons = new ArrayList<>();
            module.getLessons().forEach(lesson -> lessons.add(new Lesson(lesson)));
        }
    }

    public static class Lesson {
        public final Long id;
        public final Integer order;
        public final String title;
        public final String content;

        public Lesson(models.entities.Lesson lesson) {
            id = lesson.getId();
            order = lesson.getOrder();
            title = lesson.getTitle();
            content = lesson.getContent();
        }
    }
}
