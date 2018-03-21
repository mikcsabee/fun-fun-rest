package builders;

import models.entities.User;

public class UserBuilder extends AbstractBuilder<User> {
    private String name;
    private String username;

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public UserBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    @Override
    public User build() {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setUsername(username);
        return user;
    }
}
