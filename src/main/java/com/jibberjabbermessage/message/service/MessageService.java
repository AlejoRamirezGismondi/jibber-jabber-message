package com.jibberjabbermessage.message.service;

import com.jibberjabbermessage.message.model.Chat;
import com.jibberjabbermessage.message.model.Message;
import com.jibberjabbermessage.message.model.MessageStatus;
import com.jibberjabbermessage.message.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
  
  @Autowired private MessageRepository messageRepository;
  @Autowired private ChatService chatService;
  
  public Message save(Message message) {
    message.setStatus(MessageStatus.RECEIVED);
    return messageRepository.save(message);
  }
  
  public Long countNewMessages(Long senderId, Long recipientId) {
    return messageRepository.countByIdAndRecipientIdAndStatus(senderId, recipientId, MessageStatus.DELIVERED);
  }
  
  public Long countNewMessages(Long chatId) {
    return messageRepository.countById(chatId);
  }
  
  public List<Message> findChatMessages(Long senderId, Long recipientId) {
    final Optional<Chat> optional = chatService.findBySenderIdAndRecipientId(senderId, recipientId);
    if (optional.isEmpty()) return new ArrayList<>();
    return getMessagesAndUpdate(optional.get());
  }
  
  public List<Message> findChatMessages(Long chatId) {
    return getMessagesAndUpdate(chatService.findById(chatId));
  }
  
  private List<Message> getMessagesAndUpdate(Chat chat) {
    final List<Message> messages = chat.getMessages();
    final List<Message> updated = new ArrayList<>(messages);
    updated.forEach(m -> {
      m.setStatus(MessageStatus.DELIVERED);
    });
    chat.setMessages(updated);
    chatService.save(chat);
    return messages;
  }
}
