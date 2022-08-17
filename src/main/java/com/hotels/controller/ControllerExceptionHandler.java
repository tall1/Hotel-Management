package com.hotels.controller;

import com.hotels.exceptions.EmailAlreadyExistsException;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityNotFoundException;
import java.sql.SQLException;

@ControllerAdvice
@ResponseBody
public class ControllerExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public ErrorMessage resourceNotFoundException(EntityNotFoundException ex) {
    return new ErrorMessage(ex.getMessage());
  }

  @ExceptionHandler(SQLException.class)
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorMessage sqlException(SQLException ex) {
    return new ErrorMessage("SQL Problem: " + ex.getMessage());
  }

  @ExceptionHandler(EmailAlreadyExistsException.class)
  @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
  public ErrorMessage emailAlreadyExistsException(EmailAlreadyExistsException ex) {
    return new ErrorMessage("Email already exists: " + ex.getMessage());
  }
}
