package com.lili.study.netty.enhance;

import com.lili.study.netty.simple.Server;
import com.lili.study.netty.simple.ThreadSocket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class EnhanceServer {

    public static int DEFAULT_PORT = 7777;


    public void start(int port) {

        try {

            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("======启动Server...");

            Socket socket = serverSocket.accept();
            System.out.println("=====连接建立成功=====");

            DataInputStream is = new DataInputStream(socket.getInputStream());
            DataOutputStream os = new DataOutputStream(socket.getOutputStream());
            //获取控制台输入信息
            Scanner scanner = new Scanner(System.in);

            while (true) {
                String clientStr = is.readUTF();
                System.out.println("接收Client信息：" + clientStr);

                String sysStr = scanner.nextLine();
                os.writeUTF(sysStr);
                os.flush();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        EnhanceServer server = new EnhanceServer();
        server.start(DEFAULT_PORT);
    }
}
