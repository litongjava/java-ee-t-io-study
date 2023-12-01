package com.litongjava.study.tio.hello.handler;

import java.nio.ByteBuffer;

import org.tio.core.Aio;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.EncodedPacket;
import org.tio.core.intf.Packet;
import org.tio.server.intf.ServerAioHandler;

import com.litongjava.study.tio.hello.common.HelloPacket;
import com.litongjava.study.tio.hello.utils.NioUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Ping E Lee
 */
@Slf4j
public class MessageServerAioHandler implements ServerAioHandler {

  @Override
  public EncodedPacket decode(ByteBuffer buffer, ChannelContext channelContext) throws AioDecodeException {
    log.info("buffer:{}", buffer);
    byte[] bytes = NioUtils.byteBuffer2ByteArray(buffer);
    log.info("bytes:{}", bytes);
    log.info("byte length:{}", bytes.length);
    return new EncodedPacket(bytes);
  }

  @Override
  public ByteBuffer encode(Packet packet, GroupContext groupContext, ChannelContext channelContext) {
    log.info("encode packet:{}", packet);
    EncodedPacket encodedPacket = (EncodedPacket) packet;
    // ByteBuffer的总长度是
    byte[] bytes = encodedPacket.getBytes();
    log.info("bytes:{}", bytes);
    log.info("byte length:{}", bytes.length);
    // 设置字节序

    // 写入消息体
    ByteBuffer buffer = null;
    if (bytes != null) {
      buffer = NioUtils.byteArray2ByteBuffer(bytes);
      buffer.order(groupContext.getByteOrder());
    }
    log.info("返回 buffer:{}", buffer);
    return buffer;
  }

  @Override
  public void handler(Packet packet, ChannelContext channelContext) throws Exception {
    EncodedPacket requestPacket = (EncodedPacket) packet;
    byte[] body = requestPacket.getBytes();
    if (body != null) {
      String str = new String(body);
      log.info("收到消息：{}", str);

      EncodedPacket responsePacket = new EncodedPacket(body);
      Boolean send = Aio.send(channelContext, responsePacket);
      if (send) {
        log.info("相应成功");
      } else {
        log.info("相应失败");
      }

    }
  }

  /**
   * 进行16进制解码
   *
   * @param buffer
   * @return
   */
  private String getStringMessge(ByteBuffer buffer) {

    return null;

  }

}
