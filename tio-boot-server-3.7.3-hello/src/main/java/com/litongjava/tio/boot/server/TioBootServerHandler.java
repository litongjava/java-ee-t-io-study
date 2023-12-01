package com.litongjava.tio.boot.server;

import java.nio.ByteBuffer;

import org.tio.core.ChannelContext;
import org.tio.core.TioConfig;
import org.tio.core.exception.TioDecodeException;
import org.tio.core.intf.Packet;
import org.tio.http.common.HttpConfig;
import org.tio.http.common.handler.HttpRequestHandler;
import org.tio.http.server.HttpServerAioHandler;
import org.tio.server.intf.ServerAioHandler;
import org.tio.websocket.common.WsResponse;
import org.tio.websocket.server.WsServerAioHandler;
import org.tio.websocket.server.WsServerConfig;
import org.tio.websocket.server.handler.IWsMsgHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TioBootServerHandler implements ServerAioHandler {

  protected WsServerConfig wsServerConfig;
  private WsServerAioHandler wsServerAioHandler;
  protected HttpConfig httpConfig;
  private HttpServerAioHandler httpServerAioHandler;

  /**
   * @param wsServerConfig
   * @param wsMsgHandler
   */
  public TioBootServerHandler(WsServerConfig wsServerConfig, IWsMsgHandler wsMsgHandler, HttpConfig httpConfig,
      HttpRequestHandler requestHandler) {
    this.wsServerConfig = wsServerConfig;
    this.wsServerAioHandler = new WsServerAioHandler(wsServerConfig, wsMsgHandler);

    this.httpConfig = httpConfig;
    this.httpServerAioHandler = new HttpServerAioHandler(httpConfig, requestHandler);

  }

  public Packet decode(ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext)
      throws TioDecodeException {
    log.info("buffer:{}", buffer);
    // 获取由ByteBuffer支持的字节数组
    byte[] bytes = new byte[readableLength];
    buffer.get(bytes);
    // 封装为ShowcasePacket
    TcpPacket tcpPacket = new TcpPacket();
    buffer.position(0);

    if (tcpPacket.isWebScoket()) {
      // 如果是websocket请求
      return wsServerAioHandler.decode(buffer, limit, position, readableLength, channelContext);
    } else {
      return httpServerAioHandler.decode(buffer, limit, position, readableLength, channelContext);
    }
  }

  public ByteBuffer encode(Packet packet, TioConfig tioConfig, ChannelContext channelContext) {
    if (packet instanceof WsResponse) {
      return wsServerAioHandler.encode(packet, tioConfig, channelContext);
    } else {
      return httpServerAioHandler.encode(packet, tioConfig, channelContext);
    }
  }

  public void handler(Packet packet, ChannelContext channelContext) throws Exception {
    wsServerAioHandler.handler(packet, channelContext);
//    if (packet instanceof WsResponse) {
//
//    } else {
//      httpServerAioHandler.handler(packet, channelContext);
//    }
  }
}