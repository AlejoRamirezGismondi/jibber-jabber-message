package com.jibberjabbermessage.message.repository;

import com.jibberjabbermessage.message.model.Message;
import com.jibberjabbermessage.message.model.MessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
  Long countByIdAndRecipientIdAndStatus(Long senderId, Long recipientId, MessageStatus status);
  Long countById(Long id);
}
