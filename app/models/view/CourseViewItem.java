package models.view;

import models.entities.Course;
import models.entities.Currency;

public class CourseListItem {
    final public Long id;
    final public Price price;
    final public Integer enrolments;
    final public String title;

    public CourseListItem(Course course) {
        id = course.getId();
        price = new Price(course.getPriceAmount(), course.getPriceCurrency());
        enrolments = course.getEnrolment();
        title = course.getTitle();
    }

    public static class Price {
        final public float amount;
        final public String currency;

        public Price(float amount, Currency currency) {
            this.amount = amount;
            this.currency = currency.name();
        }
    }
}
