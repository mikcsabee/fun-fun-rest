package builders;

import models.entities.Lesson;
import models.entities.Module;

import java.util.ArrayList;
import java.util.List;

public class ModuleBuilder extends AbstractBuilder<Module> {
    private String name;
    private List<Lesson> lessons = new ArrayList<>();

    public static ModuleBuilder builder() {
        return new ModuleBuilder();
    }

    public ModuleBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ModuleBuilder withLessons(List<Lesson> lessons) {
        this.lessons.addAll(lessons);
        return this;
    }

    public ModuleBuilder withLesson(Lesson lesson) {
        this.lessons.add(lesson);
        return this;
    }

    @Override
    public Module build() {
        Module module = new Module();
        module.setId(id);
        module.setName(name);
        if(lessons.size() > 0) {
            module.setLessons(lessons);
        }
        return module;
    }
}
