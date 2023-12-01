package com.litongjava.ee.tio.http.server.boot;

import java.io.IOException;

import org.tio.http.common.HttpConfig;
import org.tio.http.common.handler.HttpRequestHandler;
import org.tio.http.server.HttpServerStarter;
import org.tio.http.server.handler.DefaultHttpRequestHandler;
import org.tio.server.ServerTioConfig;
import org.tio.utils.jfinal.P;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Ping E Lee
 */
@Slf4j
public class TioApplication {
  public static HttpConfig httpConfig;

  public static HttpRequestHandler requestHandler;

  public static HttpServerStarter httpServerStarter;

  public static ServerTioConfig serverTioConfig;

  public static void run( Class<?> sourceClass,String[] args) {
    // 启动端口
    int port = P.getInt("http.port");
    // html/css/js等的根目录，支持classpath:，也支持绝对路径
    String pageRoot = P.get("http.page");
    // maxLiveTimeOfStaticRes
    String page404 = P.get("http.404");
    String page500 = P.get("http.500");
    Integer maxLiveTimeOfStaticRes = P.getInt("http.maxLiveTimeOfStaticRes");

    // httpConfig
    httpConfig = new HttpConfig(port, null, null, null);
    try {
      httpConfig.setPageRoot(pageRoot);
    } catch (IOException e) {
      e.printStackTrace();
    }
    httpConfig.setMaxLiveTimeOfStaticRes(maxLiveTimeOfStaticRes);
    httpConfig.setPage404(page404);
    httpConfig.setPage500(page500);
    httpConfig.setUseSession(false);
    httpConfig.setCheckHost(false);

    // 第二个参数也可以是数组,自动考试扫描handler的路径
    try {
      requestHandler = new DefaultHttpRequestHandler(httpConfig, sourceClass);
    } catch (Exception e) {
      e.printStackTrace();
    }

    // httpServerStarter
    httpServerStarter = new HttpServerStarter(httpConfig, requestHandler);
    serverTioConfig = httpServerStarter.getServerTioConfig();
    // 启动http服务器
    try {
      httpServerStarter.start();
    } catch (IOException e) {
      e.printStackTrace();
    }
    log.info("port:{}", port);
  }

  public static void stop() {
    log.info("stop server");
    try {
      httpServerStarter.stop();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  public static void restart(String[] args, Class<?> sourceClass) {
    run(sourceClass,args);
    stop();
  }
  public static boolean isRunning() {
    return httpServerStarter.getTioServer().isWaitingStop();
//    return true;
  }
}
