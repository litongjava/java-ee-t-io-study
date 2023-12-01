package com.litongjava.tio.tcp.showcase.common;

import org.tio.core.intf.Packet;

public class ShowcasePacket extends Packet {

  private static final long serialVersionUID = 172060606924066412L;
  // 编码格式
  public static final String CHARSET = "utf-8";

  private byte[] body;

  public byte[] getBody() {
    return body;
  }

  public void setBody(byte[] body) {
    this.body = body;
  }
}