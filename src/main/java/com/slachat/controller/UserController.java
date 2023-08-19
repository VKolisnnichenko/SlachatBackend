package com.slachat.controller;

import com.slachat.entity.ErrorResponse;
import com.slachat.entity.UserEntity;
import com.slachat.exceptions.InvalidCredentialsException;
import com.slachat.exceptions.TokenInvalidException;
import com.slachat.exceptions.UserAlreadyExistException;
import com.slachat.exceptions.UserNotFoundException;
import com.slachat.model.LoginDTO;
import com.slachat.model.UserModel;
import com.slachat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {


    @Autowired
    private UserService userService;

    @PostMapping("/")
    public ResponseEntity registration(@RequestBody UserEntity user) {
        try {
            var userEntity = userService.registration(user);
            return ResponseEntity.ok(userEntity);
        } catch (UserAlreadyExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("{id}")
    public ResponseEntity getUserById(@PathVariable long id) {
        try {
            return ResponseEntity.ok(userService.findUserById(id));
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/")
    public ResponseEntity getUserByUserName(@RequestParam String userName,
                                            @RequestHeader("Authorization") String authorizationHeader) {
        try {
            return ResponseEntity.ok(userService.findUserByUserName(userName, authorizationHeader));
        } catch (UserNotFoundException | TokenInvalidException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @DeleteMapping("{id}")
    public ResponseEntity deleteUserById(@PathVariable long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("User was deleted with id : " + id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDTO loginRequest) {
        try {
            return ResponseEntity.ok(userService.login(loginRequest));
        } catch (UserNotFoundException | InvalidCredentialsException e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity getUserProfile(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            UserModel userProfile = userService.getUserProfile(authorizationHeader);
            return ResponseEntity.ok(userProfile);
        } catch (TokenInvalidException | UserNotFoundException e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
