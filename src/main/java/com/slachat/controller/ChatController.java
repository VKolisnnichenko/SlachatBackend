package com.slachat.controller;

import com.slachat.entity.ChatEntity;
import com.slachat.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chats")
public class ChatController {
    @Autowired
    private ChatService chatService;

    @PostMapping
    public ResponseEntity createChat(@RequestBody List<Long> participantIds) {
        try {
            ChatEntity chatEntity = chatService.createChat(participantIds);
            return ResponseEntity.ok(chatEntity);
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
}
