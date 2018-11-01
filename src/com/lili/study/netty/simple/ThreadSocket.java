package com.lili.study.netty.simple;

import java.io.*;
import java.net.Socket;

public class ThreadSocket extends Thread {

    Socket socket;

    public ThreadSocket(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        BufferedReader br = null;
        BufferedReader brSys = null;
        BufferedWriter bw = null;

        try {
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String clientStr = br.readLine();
            System.out.println("接收Client信息：" + clientStr);
            socket.shutdownInput();

            brSys = new BufferedReader(new InputStreamReader(System.in));
            String sysStr = brSys.readLine();

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
            if (brSys != null) {
                try {
                    brSys.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                brSys = null;
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
