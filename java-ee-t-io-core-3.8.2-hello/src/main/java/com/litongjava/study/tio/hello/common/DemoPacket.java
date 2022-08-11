package com.litongjava.study.tio.hello.common;

import org.tio.core.intf.Packet;

/**
* socket消息包
*
* @author
*/
@SuppressWarnings("serial")
public class DemoPacket extends Packet {
  /**
   * 消息头的长度
   */
  public static final int HEADER_LENGTH = 4;
  private byte[] body;

  public byte[] getBody() {
    return body;
  }

  public void setBody(byte[] body) {
    this.body = body;
  }
}
