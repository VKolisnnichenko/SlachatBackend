package com.slachat.entity;

import com.slachat.model.UserModel;

public class UserChatDTO {
    private Long chatId;

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public UserChatDTO() {

    }

    public UserChatDTO(Long chatId, UserModel user) {
        this.chatId = chatId;
        this.user = user;
    }

    private UserModel user;

}
