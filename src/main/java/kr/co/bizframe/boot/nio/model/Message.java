package kr.co.bizframe.boot.nio.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Message {

  private String deviceId;

  private List<String> senderIds;

  private Map<String, String> header = new HashMap<String, String>();

  private String body;


  @Builder
  public Message(String deviceId, List<String> senderIds, Map<String, String> header, String body) {
    this.deviceId = deviceId;
    this.senderIds = senderIds;
    this.header = header;
    this.body = body;
  }



}
