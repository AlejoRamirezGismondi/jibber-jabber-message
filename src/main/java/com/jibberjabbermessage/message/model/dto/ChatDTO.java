package com.jibberjabbermessage.message.model.dto;

import com.jibberjabbermessage.message.model.Message;
import lombok.Data;

import java.util.List;

@Data
public class ChatDTO {
  private Long contactId;
  private Message message;
}
