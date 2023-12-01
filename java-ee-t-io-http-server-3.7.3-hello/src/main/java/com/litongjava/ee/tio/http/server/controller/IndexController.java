package com.litongjava.ee.tio.http.server.controller;

import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.http.server.annotation.RequestPath;
import org.tio.http.server.util.Resps;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestPath(value = "/")
public class IndexController {
  @RequestPath(value = "hello")
  public HttpResponse respText(HttpRequest request) throws Exception {
    log.info("txt");
    HttpResponse ret = Resps.txt(request, "hello t-io-http-server 2");
    return ret;
  }


  @RequestPath("classloader")
  public HttpResponse respClassLoader(HttpRequest request) {
    ClassLoader classLoader = this.getClass().getClassLoader();
    HttpResponse txt = Resps.txt(request, classLoader.toString());
    return txt;
  }

  @RequestPath("version")
  public HttpResponse version(HttpRequest request) {
    String javaVersion = System.getProperty("java.version");
    HttpResponse txt = Resps.txt(request, javaVersion);
    return txt;
  }

  @RequestPath("jvm")
  public HttpResponse jvm(HttpRequest request) {
    long jvmTotal = Runtime.getRuntime().totalMemory();
    long jvmFree = Runtime.getRuntime().freeMemory();
    long jvmUsed = jvmTotal - jvmFree;
    String text = jvmUsed / 1024 / 1024 + "MB";
    HttpResponse txt = Resps.txt(request, text);
    return txt;
  }
}
