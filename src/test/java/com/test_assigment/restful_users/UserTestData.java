package com.test_assigment.restful_users;

import com.test_assigment.restful_users.entity.User;

import java.time.LocalDate;
import java.util.List;

public class UserTestData {

    public static final String USERS_REST_URL = "/rest-api/users";

    public static User john = new User(1, "john@gmail.com", "John", "Smith",
            LocalDate.of(2000, 1, 17), "Ivanyuk st.", "+380673452211");

    public static User sam = new User(2, "sam@gmail.com", "Sam", "Solar",
            LocalDate.of(1992, 10, 1), "Samarenko st.", "+380694852214");

    public static User bob = new User(3, "bob@gmail.com", "Bob", "Jenkins",
            LocalDate.of(2002, 5, 11), "Jamaika st.", "+380995667843");

    public static User sarah = new User(4, "sarah@gmail.com", "Sarah", "Hill",
            LocalDate.of(1998, 3, 22), "Hollar st.", "+380668990767");

    public static User martha = new User(5, "martha@gmail.com", "Martha", "Kim",
            LocalDate.of(1996, 1, 1), "North st.", "+380673452211");

    public static final LocalDate FROM = LocalDate.of(1990, 1, 1);

    public static final LocalDate TO = LocalDate.of(2000, 1, 1);

    public static final List<User> FILTERED = List.of(sam, sarah, martha);

    public enum ErrorMessage {
        NOT_NEW("User must be new (id=null)"),
        ID_INCONSISTENT("User must has id="),
        NOT_FOUND("Not found user with id="),
        INVALID_DATE("User birthdate must be before than current date"),
        AGE_VIOLATION("User age must be >="),
        INVALID_FILTER("Parameter 'from' must be before than 'to'"),
        INVALID_EMAIL("Email has invalid pattern"),
        BLANK(" must not be blank");

        public final String title;

        ErrorMessage(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }

    private UserTestData() {
    }

    public static User getNew() {
        return new User(null, "carol@gmail.com", "Carol", "Clark",
                LocalDate.of(1993, 8, 13), "Summer st.", "+380995544888");
    }

    public static User getUpdated() {
        User updated = new User(bob);
        updated.setEmail("bobNewEmail@gmail.com");
        return updated;
    }

    public static User getPatch() {
        User patch = new User();
        patch.setId(bob.getId());
        patch.setAddress("New home st.");
        return patch;
    }

}
