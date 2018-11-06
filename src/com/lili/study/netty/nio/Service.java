package com.lili.study.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class Service {

    private static int DEFAULT_PORT = 7776;

    public static void main(String[] args) {

        try {

            Charset utf8 = Charset.forName("UTF-8");

            InetSocketAddress address = new InetSocketAddress(DEFAULT_PORT);

            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.bind(address, 100);
            ssc.configureBlocking(false);//设置非阻塞

            Selector selector = Selector.open();
            ssc.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("server start success");


            try {

                while (!Thread.currentThread().isInterrupted()) {

                    int n = selector.select();//阻塞等待
                    if (n == 0) {
                        continue;
                    }

                    Set<SelectionKey> keySet = selector.selectedKeys();
                    Iterator<SelectionKey> it = keySet.iterator();

                    SelectionKey key = null;
                    while (it.hasNext()) {
                        key = it.next();
                        it.remove();

                        try {
                            if (key.isAcceptable()) {
                                SocketChannel sc = ssc.accept();
                                sc.configureBlocking(false);

                                sc.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, new Buffers(256, 256));

                                System.out.println("accept from " + sc.getRemoteAddress());
                            }

                            if (key.isReadable()) {
                                Buffers buffers = (Buffers) key.attachment();
                                ByteBuffer readByteBuffer = buffers.getReadByteBuffer();
                                ByteBuffer writeByteBuffer = buffers.getWriteByteBuffer();

                                SocketChannel sc = (SocketChannel) key.channel();
                                sc.read(readByteBuffer);

                                readByteBuffer.flip();
                                CharBuffer cb = utf8.decode(readByteBuffer);
                                System.out.println("read client data:");
                                System.out.println(cb.array());

                                readByteBuffer.clear();

                                //key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
                                //sc.register(selector, SelectionKey.OP_WRITE, new Buffers(256,256));
                            }

                            if (key.isWritable()) {
                                Buffers buffers = (Buffers) key.attachment();
                                ByteBuffer writeByteBuffer = buffers.getWriteByteBuffer();

                                writeByteBuffer.put("hello client".getBytes("UTF-8"));
                                writeByteBuffer.flip();//flip方法  重新设置position和limit

                                SocketChannel sc = (SocketChannel) key.channel();
                                sc.write(writeByteBuffer);

                                //writeByteBuffer.compact();

                                //取消写的事件
                                //key.interestOps(key.interestOps() & (~SelectionKey.OP_WRITE));
                                writeByteBuffer.clear();

                            }
                        } catch (IOException e) {
                            System.out.println("client error");
                            //若客户端连接出现异常，从Seletcor中移除这个key
                            key.cancel();
                            key.channel().close();
                        }
                    }


                    Thread.sleep(1000);
                }

            } catch (Exception e) {
                System.out.println("server selecotr error");

            } finally {
                System.out.println("server close");
                selector.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
