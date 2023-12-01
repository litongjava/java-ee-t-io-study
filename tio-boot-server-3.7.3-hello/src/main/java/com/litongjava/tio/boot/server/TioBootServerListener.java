package com.litongjava.tio.boot.server;

import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.core.intf.Packet;
import org.tio.http.common.HttpConst;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.server.intf.ServerAioListener;

import lombok.extern.slf4j.Slf4j;
import org.tio.websocket.common.WsSessionContext;

@Slf4j
public class TioBootServerListener implements ServerAioListener {
  public void onAfterConnected(ChannelContext channelContext, boolean isConnected, boolean isReconnect) throws Exception {
    WsSessionContext wsSessionContext = new WsSessionContext();
    channelContext.set(wsSessionContext);
    return;
  }

  public void onAfterDecoded(ChannelContext channelContext, Packet packet, int packetSize) throws Exception {
  }

  public void onAfterReceivedBytes(ChannelContext channelContext, int receivedBytes) throws Exception {
  }

  public void onAfterSent(ChannelContext channelContext, Packet packet, boolean isSentSuccess) throws Exception {

    if (packet instanceof HttpResponse) {
      HttpResponse httpResponse = (HttpResponse) packet;
      HttpRequest request = httpResponse.getHttpRequest();
      //		String connection = request.getConnection();

      if (request != null) {
        if (request.httpConfig.compatible1_0) {
          switch (request.requestLine.version) {
            case HttpConst.HttpVersion.V1_0:
              if (!HttpConst.RequestHeaderValue.Connection.keep_alive.equals(request.getConnection())) {
                Tio.remove(channelContext, "http 请求头Connection!=keep-alive：" + request.getRequestLine());
              }
              break;

            default:
              if (HttpConst.RequestHeaderValue.Connection.close.equals(request.getConnection())) {
                Tio.remove(channelContext, "http 请求头Connection=close：" + request.getRequestLine());
              }
              break;
          }
        } else {
          if (HttpConst.RequestHeaderValue.Connection.close.equals(request.getConnection())) {
            Tio.remove(channelContext, "http 请求头Connection=close：" + request.getRequestLine());
          }
        }
      }
    }

  }

  public void onAfterHandled(ChannelContext channelContext, Packet packet, long cost) throws Exception {
  }

  /**
   * 连接关闭前触发本方法
   *
   * @param channelContext the channelcontext
   * @param throwable      the throwable 有可能为空
   * @param remark         the remark 有可能为空
   * @param isRemove
   * @throws Exception
   */

  public void onBeforeClose(ChannelContext channelContext, Throwable throwable, String remark, boolean isRemove) throws Exception {

  }

  /**
   * @param channelContext
   * @param interval              已经多久没有收发消息了，单位：毫秒
   * @param heartbeatTimeoutCount 心跳超时次数，第一次超时此值是1，以此类推。此值被保存在：channelContext.stat.heartbeatTimeoutCount
   * @return 返回true，那么服务器则不关闭此连接；返回false，服务器将按心跳超时关闭该连接
   */
  public boolean onHeartbeatTimeout(ChannelContext channelContext, Long interval, int heartbeatTimeoutCount) {
    return false;
  }
}