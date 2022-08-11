package com.litongjava.study.tio.websocket.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.tio.websocket.client.WebSocket;
import org.tio.websocket.client.WsClient;
import org.tio.websocket.common.WsPacket;

import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * @author create by Ping E Lee on 2022年2月20日 上午10:11:40 
 *
 */

public class Snippet {
  public static void main(String[] args) throws Exception {
    Map<Integer, Boolean> sent = new ConcurrentHashMap<>();
    int total = 1000;
    Subject<Object> complete = PublishSubject.create().toSerialized();
    
    complete.buffer(total).subscribe(x -> {
      Boolean all = sent.values().stream().reduce(true, (p, c) -> p && c);
      if (all) {
        System.out.println("All sent success! ");
      }
    });
//    
//    WsClientConfig config=new WsClientConfig(e -> System.out.println("opened"), e -> {
//      WsPacket data = e.data;
//      int i = Integer.parseInt(data.getWsBodyText());
//      sent.put(i, true);
//      System.out.println("recv: " + i);
//      complete.onNext(i);
//    }, e -> System.out.printf("on close: %d, %s, %s\n", e.code, e.reason, e.wasClean),
//        e -> System.out.println(String.format("on error: %s", e.msg)), Throwable::printStackTrace);
    
    WsClient wsClient=WsClient.create("wss://echo.websocket.org/?encoding=text");
    
    WebSocket ws = wsClient.connect();
    for (int i = 0; i < total; i++) {
      ws.send("" + i);
      sent.put(i, false);
      System.out.println("sent: " + i);
      System.out.println("SpringBootoy ");
    }
  }
}
