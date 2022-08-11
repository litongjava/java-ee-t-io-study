package top.ppnt.java.ee.tio.http.server.controller;

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
    HttpResponse ret = Resps.txt(request, "hello t-io-http-server");
    return ret;
  }
  
 
  @RequestPath("classloader")
  public HttpResponse respClassLoader(HttpRequest request) {
    ClassLoader classLoader = this.getClass().getClassLoader();
    HttpResponse txt = Resps.txt(request,classLoader.toString());
    return txt;
  }
}
