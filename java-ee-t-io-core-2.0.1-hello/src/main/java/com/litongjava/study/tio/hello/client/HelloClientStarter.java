package com.litongjava.study.tio.hello.client;

import com.litongjava.study.tio.hello.common.Const;
import com.litongjava.study.tio.hello.common.HelloPacket;
import org.tio.client.AioClient;
import org.tio.client.ClientChannelContext;
import org.tio.client.ClientGroupContext;
import org.tio.client.ReconnConf;
import org.tio.client.intf.ClientAioHandler;
import org.tio.client.intf.ClientAioListener;
import org.tio.core.Aio;
import org.tio.core.Node;

/**
 * Created by litonglinux@qq.com on 2022/2/19_14:26
 */
public class HelloClientStarter {
  //服务器节点
  public static final Node serverNode = new Node(Const.SERVER, Const.PORT);

  //handler,包含编解码,消息处理
  public static ClientAioHandler clientAioHandler = new HelloClientAioHandler();

  //事件监听器,可以为null,但是建议自己实现该接口,可以参考showcase
  public static ClientAioListener clientAioListener = null;

  //断链后自动连接,不自动连接设置为null
  private static ReconnConf reconnConf = new ReconnConf(50000L);
  //共用上下文对象
  private static ClientGroupContext clientGroupContext = new ClientGroupContext(clientAioHandler, clientAioListener, reconnConf);

  //发送消息客户端
  public static AioClient aioClient;
  //客户端通道上下文,连接服务器后获得
  public static ClientChannelContext clientChannelContext;

  /**
   * 启动程序
   */
  public static void main(String[] args) throws Exception {
    //设置心跳时间
    clientGroupContext.setHeartbeatTimeout(Const.TIMEOUT);
    //初始化client
    aioClient = new AioClient(clientGroupContext);
    //连接服务器
    clientChannelContext = aioClient.connect(serverNode);
    //发送消息
    send();
  }

  private static void send() {
    HelloPacket helloPacket = new HelloPacket();
    helloPacket.setBody("Hello World".getBytes());
    Aio.send(clientChannelContext, helloPacket);
  }
}
