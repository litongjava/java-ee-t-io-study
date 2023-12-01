package com.litongjava.tio.websocket.demo;

import java.io.IOException;

import org.tio.server.ServerTioConfig;
import org.tio.websocket.server.WsServerStarter;

public class ShowcaseWebsocketStarter {

  private WsServerStarter wsServerStarter;
  private ServerTioConfig serverTioConfig;

  public ShowcaseWebsocketStarter(int port, ShowcaseWsMsgHandler wsMsgHandler) throws Exception {
    wsServerStarter = new WsServerStarter(port, wsMsgHandler);

    serverTioConfig = wsServerStarter.getServerTioConfig();
    serverTioConfig.setName(ShowcaseServerConfig.PROTOCOL_NAME);
    serverTioConfig.setServerAioListener(ShowcaseServerAioListener.me);

    // 设置ip监控
    serverTioConfig.setIpStatListener(ShowcaseIpStatListener.me);
    // 设置ip统计时间段
    serverTioConfig.ipStats.addDurations(ShowcaseServerConfig.IpStatDuration.IPSTAT_DURATIONS);

    // 设置心跳超时时间
    serverTioConfig.setHeartbeatTimeout(ShowcaseServerConfig.HEARTBEAT_TIMEOUT);

    // 如果你希望通过wss来访问，就加上下面的代码吧，不过首先你得有SSL证书（证书必须和域名相匹配，否则可能访问不了ssl）
    // String keyStoreFile = "classpath:config/ssl/keystore.jks";
    // String trustStoreFile = "classpath:config/ssl/keystore.jks";
    // String keyStorePwd = "214323428310224";

    // String keyStoreFile = P.get("ssl.keystore", null);
    // String trustStoreFile = P.get("ssl.truststore", null);
    // String keyStorePwd = P.get("ssl.pwd", null);
    // serverTioConfig.useSsl(keyStoreFile, trustStoreFile, keyStorePwd);
  }

  /**
   * @param args
   * @throws IOException
   */
  public static void start() throws Exception {
    ShowcaseWebsocketStarter appStarter = new ShowcaseWebsocketStarter(ShowcaseServerConfig.SERVER_PORT,
        ShowcaseWsMsgHandler.me);
    appStarter.wsServerStarter.start();
  }

  /**
   * @return the serverTioConfig
   */
  public ServerTioConfig getServerTioConfig() {
    return serverTioConfig;
  }

  public WsServerStarter getWsServerStarter() {
    return wsServerStarter;
  }

  public static void main(String[] args) throws Exception {
    // 启动websocket server
    start();
  }

}
