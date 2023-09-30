package com.test_assigment.restful_users.error_handling.exception;

public record ErrorInfo (String url, ErrorType type, String typeMessage, String[] details){
}