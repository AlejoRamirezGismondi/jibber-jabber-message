package com.jibberjabbermessage.message.model;

import lombok.Data;

@Data
public class ChatNotification {
  private Long id, senderId;
  private String senderName;
  
  public ChatNotification(Long id, Long senderId, String senderName) {
    this.id = id;
    this.senderId = senderId;
    this.senderName = senderName;
  }
}
