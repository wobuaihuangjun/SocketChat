package com.example.huangzj.socketchat.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import android.os.Looper;

import com.example.huangzj.socketchat.common.Constant;
import com.example.huangzj.socketchat.common.DLog;
import com.example.huangzj.socketchat.common.ParserTool;
import com.example.huangzj.socketchat.model.Message;
import com.example.huangzj.socketchat.model.OnMessageReceiveListener;

/**
 * Created by huangzj on 2015/10/6.
 */
public class ClientThread implements Runnable {
    private Socket socket;
    // 定义向UI线程发送消息的listener对象
    OnMessageReceiveListener listener;

    // 该线程处理Socket所对用的输入输出流
    BufferedReader bufferedReader = null;
    PrintWriter pout = null;

    private String serverIp;

    private String localIp;

    public ClientThread(OnMessageReceiveListener listener, String serverIp) {
        this.listener = listener;
        this.serverIp = serverIp;
    }

    public void sendData(Message message) {

        if (socket.isConnected()) {
            if (!socket.isOutputShutdown()) {
                message.setIpAddress(localIp);
                String content = ParserTool.getJson(message);
                try {
                    pout.println(content);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                DLog.d("socket.isOutputShutdown()");
            }
        } else {
            DLog.d("客户端未连接");
        }
    }

    @Override
    public void run() {
        DLog.d("run()");
        socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(serverIp, Constant.PORT), 5000);

            localIp = socket.getLocalAddress().getHostAddress();

            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), Constant.Encode));
            pout = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    socket.getOutputStream(), Constant.Encode)), true);
            // 启动一条子线程来读取服务器相应的数据
            new Thread() {

                @Override
                public void run() {
                    DLog.d("开启读取线程");
                    String content = null;
                    // 不断的读取Socket输入流的内容

                    while (true) {
                        if (socket.isConnected()) {
                            if (!socket.isInputShutdown()) {
                                try {
                                    if ((content = bufferedReader.readLine()) != null) {
                                        // 每当读取到来自服务器的数据之后，发送的消息通知程序
                                        // 界面显示该数据
                                        DLog.i("接收到数据：" + content);
                                        Message message = ParserTool.getMessage(content);

                                        if (listener != null) {
                                            listener.onReceive(message);
                                        }
                                    }
                                } catch (IOException io) {
                                    io.printStackTrace();
                                }
                            } else {
                                DLog.d("!socket.isInputShutdown()");
                            }
                        } else {
                            DLog.d("连接已断开");
                        }
                    }

                }
            }.start();
            // 为当前线程初始化Looper
            Looper.prepare();
            // 启动Looper
            Looper.loop();

        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            if (listener != null) {
                listener.onFail("网络连接超时！");
            }
        } catch (IOException io) {
            io.printStackTrace();
        }

    }
}
