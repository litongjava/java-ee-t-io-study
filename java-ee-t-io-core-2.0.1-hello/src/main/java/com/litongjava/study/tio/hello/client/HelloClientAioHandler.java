package com.litongjava.study.tio.hello.client;

import java.nio.ByteBuffer;

import org.tio.client.intf.ClientAioHandler;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;

import com.litongjava.study.tio.hello.common.HelloPacket;

import lombok.extern.slf4j.Slf4j;

/**
 * @author create by Ping E Lee on 2022-2-19 13:32:46
 */
@Slf4j
public class HelloClientAioHandler implements ClientAioHandler {
  private static HelloPacket heartbeatPacket = new HelloPacket();

  /**
   * 解码:把接收到的ByteBuffer解码成应用可以识别的业务消息包
   * 消息数据结构:消息头+消息体
   * 消息头结构:4个字节,存储消息体的长度
   * 消息体结构:对象json串的byte[]
   */
  @Override
  public Packet decode(ByteBuffer buffer, ChannelContext channelContext) throws AioDecodeException {
    log.info("decode");
    int readableLength = buffer.limit() - buffer.position();
    //如果收到的数据无法组成业务包,则返回null告诉框架,数据不够
    if (readableLength < HelloPacket.HEADER_LENGTH) {
      return null;
    }
    //读取消息体的长度
    int bodyLength = buffer.getInt();

    //数据不正确,则抛出AioDecodeException异常
    if (bodyLength < 0) {
      String template = "bodyLength:%s is not correct,remote:%s";
      String msg = String.format(template, bodyLength, channelContext.getClientNode());
      throw new AioDecodeException(msg);
    }
    //计算本次需要的数据长度
    int neededLength = HelloPacket.HEADER_LENGTH + bodyLength;
    //收到的数据是否足够组包
    int isDateEnough = readableLength - neededLength;
    //不够消息体长度,剩下的buffer无法组成消息体
    if (isDateEnough < 0) {
      return null;
    }
    //组包成功
    else {
      HelloPacket imPackage = new HelloPacket();
      if (bodyLength > 0) {
        byte[] dst = new byte[bodyLength];
        buffer.get(dst);
        imPackage.setBody(dst);
      }
      return imPackage;
    }
  }

  /**
   * 编码:把业务消息包编码为可以发送的ByteBuffer
   * 消息数据结构:消息头+消息体
   * 消息头结构:4个字节,存储消息体的长度
   * 消息体结构:对象json串的byte[]
   */
  @Override
  public ByteBuffer encode(Packet packet, GroupContext groupContext, ChannelContext channelContext) {
    log.info("encode");
    HelloPacket helloPacket = (HelloPacket) packet;
    byte[] body = helloPacket.getBody();
    int bodyLen = 0;
    if (body != null) {
      bodyLen = body.length;
    }
    //ByteBuffer的总长度是消息体头长度+消息体长度
    int allLen = HelloPacket.HEADER_LENGTH + bodyLen;
    //创建一个新的ByteBuffer
    ByteBuffer buffer = ByteBuffer.allocate(allLen);
    //设置字节序
    buffer.order(groupContext.getByteOrder());
    //消息消息头,消息头的内容就是消息体的长度
    buffer.putInt(bodyLen);
    //消息消息体
    if (body != null) {
      buffer.put(body);
    }
    return buffer;
  }

  /**
   * 处理消息
   */
  @Override
  public void handler(Packet packet, ChannelContext channelContext) throws Exception {
    log.info("handler");
    HelloPacket helloPacket = (HelloPacket) packet;
    byte[] body = helloPacket.getBody();
    if (body != null) {
      String str = new String(body, HelloPacket.CHARSET);
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
    log.info("heartbeatPacket");
    return heartbeatPacket;
  }

}
