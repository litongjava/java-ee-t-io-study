package com.litongjava.ee.tio.http.demo.server;

import java.io.IOException;

import org.tio.server.ServerTioConfig;
import org.tio.server.TioServer;
import org.tio.server.intf.ServerAioHandler;
import org.tio.server.intf.ServerAioListener;

public class HttpServer {
  /**
   * 启动程序入口
   */
  public static void main(String[] args) throws IOException {
    // handler, 包括编码、解码、消息处理
    ServerAioHandler serverHandler = new HttpServerHandler();
    // 事件监听器，可以为null，但建议自己实现该接口，可以参考showcase了解些接口
    ServerAioListener serverListener = new HttpServerListener();
    // 配置对象
    ServerTioConfig tioServerConfig = new ServerTioConfig(serverHandler, serverListener);
    // 设置心跳,-1 取消心跳
    tioServerConfig.setHeartbeatTimeout(-1);
    // TioServer对象
    TioServer tioServer = new TioServer(tioServerConfig);

    // 启动服务
    tioServer.start(null, 6789);
  }
}