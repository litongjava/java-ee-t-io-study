package com.litongjava.tio.boot.server;

import org.tio.core.intf.Packet;

@SuppressWarnings("serial")
public class TcpPacket extends Packet {

  private byte[] body;

  public byte[] getBody() {
    return body;
  }

  public void setBody(byte[] body) {
    this.body = body;
  }

  public boolean isWebScoket() {
    return true;
  }
}
