package com.jibberjabbermessage.message.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

@Data
@Entity
public class Message {
  @Id
  @GeneratedValue
  private Long id;
  private Long senderId, recipientId;
  private String content;
  private Date timestamp;
  private MessageStatus status;
  @ManyToOne
  private Chat chat;
}

