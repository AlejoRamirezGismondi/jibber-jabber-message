package com.jibberjabbermessage.message.repository;

import com.jibberjabbermessage.message.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
  Optional<Chat> findBySenderIdAndRecipientId(Long senderId, Long recipientId);
}
