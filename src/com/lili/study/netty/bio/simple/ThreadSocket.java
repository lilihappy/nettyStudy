package com.lili.study.netty.bio.simple;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ThreadSocket extends Thread {

    Socket socket;

    public ThreadSocket(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        BufferedReader br = null;
        BufferedWriter bw = null;

        try {
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String clientStr = br.readLine();
            System.out.println("接收Client信息：" + clientStr);
            socket.shutdownInput();

            Scanner scanner = new Scanner(System.in);
            String sysStr = scanner.nextLine();

            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bw.write(sysStr);
            bw.flush();
            socket.shutdownOutput();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bw = null;
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                br = null;
            }
        }
    }
}
