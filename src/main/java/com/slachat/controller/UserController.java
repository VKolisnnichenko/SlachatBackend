package com.slachat.controller;

import com.slachat.entity.UserEntity;
import com.slachat.exceptions.UserAlreadyExistException;
import com.slachat.exceptions.UserNotFoundException;
import com.slachat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity getUserByUserName(@RequestParam String userName) {
        try {
            return ResponseEntity.ok(userService.findUserByUserName(userName));
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteUserById(@PathVariable long id){
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("User was deleted with id : "+id);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
