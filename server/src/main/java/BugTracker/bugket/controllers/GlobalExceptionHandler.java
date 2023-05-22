package BugTracker.bugket.controllers;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorHandler> handleException(DataAccessException ex) {
        return new ResponseEntity<>(
                new ErrorHandler("The server threw an exception, please contact an administrator."),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorHandler> handleException(Exception ex) {
        return new ResponseEntity<>(
                new ErrorHandler("The server threw an unknown exception, please contact an administrator."),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
