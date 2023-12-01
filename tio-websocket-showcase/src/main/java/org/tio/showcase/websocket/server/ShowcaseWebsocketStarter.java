package org.tio.showcase.websocket.server;

import java.io.IOException;

import org.tio.server.ServerTioConfig;
import org.tio.showcase.http.init.HttpServerInit;
import org.tio.utils.jfinal.P;
import org.tio.websocket.server.WsServerStarter;

/**
 * @author tanyaowu
 * 2017年6月28日 下午5:34:04
 */
public class ShowcaseWebsocketStarter {

	private WsServerStarter wsServerStarter;
	private ServerTioConfig serverTioConfig;

	/**
	 *
	 * @author tanyaowu
	 */
	public ShowcaseWebsocketStarter(int port, ShowcaseWsMsgHandler wsMsgHandler) throws Exception {
		wsServerStarter = new WsServerStarter(port, wsMsgHandler);

		serverTioConfig = wsServerStarter.getServerTioConfig();
		serverTioConfig.setName(ShowcaseServerConfig.PROTOCOL_NAME);
		serverTioConfig.setServerAioListener(ShowcaseServerAioListener.me);

		//设置ip监控
		serverTioConfig.setIpStatListener(ShowcaseIpStatListener.me);
		//设置ip统计时间段
		serverTioConfig.ipStats.addDurations(ShowcaseServerConfig.IpStatDuration.IPSTAT_DURATIONS);
		
		//设置心跳超时时间
		serverTioConfig.setHeartbeatTimeout(ShowcaseServerConfig.HEARTBEAT_TIMEOUT);
		
		if (P.getInt("ws.use.ssl", 1) == 1) {
			//如果你希望通过wss来访问，就加上下面的代码吧，不过首先你得有SSL证书（证书必须和域名相匹配，否则可能访问不了ssl）
//			String keyStoreFile = "classpath:config/ssl/keystore.jks";
//			String trustStoreFile = "classpath:config/ssl/keystore.jks";
//			String keyStorePwd = "214323428310224";
			
			
			String keyStoreFile = P.get("ssl.keystore", null);
			String trustStoreFile = P.get("ssl.truststore", null);
			String keyStorePwd = P.get("ssl.pwd", null);
			serverTioConfig.useSsl(keyStoreFile, trustStoreFile, keyStorePwd);
		}
	}

	/**
	 * @param args
	 * @author tanyaowu
	 * @throws IOException
	 */
	public static void start() throws Exception {
		ShowcaseWebsocketStarter appStarter = new ShowcaseWebsocketStarter(ShowcaseServerConfig.SERVER_PORT, ShowcaseWsMsgHandler.me);
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
		//启动http server，这个步骤不是必须的，但是为了用页面演示websocket，所以先启动http
		P.use("app.properties");
		
		
		if (P.getInt("start.http", 1) == 1) {
			HttpServerInit.init();
		}
		
		//启动websocket server
		start();
	}

}
