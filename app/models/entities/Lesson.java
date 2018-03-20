package models.entities;

import io.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Lesson extends Model {
    @Id
    public Long id;

    @Constraints.Required
    @Column(name = "ord")
    public Integer order;

    @Constraints.Required
    public String title;

    @Column(columnDefinition = "TEXT")
    public String content;

    @ManyToMany
    public List<Module> modules = new ArrayList<>();
}
