package com.jibberjabbermessage.message.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserService {
  private final RestTemplate restTemplate;
  @Value("${microservice.user.base-url}")
  private String url;
  public UserService(RestTemplateBuilder rtb) {
    this.restTemplate = rtb.build();
  }
  
  
  public Long getUserId(String token) {
    HttpEntity http = tokenToHttp(token);
    final ResponseEntity<String> exchange = restTemplate.exchange(url + "/id", HttpMethod.GET, http, String.class);
    return Long.parseLong(exchange.getBody());
  }
  
  public String getUserName(String token, Long senderId) {
    HttpEntity http = tokenToHttp(token);
    final ResponseEntity<String> exchange = restTemplate.exchange(url + "/userName", HttpMethod.GET, http, String.class);
    return exchange.getBody();
  }
  
  private HttpEntity tokenToHttp(String token) {
    final HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", token);
    HttpEntity a = new HttpEntity(headers);
    return a;
  }
}
