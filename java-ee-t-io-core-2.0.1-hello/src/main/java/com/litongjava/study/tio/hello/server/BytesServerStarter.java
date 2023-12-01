package com.litongjava.study.tio.hello.server;

import com.litongjava.study.tio.hello.common.Const;
import com.litongjava.study.tio.hello.handler.BytesHandler;
import org.tio.server.AioServer;
import org.tio.server.ServerGroupContext;
import org.tio.server.intf.ServerAioHandler;
import org.tio.server.intf.ServerAioListener;

import java.io.IOException;

public class BytesServerStarter {
  // handler, 包括编码、解码、消息处理
  public static ServerAioHandler aioHandler = new BytesHandler();


//  public static ServerAioHandler aioHandler = new MessageServerAioHandler();

  // 事件监听器，可以为null，但建议自己实现该接口，可以参考showcase了解些接口
  public static ServerAioListener aioListener = null;

  // 一组连接共用的上下文对象
  public static ServerGroupContext serverGroupContext = new ServerGroupContext(aioHandler, aioListener);

  public static AioServer aioServer;

  /**
   * 启动程序入口
   */
  public static void main(String[] args) {
    String serverIp = null;
    int serverPort = 5678;
    run(serverIp, serverPort);
  }

  private static void run(String serverIp, int serverPort) {
    // 设置心跳
    serverGroupContext.setHeartbeatTimeout(Const.TIMEOUT);
    // aioServer对象
    aioServer = new AioServer(serverGroupContext);
    // 启动服务
    try {
      aioServer.start(serverIp, serverPort);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}