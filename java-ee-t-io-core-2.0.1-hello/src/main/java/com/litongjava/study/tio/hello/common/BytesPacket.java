package com.litongjava.study.tio.hello.common;

import org.tio.core.intf.Packet;

/**
 * Created by litonglinux@qq.com on 10/25/2023_11:26 AM
 */
public class BytesPacket extends Packet {
  private byte[] body;

  public byte[] getBody() {
    return body;
  }

  public void setBody(byte[] body) {
    this.body = body;
  }
}
