package com.lili.study.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class Client {

    public static String SERVER_DEFAULT_IP = "127.0.0.1";
    public static int SERVER_DEFAULT_PORT = 7776;


    public static void main(String[] args) {

        try {
            Charset utf8 = Charset.forName("UTF-8");
            Selector selector = null;

            try {
                InetSocketAddress address = new InetSocketAddress(SERVER_DEFAULT_IP, SERVER_DEFAULT_PORT);
                SocketChannel sc = SocketChannel.open();
                sc.configureBlocking(false);

                selector = Selector.open();

                sc.register(selector, (SelectionKey.OP_READ | SelectionKey.OP_WRITE), new Buffers(256, 256));

                sc.connect(address);

                while (!sc.finishConnect()) {
                    ;
                }

            } catch (IOException e) {
                System.out.println( "connet server fail");
            }

            System.out.println("connet server success");

            try {
                while (!Thread.currentThread().isInterrupted()) {

                    //阻塞等待
                    selector.select();

                    Set<SelectionKey> keySet = selector.selectedKeys();
                    Iterator<SelectionKey> it = keySet.iterator();

                    while (it.hasNext()) {
                        SelectionKey key = it.next();
                        it.remove();

                        Buffers buffers = (Buffers) key.attachment();
                        ByteBuffer readByteBuffer = buffers.getReadByteBuffer();
                        ByteBuffer writeByteBuffer = buffers.getWriteByteBuffer();

                        SocketChannel sc = (SocketChannel) key.channel();


                        if (key.isReadable()) {
                            sc.read(readByteBuffer);
                            readByteBuffer.flip();

                            CharBuffer cb = utf8.decode(readByteBuffer);
                            System.out.println("read server data:");
                            System.out.println(cb.array());
                            readByteBuffer.clear();
                        }

                        if (key.isWritable()) {
                            writeByteBuffer.put("hello server".getBytes("UTF-8"));
                            writeByteBuffer.flip();

                            sc.write(writeByteBuffer);

                            //writeByteBuffer.compact();

                            writeByteBuffer.clear();
                        }

                    }


                    Thread.sleep(1000);
                }
            } catch (IOException e) {
                System.out.println("client close");
            } finally {
                selector.close();
                System.out.println("client selector close");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
