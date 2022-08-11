package top.ppnt.java.ee.tio.http.server;

import java.text.DecimalFormat;

import org.tio.utils.jfinal.P;

import com.litongjava.hotswap.debug.Diagnostic;
import com.litongjava.hotswap.kit.HotSwapUtils;
import com.litongjava.hotswap.server.RestartServer;
import com.litongjava.hotswap.watcher.HotSwapWatcher;

import lombok.extern.slf4j.Slf4j;
import top.ppnt.java.ee.tio.http.server.boot.TioApplication;

@Slf4j
public class HelloApp {
  protected static volatile HotSwapWatcher hotSwapWatcher;
  
  public static void main(String[] args){
    long start = System.currentTimeMillis();
    // 加载配置文件
    P.use("app.properties");
    Boolean isDev = P.getBoolean("dev.mode");
    if (isDev) {
      runDev(HelloApp.class, args);
    } else {
      //初始化服务器并启动服务器
      TioApplication.run(HelloApp.class,args);
    }
    long end = System.currentTimeMillis();
    System.out.println("启动完成,共使用了:"+(end-start)+"ms");
  }

  private static void runDev(Class<HelloApp> primarySource, String[] args) {
    // 获取自定义的classLoalder
    ClassLoader hotSwapClassLoader = HotSwapUtils.getClassLoader();
    log.info("hotSwapClassLoader:{}", hotSwapClassLoader);

    // 第一次启动不需要使用自定义的类加载器,使用默认的类加载器即可
    Thread.currentThread().setContextClassLoader(hotSwapClassLoader);
    TioApplication.run(primarySource, args);

    // 需要在spring启动之前启动hotswapWatcher,否则springboot重启之后,hotswapWatcher会也关闭 测试不需要
    // 在spring-boot启动之后再启动hotSwapWatcher
    if (hotSwapWatcher == null) {
      // 开启热加载调试模式
      Diagnostic.setDebug(true);
      log.info("start hotSwapWatcher");
      hotSwapWatcher = new HotSwapWatcher(new RestartServer() {
        protected DecimalFormat decimalFormat = new DecimalFormat("#.#");
        @Override
        public void restart() {
          System.err.println("loading");
          long start = System.currentTimeMillis();
          // 关闭
          TioApplication.stop();
          // 获取启动类和启动参数
          // 获取一个新的ClassLoader
          ClassLoader hotSwapClassLoader = HotSwapUtils.newClassLoader();
          if (Diagnostic.isDebug()) {
            log.info("new classLoader:{}", hotSwapClassLoader);
          }

          // 在启动新的应用之前必须设置上下文加载器
          Thread.currentThread().setContextClassLoader(hotSwapClassLoader);

          // 启动应用
          TioApplication.run(HelloApp.class,args);

          System.err.println("Loading complete in " + getTimeSpent(start) + " seconds (^_^)\n");
        }
        
        @Override
        public boolean isStarted() {
//          boolean running = TioApplication.isRunning();
//          log.info("isRunning:{}",running);
          return true;
        }
        
        protected String getTimeSpent(long startTime) {
          float timeSpent = (System.currentTimeMillis() - startTime) / 1000F;
          return decimalFormat.format(timeSpent);
        }
      });
      hotSwapWatcher.start();
    }
    
  }

}
