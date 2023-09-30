package com.test_assigment.restful_users.util;

import com.test_assigment.restful_users.entity.User;
import com.test_assigment.restful_users.error_handling.exception.IllegalRequestDataException;
import com.test_assigment.restful_users.error_handling.exception.NotFoundException;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class ValidationUtil {

    private ValidationUtil() {
    }

    public static User checkNotFoundWithId(User user, int id) {
        if (user == null) {
            throw new NotFoundException("Not found user with id=" + id);
        }
        return user;
    }

    public static void checkNew(User user) {
        if (!user.isNew()) {
            throw new IllegalRequestDataException("User must be new (id=null)");
        }
    }

    public static void assureIdConsistent(User user, int id) {
        if (user.isNew()) {
            throw new IllegalRequestDataException("User must has id");
        }
        if (user.getId() != id) {
            throw new IllegalRequestDataException("User must has id=" + id);
        }
    }

    public static void birthdayValidation(User user, int ageRestriction) {
        LocalDate currentDate = LocalDate.now();
        if (user.getBirthdate().isAfter(currentDate) || user.getBirthdate().isEqual(currentDate)) {
            throw new IllegalRequestDataException("User birthdate must be before than current date");
        }

        int yearsOld = currentDate.getYear() - user.getBirthdate().getYear();
        if (yearsOld < ageRestriction) {
            throw new IllegalRequestDataException("User age must be >=" + ageRestriction);
        }
    }

    public static void checkDateFilter(LocalDate from, LocalDate to) {
        if (from.isAfter(to) || from.isEqual(to)) {
            throw new IllegalRequestDataException("Parameter 'from' must be before than 'to'");
        }
    }

    public static void checkEmailPattern(String email) {
        String regexValid = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        if (!Pattern.matches(regexValid, email)) {
            throw new IllegalRequestDataException("Email has invalid pattern");
        }
    }

    public static void checkNotBlank(String name, String value) {
        String check = value.replace(" ", "");
        if (check.length() < 1) {
            throw new IllegalRequestDataException(name + " must not be blank");
        }
    }

}
