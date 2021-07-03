package com.jibberjabbermessage.message.controller;

import com.jibberjabbermessage.message.model.ChatNotification;
import com.jibberjabbermessage.message.model.Message;
import com.jibberjabbermessage.message.service.ChatService;
import com.jibberjabbermessage.message.service.MessageService;
import com.jibberjabbermessage.message.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
public class ChatController {
  @Autowired
  private SimpMessagingTemplate messagingTemplate;
  @Autowired
  private MessageService messageService;
  @Autowired
  private ChatService chatService;
  @Autowired
  private UserService userService;
  
  @MessageMapping("/chat")
  public void processMessage(@RequestHeader("Authorization") String token, @Payload Message message) {
    final Long userId = userService.getUserId(token);
    if (!message.getSenderId().equals(userId)) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    Message saved = messageService.save(message);
    chatService.addMessage(saved);
    messagingTemplate.convertAndSend(
           "/queue/messages",
            new ChatNotification(
                    saved.getId(),
                    saved.getSenderId(),
                    userService.getUserName(token, saved.getSenderId())));
  }
  
  @GetMapping("/messages/{senderId}/{recipientId}/count")
  public Long countNewMessage(@PathVariable Long senderId, @PathVariable Long recipientId) {
    return messageService.countNewMessages(senderId, recipientId);
  }
  
  @GetMapping("/messages/{chatId}/count")
  public Long countNewMessage(@PathVariable Long chatId) {
    return messageService.countNewMessages(chatId);
  }
  
  @GetMapping("/messages/{senderId}/{recipientId}")
  public List<Message> findChatMessages(@PathVariable Long senderId, @PathVariable Long recipientId) {
    return messageService.findChatMessages(senderId, recipientId);
  }
  
  @GetMapping("/messages/{chatId}")
  public List<Message> findChatMessages(@PathVariable Long chatId) {
    return messageService.findChatMessages(chatId);
  }
  
}