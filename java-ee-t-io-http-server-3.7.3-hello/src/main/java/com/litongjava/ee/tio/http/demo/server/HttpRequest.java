package com.litongjava.ee.tio.http.demo.server;

import org.tio.core.intf.Packet;

/**
* socket消息包
*/
@SuppressWarnings("serial")
public class HttpRequest extends Packet {
  private byte[] body;

  public byte[] getBody() {
    return body;
  }

  public void setBody(byte[] body) {
    this.body = body;
  }
}