package com.jibberjabbermessage.message.repository;

import com.jibberjabbermessage.message.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
  Long countBySenderIdAndRecipientIdAndReceived(Long senderId, Long recipientId, boolean received);
  Long countById(Long id);
}
