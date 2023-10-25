package org.tio.study.helloworld.server;

import java.io.IOException;

import org.tio.server.ServerTioConfig;
import org.tio.server.TioServer;
import org.tio.server.intf.ServerAioHandler;
import org.tio.server.intf.ServerAioListener;
import org.tio.study.helloworld.common.Const;

/**
 *
 * @author tanyaowu
 * 2017年4月4日 下午12:22:58
 */
public class HelloServerStarter {
  // handler, 包括编码、解码、消息处理
  public static ServerAioHandler aioHandler = new HelloServerAioHandler();
  // 事件监听器，可以为null，但建议自己实现该接口，可以参考showcase了解些接口
  public static ServerAioListener aioListener = null;
  // 一组连接共用的上下文对象
  public static ServerTioConfig serverTioConfig = new ServerTioConfig("hello-tio-server", aioHandler, aioListener);
  // tioServer对象
  public static TioServer tioServer = new TioServer(serverTioConfig);
  // 有时候需要绑定ip，不需要则null
  public static String serverIp = null;
  // 监听的端口
  public static int serverPort = Const.PORT;

  /**
   * 启动程序入口
   */
  public static void main(String[] args) throws IOException {
    serverTioConfig.setHeartbeatTimeout(org.tio.study.helloworld.common.Const.TIMEOUT);
    tioServer.start(serverIp, serverPort);
  }
}