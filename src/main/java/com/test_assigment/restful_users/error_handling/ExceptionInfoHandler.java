package com.test_assigment.restful_users.error_handling;

import com.test_assigment.restful_users.error_handling.exception.ErrorInfo;
import com.test_assigment.restful_users.error_handling.exception.ErrorType;
import com.test_assigment.restful_users.error_handling.exception.IllegalRequestDataException;
import com.test_assigment.restful_users.error_handling.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import static com.test_assigment.restful_users.error_handling.exception.ErrorType.*;


@RestControllerAdvice
@Slf4j
public class ExceptionInfoHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorInfo> notFoundError(HttpServletRequest req, NotFoundException e) {
        return logAndGetErrorInfo(req, e, false, NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorInfo> conflict(HttpServletRequest req, DataIntegrityViolationException e) {
        return logAndGetErrorInfo(req, e, true, DATA_CONFLICT);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorInfo> bindValidationError(HttpServletRequest req, BindException e) {
        String[] details = e.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toArray(String[]::new);

        return logAndGetErrorInfo(req, e, false, BAD_DATA, details);
    }

    @ExceptionHandler({IllegalRequestDataException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErrorInfo> validationError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, false, BAD_REQUEST);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorInfo> noHandlerFoundError(HttpServletRequest req, NoHandlerFoundException e) {
        return logAndGetErrorInfo(req, e, false, NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorInfo> internalError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, true, APP_ERROR);
    }

    private static ResponseEntity<ErrorInfo> logAndGetErrorInfo(HttpServletRequest req, Exception e,
                                                                boolean logStackTrace,
                                                                ErrorType errorType, String... details) {
        Throwable rootCause = logAndGetRootCause(log, req, e, logStackTrace, errorType);
        String[] msg = details.length != 0 ? details : new String[]{getMessage(rootCause)};

        if (e instanceof DataIntegrityViolationException) {
            msg = new String[]{getIntegrityViolationMessage(rootCause)};
        }

        return ResponseEntity.status(errorType.getStatus())
                .body(new ErrorInfo(req.getRequestURL().toString(), errorType, errorType.getTitle(), msg));
    }

    public static Throwable logAndGetRootCause(Logger log, HttpServletRequest req, Exception e, boolean logStackTrace,
                                               ErrorType errorType) {
        Throwable rootCause = getRootCause(e);
        if (logStackTrace) {
            log.error("{} at request  {}: {}", errorType, req.getRequestURL(), rootCause.toString());
        } else {
            log.warn("{} at request  {}: {}", errorType, req.getRequestURL(), rootCause.toString());
        }
        return rootCause;
    }

    private static Throwable getRootCause(@NonNull Throwable t) {
        Throwable rootCause = NestedExceptionUtils.getRootCause(t);
        return rootCause != null ? rootCause : t;
    }

    private static String getIntegrityViolationMessage(Throwable e) {
        String message = getMessage(e);
        if (message.contains("email")) {
            return "This email address already exists in the database";
        }
        return null;
    }

    private static String getMessage(Throwable e) {
        return e.getLocalizedMessage() != null ? e.getLocalizedMessage() : e.getClass().getName();
    }
}