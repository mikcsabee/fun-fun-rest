package models.view;

import models.entities.User;

public class UserEnrolmentItem {
    public final Long id;

    public final String name;

    public final String username;

    public UserEnrolmentItem(User user) {
        id = user.getId();
        name = user.getName();
        username = user.getUsername();
    }
}
