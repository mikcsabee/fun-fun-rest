package models.entities;

import io.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import play.data.validation.Constraints;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Course extends Model {
    @Id
    private Long id;

    @Constraints.Required
    private String title;

    @Constraints.Required
    private Float priceAmount;

    @Constraints.Required
    private Currency priceCurrency;

    private Integer enrolment;

    @ManyToMany
    private List<User> users = new ArrayList<>();

    @ManyToMany
    private List<Module> modules = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Float getPriceAmount() {
        return priceAmount;
    }

    public void setPriceAmount(Float priceAmount) {
        this.priceAmount = priceAmount;
    }

    public Currency getPriceCurrency() {
        return priceCurrency;
    }

    public void setPriceCurrency(Currency priceCurrency) {
        this.priceCurrency = priceCurrency;
    }

    public Integer getEnrolment() {
        return enrolment;
    }

    public void setEnrolment(Integer enrolment) {
        this.enrolment = enrolment;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }
}
