package com.lili.study.netty.bio.simple;

import java.net.ServerSocket;
import java.net.Socket;

public class Server{

    public static int DEFAULT_PORT = 7777;

    public void start(int port) {

        try {

            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("======启动Server...");

            while(true) {
                Socket socket = serverSocket.accept();
                System.out.println("=====连接建立成功=====");

                new ThreadSocket(socket).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start(DEFAULT_PORT);
    }
}
