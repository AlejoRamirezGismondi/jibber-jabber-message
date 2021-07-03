package com.jibberjabbermessage.message.repository;

import com.jibberjabbermessage.message.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
  @Query("select c from Chat c where (c.senderId = ?1 and c.recipientId = ?2) or (c.senderId = ?2 and c.recipientId = ?1)")
  Optional<Chat> findBySenderIdAndRecipientId(Long senderId, Long recipientId);
}
