package models.entities;

import io.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Module extends Model {
    @Id
    public Long id;

    @Constraints.Required
    public String name;

    @ManyToMany
    public List<Course> courses = new ArrayList<>();

    @ManyToMany
    public List<Lesson> lessons = new ArrayList<>();
}
