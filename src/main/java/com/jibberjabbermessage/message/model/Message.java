package com.jibberjabbermessage.message.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Message {
  @Id
  @GeneratedValue
  private Long id;
  private Long senderId, recipientId;
  private String content;
  private LocalDate timestamp;
  private MessageStatus status;
  @ManyToOne
  @JsonIgnore
  private Chat chat;
}

