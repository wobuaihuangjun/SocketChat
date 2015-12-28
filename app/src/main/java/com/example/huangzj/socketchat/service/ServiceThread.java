package com.example.huangzj.socketchat.service;


import android.util.Log;

import com.example.huangzj.socketchat.common.Constant;
import com.example.huangzj.socketchat.common.DLog;
import com.example.huangzj.socketchat.model.ClientEntity;
import com.example.huangzj.socketchat.model.OnClientConnectListener;
import com.example.huangzj.socketchat.service.MyService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by huangzj on 2015/10/6.
 */
public class ServiceThread implements Runnable {

    // 定义当前线程处理的Socket
    private Socket s = null;
    // 定义向UI线程发送消息的listener对象
    OnClientConnectListener listener;
    // 该线程所处理的Socket所对应的输入流
    BufferedReader br = null;

    private String id;

    /**
     * 退出标识
     */
    private volatile boolean exit = false;

    public void setExit(boolean exit) {
        this.exit = exit;
    }

    private boolean isExit() {
        return exit;
    }

    public ServiceThread(Socket s, OnClientConnectListener listener) {
        this.s = s;
        this.listener = listener;
        try {
            br = new BufferedReader(new InputStreamReader(s.getInputStream(), Constant.Encode));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendData(String str) {
        for (ClientEntity client : MyService.socketList) {
            Socket mSocket = client.getSocket();
            if (mSocket.isConnected()) {
                if (!mSocket.isOutputShutdown()) {
                    try {
                        PrintWriter pout = new PrintWriter(new BufferedWriter(
                                new OutputStreamWriter(mSocket.getOutputStream(), Constant.Encode)), true);
                        pout.println(str);

                    } catch (IOException e) {
                        MyService.socketList.remove(s);
                        e.printStackTrace();
                    }
                } else {
                    DLog.e("socket.isOutputShutdown()");
                    closeSocket(mSocket);
                }
            } else {
                DLog.e("客户端已断开连接：" + mSocket.getInetAddress().getHostAddress());
                closeSocket(mSocket);
            }
        }
    }

    private void closeSocket(Socket mSocket) {
        try {
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MyService.socketList.remove(mSocket);
        listener.onClientDisConnect(mSocket.getInetAddress().getHostAddress());

    }

    @Override
    public void run() {
        DLog.i("客户端响应线程已启动");
        String content;
        //采用循环不断的从Socket中读取客户端发送过来的数据

        while (!isExit()) {
            if ((content = readFromClient()) != null) {
                //遍历socketList中的每个Socket
                //将读取到的内容每个向Socket发送一次
                Log.e("ServiceThread", "分发数据：" + content);
                sendData(content);
            }
        }
        DLog.e("客户端响应线程已停止");
    }

    // 定义读取客户端的信息
    public String readFromClient() {
        try {
            return br.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
