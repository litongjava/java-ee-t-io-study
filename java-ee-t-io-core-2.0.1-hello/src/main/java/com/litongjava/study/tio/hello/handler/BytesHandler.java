package com.litongjava.study.tio.hello.handler;

import com.litongjava.study.tio.hello.common.BytesPacket;
import lombok.extern.slf4j.Slf4j;
import org.tio.core.Aio;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;
import org.tio.server.intf.ServerAioHandler;

import java.nio.ByteBuffer;

/**
 * Created by litonglinux@qq.com on 10/25/2023_11:27 AM
 */
@Slf4j
public class BytesHandler implements ServerAioHandler {

  @Override
  public BytesPacket decode(ByteBuffer buffer, ChannelContext channelContext) throws AioDecodeException {
    byte[] bytes = new byte[buffer.limit()];
    buffer.get(bytes);

    BytesPacket imPacket = new BytesPacket();
    imPacket.setBody(bytes);
    return imPacket;
  }

  @Override
  public ByteBuffer encode(Packet packet, GroupContext groupContext, ChannelContext channelContext) {
    BytesPacket serverPacket = (BytesPacket) packet;
    byte[] body = serverPacket.getBody();
    int bodyLen = 0;
    if (body != null) {
      bodyLen = body.length;
    }
    // bytebuffer的总长度是 = 消息头的长度 + 消息体的长度,这里只有消息体
    int allLen = bodyLen;
    // 创建一个新的bytebuffer
    ByteBuffer buffer = ByteBuffer.allocate(allLen);
    // 设置字节序
    buffer.order(groupContext.getByteOrder());
    // 写入消息头----消息头的内容就是消息体的长度
    if (body != null) {
      buffer.put(body);
    }
    return buffer;

  }

  @Override
  public void handler(Packet packet, ChannelContext channelContext) throws Exception {
    BytesPacket packingPacket = (BytesPacket) packet;
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
    BytesPacket responsePacket = new BytesPacket();
    responsePacket.setBody(bytes);
    //响应消息
    log.info("开始响应");
    Aio.send(channelContext, responsePacket);
    log.info("响应完成");
  }
}
