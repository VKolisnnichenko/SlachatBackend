package com.slachat.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "message_table")
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private ChatEntity chat;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity sender;

    @Column(name = "content")
    private String content;

}
