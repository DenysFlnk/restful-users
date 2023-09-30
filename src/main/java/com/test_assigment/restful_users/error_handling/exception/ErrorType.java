package com.test_assigment.restful_users.error_handling.exception;

import org.springframework.http.HttpStatus;

public enum ErrorType {
    APP_ERROR("Application error", HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_DATA("Wrong data", HttpStatus.UNPROCESSABLE_ENTITY),
    BAD_REQUEST("Bad request", HttpStatus.UNPROCESSABLE_ENTITY),
    DATA_CONFLICT("DB data conflict", HttpStatus.CONFLICT),
    NOT_FOUND("Wrong data in request", HttpStatus.NOT_FOUND);

    public final String title;

    public final HttpStatus status;

    ErrorType(String title, HttpStatus status) {
        this.title = title;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
