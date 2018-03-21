package builders;

import models.entities.Lesson;

public class LessonBuilder extends AbstractBuilder<Lesson> {
    private Integer order;
    private String title;
    private String content;

    public static LessonBuilder builder() {
        return new LessonBuilder();
    }

    public LessonBuilder withOrder(Integer order) {
        this.order = order;
        return this;
    }

    public LessonBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public LessonBuilder withContent(String content) {
        this.content = content;
        return this;
    }

    @Override
    public Lesson build() {
        Lesson lesson = new Lesson();
        lesson.setId(id);
        lesson.setOrder(order);
        lesson.setTitle(title);
        lesson.setContent(content);
        return lesson;
    }
}
