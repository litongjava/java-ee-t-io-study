package com.litongjava.study.java.ee.tio.http.server.boot;

import org.tio.http.common.HttpConfig;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.http.common.RequestLine;
import org.tio.http.common.handler.HttpRequestHandler;
import org.tio.http.server.util.Resps;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleHttpRequestHandler implements HttpRequestHandler {
  private HttpConfig httpConfig;

  public SimpleHttpRequestHandler(HttpConfig httpConfig) {
    if (httpConfig == null) {
      throw new RuntimeException("httpConfig can not be null");
    }

    this.httpConfig = httpConfig;

  }

  public HttpResponse handler(HttpRequest request) throws Exception {
    RequestLine requestLine = request.getRequestLine();
    log.info("requestLine:{}", requestLine);
    HttpResponse ret = Resps.txt(request, "hello");
    return ret;
  }

  public HttpResponse resp404(HttpRequest request, RequestLine requestLine) throws Exception {
    return null;
  }

  public HttpResponse resp500(HttpRequest request, RequestLine requestLine, Throwable throwable) throws Exception {
    return null;
  }

  public HttpConfig getHttpConfig(HttpRequest request) {
    return httpConfig;
  }

  public void clearStaticResCache() {

  }

}
