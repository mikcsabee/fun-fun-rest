package models.view;

import models.entities.Course;

public class CourseEnrolmentItem {
    public final Long id;
    public final String title;

    public CourseEnrolmentItem(Course course) {
        this.id = course.getId();
        this.title = course.getTitle();
    }
}

