package com.jibberjabbermessage.message.controller;

import com.jibberjabbermessage.message.model.Chat;
import com.jibberjabbermessage.message.model.ChatNotification;
import com.jibberjabbermessage.message.model.Message;
import com.jibberjabbermessage.message.model.dto.ChatDTO;
import com.jibberjabbermessage.message.model.dto.MessageDTO;
import com.jibberjabbermessage.message.service.ChatService;
import com.jibberjabbermessage.message.service.MessageService;
import com.jibberjabbermessage.message.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Controller
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
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
  @SendTo("/queue/messages")
  public ChatDTO processMessage(@Payload MessageDTO dto) {
    Message message = messageService.toMessage(dto);
    String token = dto.getToken();
    final Long userId = userService.getUserId(token);
    if (!message.getSenderId().equals(userId)) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    chatService.createIfNotExists(message);
    Message saved = messageService.save(message);
    chatService.addMessage(saved);
    Optional<Chat> wholeChat = chatService.findBySenderIdAndRecipientId(message.getSenderId(), message.getRecipientId());
    List<Message> messageList = wholeChat.get().getMessages();
    ChatDTO chat = new ChatDTO();
    chat.setMessages(messageList);
    chat.setContactId(message.getRecipientId());
    return chat;
  }

  @GetMapping("/messages/{senderId}/{recipientId}/count")
  public @ResponseBody
  Long countNewMessage(@PathVariable Long senderId, @PathVariable Long recipientId) {
    return messageService.countNewMessages(senderId, recipientId);
  }

  @GetMapping("/messages/{chatId}/count")
  public @ResponseBody
  Long countNewMessage(@PathVariable Long chatId) {
    return messageService.countNewMessages(chatId);
  }

  @GetMapping("/messages/{senderId}/{recipientId}")
  public @ResponseBody
  List<Message> findChatMessages(@PathVariable Long senderId, @PathVariable Long recipientId) {
    return messageService.findChatMessages(senderId, recipientId);
  }

  @GetMapping("/messages/{chatId}")
  public List<Message> findChatMessages(@PathVariable Long chatId) {
    return messageService.findChatMessages(chatId);
  }

}
