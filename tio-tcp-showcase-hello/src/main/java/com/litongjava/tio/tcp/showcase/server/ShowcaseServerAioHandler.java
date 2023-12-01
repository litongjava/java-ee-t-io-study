package com.litongjava.tio.tcp.showcase.server;

import java.nio.ByteBuffer;

import org.tio.core.Aio;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;
import org.tio.server.intf.ServerAioHandler;

import com.litongjava.tio.tcp.showcase.common.ShowcasePacket;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by litonglinux@qq.com on 2022/2/19_15:26
 */
@Slf4j
public class ShowcaseServerAioHandler implements ServerAioHandler {
  /**
   * 解码:把接收到的ByteBuffer解码成应用可以识别的业务消息包
   */
  @Override
  public Packet decode(ByteBuffer buffer, ChannelContext channelContext) throws AioDecodeException {
    log.info("buffer:{}", buffer);
    // 转换前准备ByteBuffer
    int length = buffer.remaining();
    // 获取由ByteBuffer支持的字节数组
    byte[] bytes = new byte[length];
    buffer.get(bytes);
    // 封装为ShowcasePacket
    ShowcasePacket imPackage = new ShowcasePacket();
    imPackage.setBody(bytes);
    return imPackage;
  }

  /**
   * 编码:把业务消息包编码为可以发送的ByteBuffer
   */
  @Override
  public ByteBuffer encode(Packet packet, GroupContext groupContext, ChannelContext channelContext) {
    log.info("packet:{}", packet);
    ShowcasePacket helloPacket = (ShowcasePacket) packet;
    byte[] body = helloPacket.getBody();

    // ByteBuffer的总长度是消息体长度
    int length = body.length;
    // 创建一个新的ByteBuffer
    ByteBuffer buffer = ByteBuffer.allocate(length);
    // 设置字节序
    buffer.order(groupContext.getByteOrder());
    // 消息消息体
    buffer.put(body);
    return buffer;
  }

  /**
   * 处理消息
   */
  @Override
  public void handler(Packet packet, ChannelContext channelContext) throws Exception {
    ShowcasePacket requestPacket = (ShowcasePacket) packet;
    byte[] body = requestPacket.getBody();
    if (body != null) {
      String str = new String(body, ShowcasePacket.CHARSET);
      log.info("received:{}", str);
      ShowcasePacket responsePacket = new ShowcasePacket();
      responsePacket.setBody(("received:" + str).getBytes(ShowcasePacket.CHARSET));
      Aio.send(channelContext, responsePacket);
    }
    return;

  }
}