package com.litongjava.tio.tcp.showcase.client;

import java.nio.ByteBuffer;

import org.tio.client.intf.ClientAioHandler;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;

import com.litongjava.tio.tcp.showcase.common.ShowcasePacket;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShowcaseClientAioHandler implements ClientAioHandler {

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
    ShowcasePacket helloPacket = (ShowcasePacket) packet;
    byte[] body = helloPacket.getBody();
    // ByteBuffer的总长度是消息体长度
    int bodyLength = body.length;
    log.info("encode:{}", bodyLength);

    // 创建一个新的ByteBuffer
    ByteBuffer buffer = ByteBuffer.allocate(bodyLength);
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
    log.info("handler");
    ShowcasePacket helloPacket = (ShowcasePacket) packet;
    byte[] body = helloPacket.getBody();
    if (body != null) {
      String str = new String(body, ShowcasePacket.CHARSET);
      System.out.println("收到的消息是:" + str);
    }
  }

  /**
   * 此方法如果返回null,框架层面则不会发出心跳,如果返回非null,框架层面会定时发送本方法返回的消息包
   *
   * @return
   */
  @Override
  public Packet heartbeatPacket() {
    return null;
  }
}