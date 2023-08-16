package com.slachat.model;

import com.slachat.entity.ChatEntity;
import com.slachat.entity.UserEntity;

import java.util.List;

public class UserModel {
    private Long id;
    private String username;

    private List<ChatEntity> chats;


    public Long getId() {
        return id;
    }

    public UserModel(Long id, String username, List<ChatEntity> chats) {
        this.id = id;
        this.username = username;
        this.chats = chats;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setChats(List<ChatEntity> chats) {
        this.chats = chats;
    }

    public static UserModel mapUser(UserEntity userEntity) {
        return new UserModel(userEntity.getId(), userEntity.getUserName(), userEntity.getChats());
    }

    public List<ChatEntity> getChats() {
        return chats;
    }
}

