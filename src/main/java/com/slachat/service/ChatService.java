package com.slachat.service;

import com.slachat.entity.ChatEntity;
import com.slachat.entity.UserEntity;
import com.slachat.exceptions.ChatNotFoundException;
import com.slachat.model.UserModel;
import com.slachat.repository.ChatRepository;
import com.slachat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatRepository chatRepository;

    public ChatEntity createChat(List<Long> participantIds) {
        List<UserEntity> participants = (List<UserEntity>) userRepository.findAllById(participantIds);
        ChatEntity chat = new ChatEntity();
        chat.setParticipants(participants);
        return chatRepository.save(chat);
    }

    public ChatEntity getChatById(Long id) throws ChatNotFoundException {
        var chat = chatRepository.findById(id);
        if (chat.isPresent()) {
            return chat.get();
        } else {
            throw new ChatNotFoundException("Chat not found with id:" + id);
        }
    }
}
