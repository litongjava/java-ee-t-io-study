package com.litongjava.ee.tio.http.demo.server;

import java.nio.ByteBuffer;

import org.tio.core.ChannelContext;
import org.tio.core.TioConfig;
import org.tio.core.exception.TioDecodeException;
import org.tio.core.intf.Packet;
import org.tio.server.intf.ServerAioHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpServerHandler implements ServerAioHandler {

  public Packet decode(ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext)
      throws TioDecodeException {
    log.info("buffer:{}", buffer);
    // 获取由ByteBuffer支持的字节数组
    byte[] bytes = new byte[readableLength];
    buffer.get(bytes);
    // 封装为ShowcasePacket
    HttpRequest imPackage = new HttpRequest();
    imPackage.setBody(bytes);
    return imPackage;
  }

  public ByteBuffer encode(Packet packet, TioConfig tioConfig, ChannelContext channelContext) {
    HttpRequest helloPacket = (HttpRequest) packet;
    byte[] body = helloPacket.getBody();
    // ByteBuffer的总长度是消息体长度
    int bodyLength = body.length;
    log.info("encode:{}", bodyLength);

    // 创建一个新的ByteBuffer
    ByteBuffer buffer = ByteBuffer.allocate(bodyLength);
    // 设置字节序
    buffer.order(tioConfig.getByteOrder());
    // 消息消息体
    buffer.put(body);
    return buffer;
  }

  public void handler(Packet packet, ChannelContext channelContext) throws Exception {
    HttpRequest packingPacket = (HttpRequest) packet;
    byte[] body = packingPacket.getBody();
    if (body == null) {
      return;
    }
    String string = new String(body);
    log.info("received:{}", string);
  }
}