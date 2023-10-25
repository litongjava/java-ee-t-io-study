package com.litongjava.study.tio.hello.server;

import java.nio.ByteBuffer;

import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.core.TioConfig;
import org.tio.core.exception.TioDecodeException;
import org.tio.core.intf.Packet;
import org.tio.server.intf.TioServerHandler;

import com.litongjava.study.tio.hello.common.DemoPacket;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Ping E Lee
 *
 */
@Slf4j
public class DemoTioServerHandler implements TioServerHandler {

  public Packet decode(ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext) throws TioDecodeException {
    int remaining = buffer.remaining();
    if (remaining < DemoPacket.HEADER_LENGTH) {
      return null;
    }

    byte[] bytes = new byte[limit];
    buffer.get(bytes);

    DemoPacket imPacket = new DemoPacket();
    imPacket.setBody(bytes);
    return imPacket;
  }

  public ByteBuffer encode(Packet packet, TioConfig tioConfig, ChannelContext channelContext) {
    DemoPacket serverPacket = (DemoPacket) packet;
    byte[] body = serverPacket.getBody();
    int bodyLen = 0;
    if (body != null) {
      bodyLen = body.length;
    }
    // bytebuffer的总长度是 = 消息头的长度 + 消息体的长度
    int allLen = bodyLen;
    // 创建一个新的bytebuffer
    ByteBuffer buffer = ByteBuffer.allocate(allLen);
    // 设置字节序
    buffer.order(tioConfig.getByteOrder());
    // 写入消息头----消息头的内容就是消息体的长度
    // 写入消息体
    if (body != null) {
      buffer.put(body);
    }
    return buffer;
  }

  public void handler(Packet packet, ChannelContext channelContext) throws Exception {
    DemoPacket packingPacket = (DemoPacket) packet;
    byte[] body = packingPacket.getBody();
    if (body == null) {
      return;
    }
    String string = new String(body);
    log.info("received:{}",string);
    //响应数据
    String sendMessage = "收到了你的消息，你的消息是:" + string;
    log.info("sendMessage:{}",sendMessage);
    byte[] bytes = sendMessage.getBytes();
    //响应包
    DemoPacket responsePacket = new DemoPacket();
    responsePacket.setBody(bytes);
    //响应消息
    log.info("开始响应");
    Tio.send(channelContext, responsePacket);
    log.info("响应完成");
  }
}
