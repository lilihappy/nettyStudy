package com.lili.study.netty.bio.simple;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client{

    public static String SERVER_DEFAULT_IP = "127.0.0.1";
    public static int SERVER_DEFAULT_PORT = 7777;

    private void start(String ip, int port) {
        Socket socket = null;
        BufferedWriter bw = null;
        BufferedReader br = null;
        try {
            socket = new Socket(ip, port);
            System.out.println("======client与server连接成功=====");

            //获取控制台输入信息
            Scanner scanner = new Scanner(System.in);
            String sysStr = scanner.nextLine();

            //给server发信息
            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bw.write(sysStr);
            bw.flush();
            socket.shutdownOutput();

            //获取server信息
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String serverStr = br.readLine();
            System.out.println("接收server信息：" + serverStr);
            socket.shutdownInput();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                br = null;
            }
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bw = null;
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                socket = null;
            }
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.start(SERVER_DEFAULT_IP, SERVER_DEFAULT_PORT);
    }
}
