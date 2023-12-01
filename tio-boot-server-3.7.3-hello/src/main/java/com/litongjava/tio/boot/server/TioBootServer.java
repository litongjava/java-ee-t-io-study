package com.litongjava.tio.boot.server;

import java.util.concurrent.ThreadPoolExecutor;

import org.tio.http.common.HttpConfig;
import org.tio.http.server.handler.DefaultHttpRequestHandler;
import org.tio.server.ServerTioConfig;
import org.tio.server.TioServer;
import org.tio.server.intf.ServerAioHandler;
import org.tio.server.intf.ServerAioListener;
import org.tio.utils.Threads;
import org.tio.utils.thread.pool.SynThreadPoolExecutor;
import org.tio.websocket.common.WsTioUuid;
import org.tio.websocket.server.WsServerConfig;

import com.litongjava.tio.boot.server.wshandler.ShowcaseWsMsgHandler;

public class TioBootServer {

  /**
   * 启动程序入口
   * @throws Exception 
   */
  public static void main(String[] args) throws Exception {
    int port = 80;

    // WsServerStarter wsServerStarter = new WsServerStarter(port, );
    WsServerConfig wsServerConfig = new WsServerConfig(port);

    HttpConfig httpConfig = new HttpConfig(null, false);

    DefaultHttpRequestHandler defaultHttpRequestHandler = new DefaultHttpRequestHandler(httpConfig,
        TioBootServer.class);

    // handler, 包括编码、解码、消息处理
    ServerAioHandler serverHandler = new TioBootServerHandler(wsServerConfig, ShowcaseWsMsgHandler.me, httpConfig,
        defaultHttpRequestHandler);
    // 事件监听器，可以为null，但建议自己实现该接口，可以参考showcase了解些接口
    ServerAioListener serverListener = new TioBootServerListener();
    // 配置对象
    ServerTioConfig serverTioConfig = new ServerTioConfig(serverHandler, serverListener);
    // 设置心跳,-1 取消心跳
    serverTioConfig.setHeartbeatTimeout(0);
    WsTioUuid wsTioUuid = new WsTioUuid();
    serverTioConfig.setTioUuid(wsTioUuid);
    serverTioConfig.setReadBufferSize(1024 * 30);
    // TioServer对象
    TioServer tioServer = new TioServer(serverTioConfig);

    // 启动服务
    tioServer.start(null, port);
  }
}