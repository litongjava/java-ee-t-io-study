package com.litongjava.study.tio.hello.utils;

import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
 * @author Ping E Lee
 *
 */
public class NioUtils {
  /**
   * byte 数组转byteBuffer
   * @param byteArray
   */
  public static ByteBuffer byteArray2ByteBuffer(byte[] byteArray) {

    // 初始化一个和byte长度一样的buffer
    ByteBuffer buffer = ByteBuffer.allocate(byteArray.length);
    // 数组放到buffer中
    buffer.put(byteArray);
    // 重置 limit 和postion 值 否则 buffer 读取数据不对
    buffer.flip();
    return buffer;
  }

  /**
   * byteBuffer 转 byte数组
   * @param buffer
   * @return
   */
  public static byte[] byteBuffer2ByteArray(ByteBuffer buffer) {
    // 重置 limit 和postion 值
    // buffer= buffer.flip();//多次测试,和t-io整合时可以取消掉
    // 获取buffer中有效大小
    int len = buffer.limit() - buffer.position();

    byte[] bytes = new byte[len];

    for (int i = 0; i < bytes.length; i++) {
      bytes[i] = buffer.get();

    }

    return bytes;
  }

  public static void main(String[] args) {

    byte[] a = new byte[] { 1, 2, 4, 5, 6, 7, 8, 9 };

    System.out.println("-----------byte2Byffer转换前----------");
    for (int i = 0; i < a.length; i++) {
      System.out.print(a[i] + ",");

    }
    ByteBuffer buffer = byteArray2ByteBuffer(a);
    System.out.println();
    System.out.println("-----------byte2Byffer转换后----------");
    int len = buffer.limit() - buffer.position();
    for (int i = 0; i < len; i++) {
      System.out.print(buffer.get() + ",");

    }
    System.out.println(buffer);
    System.out.println("\n-----------bytebuffer2ByteArray转换后----------");

    byte[] dest = byteBuffer2ByteArray(buffer);
    for (int i = 0; i < dest.length; i++) {
      System.out.print(dest[i] + ",");

    }

  }

}
