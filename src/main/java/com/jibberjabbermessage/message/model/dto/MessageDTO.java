package com.jibberjabbermessage.message.model.dto;

import lombok.Data;

@Data
public class MessageDTO {
  private Long senderId, recipientId;
  private String content;
  private String token;
}


