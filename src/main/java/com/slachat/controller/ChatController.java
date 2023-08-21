package com.slachat.controller;

import com.slachat.entity.ChatEntity;
import com.slachat.entity.ErrorResponse;
import com.slachat.entity.UserChatDTO;
import com.slachat.exceptions.TokenInvalidException;
import com.slachat.exceptions.UserNotFoundException;
import com.slachat.service.ChatService;
import com.slachat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chats")
public class ChatController {
    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;


    @PostMapping
    public ResponseEntity createChat(@RequestBody List<Long> participantIds) {
        try {
            ChatEntity chatEntity = chatService.createChat(participantIds);
            return ResponseEntity.ok(chatEntity);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    ResponseEntity deleteChat(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(chatService.deleteChat(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("{id}")
    public ResponseEntity getChatById(@PathVariable Long id) {
        try {
            ChatEntity chatEntity = chatService.getChatById(id);
            return ResponseEntity.ok(chatEntity);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/my-chats")
    public ResponseEntity getMyChats(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            List<UserChatDTO> chats = userService.getUserChats(authorizationHeader);
            return ResponseEntity.ok(chats);
        } catch (TokenInvalidException | UserNotFoundException e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

}
