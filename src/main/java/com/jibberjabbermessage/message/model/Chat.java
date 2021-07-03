package com.jibberjabbermessage.message.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Chat {
  @Id
  @GeneratedValue
  private Long id;
  private Long senderId, recipientId;
  @OneToMany(mappedBy = "chat", fetch = FetchType.EAGER, orphanRemoval = true)
  private List<Message> messages = new ArrayList<>();
  
  public void addMessage(Message message) {
    messages.add(message);
  }
}
