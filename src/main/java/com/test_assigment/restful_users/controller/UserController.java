package com.test_assigment.restful_users.controller;

import com.test_assigment.restful_users.entity.User;
import com.test_assigment.restful_users.repository.UserRepository;
import com.test_assigment.restful_users.util.UserUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static com.test_assigment.restful_users.util.ValidationUtil.*;

@Tag(name = "Users", description = "Operations with user accounts")
@RestController
@RequestMapping(value = "rest-api/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class UserController {

    private final UserRepository userRepository;

    @Value("${user.age.restriction}")
    private int ageRestriction;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Operation(summary = "Register new user")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody @Valid User user) {
        log.info("create user {}", user);
        birthdayValidation(user, ageRestriction);
        checkNew(user);
        return userRepository.save(user);
    }

    @Operation(summary = "Full edit user")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int id, @RequestBody @Valid User user) {
        log.info("update user {}, data {}", id, user);
        assureIdConsistent(user, id);
        birthdayValidation(user, ageRestriction);
        userRepository.save(user);
    }

    @Operation(summary = "Partial edit user")
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void patchUpdate(@PathVariable int id, @RequestBody User patch) {
        log.info("patchUpdate user {}, data {}", id, patch);
        assureIdConsistent(patch, id);
        User user = checkNotFoundWithId(userRepository.findById(id).orElse(null), id);
        UserUtil.patchUpdateUser(user, patch, ageRestriction);
        userRepository.save(user);
    }

    @Operation(summary = "Delete user by id")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete user {}", id);
        User deleteUser = checkNotFoundWithId(userRepository.findById(id).orElse(null), id);
        userRepository.delete(deleteUser);
    }

    @Operation(summary = "Retrieve users by birthdate range 'from'-'to' (inclusive)")
    @GetMapping
    public List<User> getByBirthdateRange(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        log.info("getByBirthdateRange from {} to {}", from, to);
        checkDateFilter(from, to);
        List<User> users = userRepository.findAll();
        return UserUtil.filterUsersByDate(users, from, to);
    }
}
