package com.jibberjabbermessage.message.service;

import com.jibberjabbermessage.message.model.Chat;
import com.jibberjabbermessage.message.model.Message;
import com.jibberjabbermessage.message.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class ChatService {
  
  @Autowired
  private ChatRepository chatRepository;
  
  public Long getChatId(Long senderId, Long recipientId) {
    final Optional<Chat> optional = chatRepository.findBySenderIdAndRecipientId(senderId, recipientId);
    if (optional.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    return optional.get().getId();
  }
  
  public Optional<Chat> findBySenderIdAndRecipientId(Long senderId, Long recipientId) {
    return chatRepository.findBySenderIdAndRecipientId(senderId, recipientId);
  }
  
  
  public void addMessage(Message message) {
    final Optional<Chat> optional = chatRepository.findBySenderIdAndRecipientId(message.getSenderId(), message.getRecipientId());
    final Chat chat;
    if (optional.isPresent()) {
      chat = optional.get();
      chat.addMessage(message);
      message.setChat(chat);
      chatRepository.save(chat);
    }
  }
  
  public Chat findById(Long id) {
    final Optional<Chat> optional = chatRepository.findById(id);
    if (optional.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    return optional.get();
  }
  
  private Chat createNewChat(Message message) {
    final Chat chat = new Chat();
    chat.setSenderId(message.getSenderId());
    chat.setRecipientId(message.getRecipientId());
    return chat;
  }
  
  public void save(Chat chat) {
    chatRepository.save(chat);
  }
  
  public void createIfNotExists(Message message) {
    final Optional<Chat> optional = chatRepository.findBySenderIdAndRecipientId(message.getSenderId(), message.getRecipientId());
    final Chat chat;
    if (optional.isEmpty()) {
      chat = createNewChat(message);
      chatRepository.save(chat);
    }
  }
}
