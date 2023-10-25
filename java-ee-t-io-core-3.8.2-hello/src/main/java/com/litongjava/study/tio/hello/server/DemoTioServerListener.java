package com.litongjava.study.tio.hello.server;

import java.nio.ByteBuffer;

import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.core.TioConfig;
import org.tio.core.exception.TioDecodeException;
import org.tio.core.intf.Packet;
import org.tio.server.intf.TioServerHandler;
import org.tio.server.intf.TioServerListener;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Ping E Lee
 */

@Slf4j
public class DemoTioServerListener implements TioServerListener {
  public void onAfterConnected(ChannelContext channelContext, boolean isConnected, boolean isReconnect)
      throws Exception {
  }

  public void onAfterDecoded(ChannelContext channelContext, Packet packet, int packetSize) throws Exception {
  }

  public void onAfterReceivedBytes(ChannelContext channelContext, int receivedBytes) throws Exception {
  }

  public void onAfterSent(ChannelContext channelContext, Packet packet, boolean isSentSuccess) throws Exception {
  }

  public void onAfterHandled(ChannelContext channelContext, Packet packet, long cost) throws Exception {
  }

  /**
   * 连接关闭前触发本方法
   *
   * @param channelContext        the channelcontext
   * @param throwable the throwable 有可能为空
   * @param remark    the remark 有可能为空
   * @param isRemove
   * @throws Exception
   */

  public void onBeforeClose(ChannelContext channelContext, Throwable throwable, String remark, boolean isRemove)
      throws Exception {
    log.info("关闭后清除认证信息");
    Tio.unbindToken(channelContext);
  }

  /**
   * @param channelContext
   * @param interval              已经多久没有收发消息了，单位：毫秒
   * @param heartbeatTimeoutCount 心跳超时次数，第一次超时此值是1，以此类推。此值被保存在：channelContext.stat.heartbeatTimeoutCount
   * @return 返回true，那么服务器则不关闭此连接；返回false，服务器将按心跳超时关闭该连接
   */
  public boolean onHeartbeatTimeout(ChannelContext channelContext, Long interval, int heartbeatTimeoutCount) {
    log.info("心跳超时");
    Tio.unbindToken(channelContext);
    return false;
  }
}
