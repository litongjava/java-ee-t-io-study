package com.litongjava.tio.tcp.showcase.client;

import org.tio.client.AioClient;
import org.tio.client.ClientChannelContext;
import org.tio.client.ClientGroupContext;
import org.tio.client.ReconnConf;
import org.tio.client.intf.ClientAioHandler;
import org.tio.client.intf.ClientAioListener;
import org.tio.core.Aio;
import org.tio.core.Node;

import com.litongjava.tio.tcp.showcase.common.ShowcasePacket;

public class ShowcaseClientStarter {

  // handler,包含编解码,消息处理
  public static ClientAioHandler clientAioHandler = new ShowcaseClientAioHandler();

  // 事件监听器,可以为null,但是建议自己实现该接口,可以参考showcase
  public static ClientAioListener clientAioListener = null;

  // 断链后自动连接,不自动连接设置为null
  private static ReconnConf reconnConf = new ReconnConf(50000L);
  // 共用上下文对象
  private static ClientGroupContext clientGroupContext = new ClientGroupContext(clientAioHandler, clientAioListener,
      reconnConf);

  // 发送消息客户端
  public static AioClient aioClient;
  // 客户端通道上下文,连接服务器后获得
  public static ClientChannelContext clientChannelContext;

  /**
   * 启动程序
   */
  public static void main(String[] args) throws Exception {
    // 服务器节点
    Node serverNode = new Node("127.0.0.1", 5678);
    // 设置心跳时间
    clientGroupContext.setHeartbeatTimeout(0);
    // 初始化client
    aioClient = new AioClient(clientGroupContext);
    // 连接服务器
    clientChannelContext = aioClient.connect(serverNode);
    // 发送消息
    send();
  }

  private static void send() {
    ShowcasePacket helloPacket = new ShowcasePacket();
    helloPacket.setBody("Hello World".getBytes());
    Aio.send(clientChannelContext, helloPacket);
  }
}