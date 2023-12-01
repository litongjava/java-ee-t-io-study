package com.litongjava.study.java.ee.tio.http.server.boot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.http.common.HttpConfig;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.http.common.RequestLine;
import org.tio.http.common.handler.HttpRequestHandler;
import org.tio.http.server.util.Resps;

public class SimpleHttpRequestHandler implements HttpRequestHandler {
  private HttpConfig httpConfig;
  private Logger log = LoggerFactory.getLogger(this.getClass());

  public SimpleHttpRequestHandler(HttpConfig httpConfig) {
    if (httpConfig == null) {
      throw new RuntimeException("httpConfig can not be null");
    }

    this.httpConfig = httpConfig;

  }

  public HttpConfig getHttpConfig(HttpRequest request) {
    return httpConfig;
  }

  @Override
  public void clearStaticResCache(HttpRequest request) {

  }

  @Override
  public HttpResponse resp404(HttpRequest request, RequestLine requestLine) {
    return null;
  }

  @Override
  public HttpResponse resp500(HttpRequest request, RequestLine requestLine, Throwable throwable) {
    return null;
  }

  @Override
  public HttpResponse handler(HttpRequest request) throws Exception {

    RequestLine requestLine = request.getRequestLine();
    log.info("requestLine:{}", requestLine);
    HttpResponse ret = Resps.txt(request, "hello");
    return ret;
  }
}