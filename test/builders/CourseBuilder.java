package builders;

import models.entities.Course;
import models.entities.Currency;
import models.entities.Module;
import models.entities.User;

import java.util.ArrayList;
import java.util.List;

public class CourseBuilder extends AbstractBuilder<Course> {
    private String title;
    private Float priceAmount;
    private Currency priceCurrency;
    private Integer enrolment = 0;
    private List<Module> modules = new ArrayList<>();
    private List<User> users = new ArrayList<>();

    public static CourseBuilder builder() {
        return new CourseBuilder();
    }

    public CourseBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public CourseBuilder withPriceAmount(Float priceAmount) {
        this.priceAmount = priceAmount;
        return this;
    }

    public CourseBuilder withPriceCurrency(Currency priceCurrency) {
        this.priceCurrency = priceCurrency;
        return this;
    }

    public CourseBuilder withEnrolment(Integer enrolment) {
        this.enrolment = enrolment;
        return this;
    }

    public CourseBuilder withModules(List<Module> modules) {
        this.modules.addAll(modules);
        return this;
    }

    public CourseBuilder withModule(Module module) {
        this.modules.add(module);
        return this;
    }

    public CourseBuilder withUsers(List<User> users) {
        this.users.addAll(users);
        return this;
    }

    public CourseBuilder withUser(User user) {
        this.users.add(user);
        return this;
    }

    @Override
    public Course build() {
        Course course = new Course();
        course.setId(id);
        course.setTitle(title);
        course.setEnrolments(enrolment);
        course.setPriceAmount(priceAmount);
        course.setPriceCurrency(priceCurrency);
        if(modules.size() > 0) {
            course.setModules(modules);
        }
        if(users.size() > 0) {
            course.setUsers(users);
        }
        return course;
    }
}
