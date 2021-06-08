package io.todolist.api.controller;

import io.todolist.api.dto.ErrorMessage;
import io.todolist.server.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity<ErrorMessage> manageUserNotFound(UserNotFoundException userNotFoundException){
        ErrorMessage error = new ErrorMessage();
        error.setMessage(userNotFoundException.getMessage());
        return ResponseEntity.status(404).body(error);
    }

    @ExceptionHandler(value = {NotValidUserException.class})
    public ResponseEntity<ErrorMessage> manageNotValidUser(NotValidUserException notValidUserException){
        ErrorMessage error = new ErrorMessage();
        error.setMessage(notValidUserException.getMessage());
        return ResponseEntity.status(400).body(error);
    }

    @ExceptionHandler(value = {TaskNotFoundException.class})
    public ResponseEntity<ErrorMessage> manageTaskNotFound(TaskNotFoundException taskNotFoundException){
        ErrorMessage error = new ErrorMessage();
        error.setMessage(taskNotFoundException.getMessage());
        return ResponseEntity.status(404).body(error);
    }

    @ExceptionHandler(value = {TaskContentHasTooManyCharactersException.class})
    public ResponseEntity<ErrorMessage> manageTaskContentHasTooManyCharacters(TaskContentHasTooManyCharactersException taskContentHasTooManyCharactersException){
        ErrorMessage error = new ErrorMessage();
        error.setMessage(taskContentHasTooManyCharactersException.getMessage());
        return ResponseEntity.status(404).body(error);
    }

    @ExceptionHandler(value = {TaskCreationUnderThirtyMinuteException.class})
    public ResponseEntity<ErrorMessage> manageTaskCreationUnderThirtyMinute(TaskCreationUnderThirtyMinuteException taskCreationUnderThirtyMinuteException){
        ErrorMessage error = new ErrorMessage();
        error.setMessage(taskCreationUnderThirtyMinuteException.getMessage());
        return ResponseEntity.status(425).body(error);
    }

    @ExceptionHandler(value = {TaskNameAlreadyTakenException.class})
    public ResponseEntity<ErrorMessage> manageTaskNameAlreadyTaken(TaskNameAlreadyTakenException taskNameAlreadyTakenException){
        ErrorMessage error = new ErrorMessage();
        error.setMessage(taskNameAlreadyTakenException.getMessage());
        return ResponseEntity.status(404).body(error);
    }

    @ExceptionHandler(value = {TooManyTasksInToDoListException.class})
    public ResponseEntity<ErrorMessage> manageTooManyTasksInToDoList(TooManyTasksInToDoListException tooManyTasksInToDoListException){
        ErrorMessage error = new ErrorMessage();
        error.setMessage(tooManyTasksInToDoListException.getMessage());
        return ResponseEntity.status(404).body(error);
    }
}
