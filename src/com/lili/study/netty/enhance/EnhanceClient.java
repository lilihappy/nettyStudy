package com.lili.study.netty.enhance;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class EnhanceClient {

    public static String SERVER_DEFAULT_IP = "127.0.0.1";
    public static int SERVER_DEFAULT_PORT = 7777;

    private void start(String ip, int port) {
        try {

            Socket socket = new Socket(ip, port);
            System.out.println("======client与server连接成功=====");

            DataInputStream is = new DataInputStream(socket.getInputStream());
            DataOutputStream os = new DataOutputStream(socket.getOutputStream());
            //获取控制台输入信息
            Scanner scanner = new Scanner(System.in);

            while (true) {
                String sysStr = scanner.nextLine();
                os.writeUTF(sysStr);
                os.flush();

                String serverStr = is.readUTF();
                System.out.println("接收server信息：" + serverStr);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        EnhanceClient client = new EnhanceClient();
        client.start(SERVER_DEFAULT_IP, SERVER_DEFAULT_PORT);
    }
}
