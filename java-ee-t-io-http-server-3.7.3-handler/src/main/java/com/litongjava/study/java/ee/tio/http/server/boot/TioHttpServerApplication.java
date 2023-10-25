package com.litongjava.study.java.ee.tio.http.server.boot;

import org.tio.http.common.HttpConfig;
import org.tio.http.common.handler.HttpRequestHandler;
import org.tio.http.server.HttpServerStarter;
import org.tio.server.ServerTioConfig;

/**
 * @author Ping E Lee
 */
public class TioHttpServerApplication {
  public static HttpConfig httpConfig;

  public static HttpRequestHandler requestHandler;

  public static HttpServerStarter httpServerStarter;

  public static ServerTioConfig serverTioConfig;

  public static void run(Class<?> sourceClass, int port) {

    // httpConfig
    httpConfig = new HttpConfig(port, null, null, null);

    try {
      // requestHandler
      requestHandler = new SimpleHttpRequestHandler(httpConfig);
      // httpServerStarter
      httpServerStarter = new HttpServerStarter(httpConfig, requestHandler);
      // serverTioConfig
      serverTioConfig = httpServerStarter.getServerTioConfig();
      httpServerStarter.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    TioHttpServerApplication.run(TioHttpServerApplication.class, 80);
  }
}