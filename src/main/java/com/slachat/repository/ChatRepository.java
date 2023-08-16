package com.slachat.repository;

import com.slachat.entity.ChatEntity;
import org.springframework.data.repository.CrudRepository;

public interface ChatRepository extends CrudRepository<ChatEntity, Long> {
}
