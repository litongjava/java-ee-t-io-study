package com.litongjava.study.tio.hello.common;

import org.tio.core.intf.Packet;

/**
 * @author create by Ping E Lee on 2022-2-19 13:19:45 
 *
 */
public class HelloPacket extends Packet {

  private static final long serialVersionUID = 172060606924066412L;

  // 消息头的长度
  public static final int HEADER_LENGTH = 4;
  // 编码格式
  public static final String CHARSET="utf-8";

  private byte[] body;
  
  public byte[] getBody() {
    return body;
  }
  
  public void setBody(byte[] body) {
    this.body=body;
  }
}
