package com.jibberjabbermessage.message.service;

import com.jibberjabbermessage.message.model.Chat;
import com.jibberjabbermessage.message.model.Message;
import com.jibberjabbermessage.message.model.dto.MessageDTO;
import com.jibberjabbermessage.message.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MessageService {
  
  @Autowired private MessageRepository messageRepository;
  @Autowired private ChatService chatService;
  
  public Message save(Message message) {
    final Optional<Chat> optional = chatService.findBySenderIdAndRecipientId(message.getSenderId(), message.getRecipientId());
    message.setReceived(false);
    message.setTimestamp(LocalDate.now());
    message.setChat(optional.get());
    return messageRepository.save(message);
  }
  
  public int countNewMessages(Long senderId, Long recipientId, Long myId) {
    return (chatService.findBySenderIdAndRecipientId(senderId, recipientId).get().getMessages().stream().filter(m -> m.getRecipientId().equals(myId) && !m.isReceived()).collect(Collectors.toList()).size());
  }
  
  public Long countNewMessages(Long chatId) {
    return messageRepository.countById(chatId);
  }
  
  public List<Message> findChatMessages(Long senderId, Long recipientId, Long myId) {
    final Optional<Chat> optional = chatService.findBySenderIdAndRecipientId(senderId, recipientId);
    if (optional.isEmpty()) return new ArrayList<>();
    return getMessagesAndUpdate(optional.get(), myId);
  }
  
  public List<Message> findChatMessages(Long chatId, Long myId) {
    return getMessagesAndUpdate(chatService.findById(chatId), myId);
  }
  
  private List<Message> getMessagesAndUpdate(Chat chat, Long userId) {
    final List<Message> messages = chat.getMessages();
    final List<Message> updated = new ArrayList<>(messages);
    updated.forEach(m -> {
      if (m.getRecipientId().equals(userId)) {
        m.setReceived(true);
      }
    });
    chat.setMessages(updated);
    chatService.save(chat);
    return messages;
  }

  public Message toMessage(MessageDTO dto) {
    Message message = new Message();
    message.setSenderId(dto.getSenderId());
    message.setRecipientId(dto.getRecipientId());
    message.setTimestamp(LocalDate.now());
    message.setContent(dto.getContent());
    return message;
  }
}
