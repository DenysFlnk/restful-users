package com.test_assigment.restful_users.util;

import com.test_assigment.restful_users.entity.User;

import java.time.LocalDate;
import java.util.List;

import static com.test_assigment.restful_users.util.ValidationUtil.*;

public class UserUtil {

    private UserUtil() {
    }

    public static void patchUpdateUser(User user, User patch, int ageRestriction) {
        if (patch.getEmail() != null) {
            checkEmailPattern(patch.getEmail());
            user.setEmail(patch.getEmail());
        }
        if (patch.getFirstName() != null) {
            checkNotBlank("First name", patch.getFirstName());
            user.setFirstName(patch.getFirstName());
        }
        if (patch.getLastName() != null) {
            checkNotBlank("Last name", patch.getLastName());
            user.setLastName(patch.getLastName());
        }
        if (patch.getBirthdate() != null) {
            birthdayValidation(patch, ageRestriction);
            user.setBirthdate(patch.getBirthdate());
        }
        if (patch.getAddress() != null) {
            checkNotBlank("Address", patch.getAddress());
            user.setAddress(patch.getAddress());
        }
        if (patch.getPhoneNumber() != null) {
            checkNotBlank("Phone number", patch.getPhoneNumber());
            user.setPhoneNumber(patch.getPhoneNumber());
        }
    }

    public static List<User> filterUsersByDate(List<User> users, LocalDate from, LocalDate to) {
        return users.stream().filter(user -> isBetween(user.getBirthdate(), from, to)).toList();
    }

    private static <LocalDate extends Comparable<LocalDate>> boolean isBetween(LocalDate date, LocalDate from,
                                                                               LocalDate to) {
        return (date.compareTo(from) >= 0) && (date.compareTo(to) <= 0);
    }
}
