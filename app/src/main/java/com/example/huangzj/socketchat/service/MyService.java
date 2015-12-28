package com.example.huangzj.socketchat.service;

import android.util.Log;

import com.example.huangzj.socketchat.common.Constant;
import com.example.huangzj.socketchat.common.DLog;
import com.example.huangzj.socketchat.common.ParserTool;
import com.example.huangzj.socketchat.model.ClientEntity;
import com.example.huangzj.socketchat.model.Message;
import com.example.huangzj.socketchat.model.OnClientConnectListener;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by huangzj on 2015/10/6.
 * <p/>
 * 服务器显示客户端连接状态
 */
public class MyService {
    // 定义保存所有的客户端
    public static List<ClientEntity> socketList = new ArrayList<>();

//    public static Map<String, ClientEntity> clientMap = new HashMap<>();

    public static String serverIp = "";

    public static void build(OnClientConnectListener listener) throws IOException {
        Log.i("MyService", "build");
        ServerSocket server = new ServerSocket(Constant.PORT);
        serverIp = server.getInetAddress().getHostAddress();
        while (true) {
            Socket mSocket = server.accept();

            //每当客户端连接之后启动一条ServerThread线程为该客户端服务
            ServiceThread thread = new ServiceThread(mSocket, listener);
            new Thread(thread).start();

            sendReceipt(mSocket);
            listener.onClientConnect(mSocket.getInetAddress().getHostAddress());

            String id = UUID.randomUUID().toString().replaceAll("-", "");
            String ipAddress = mSocket.getInetAddress().getHostAddress();

            removeDuplicateClient(ipAddress, listener);

            ClientEntity entity = new ClientEntity();
            entity.setId(id);
            entity.setSocket(mSocket);
            socketList.add(entity);
            DLog.i("a client is connected，client size is ：" + socketList.size());
        }
    }

    /**
     * 去除重复ip的客户端
     *
     * @param ipAddress
     */
    public static void removeDuplicateClient(String ipAddress, OnClientConnectListener listener) {

        if (ipAddress == null) {
            return;
        }

        ClientEntity dupClient = null;

        for (ClientEntity client : socketList) {
            if (ipAddress.equals(client.getSocket().getInetAddress().getHostAddress())) {
                dupClient = client;
                break;
            }
        }

        if (dupClient != null) {
            if (listener != null) {
                listener.onClientDisConnect(ipAddress);
            }
            socketList.remove(dupClient);
        }

    }

    public static void sendReceipt(Socket mSocket) throws IOException {
        DLog.i("mSocket.getInetAddress().getHostAddress() ：" + mSocket.getInetAddress().getHostAddress()
                + ", mSocket.getLocalAddress() ：" + mSocket.getLocalAddress()
                + ", mSocket.getLocalSocketAddress() ：" + mSocket.getLocalSocketAddress()
                + ", mSocket.getInetAddress().getHostName() ：" + mSocket.getInetAddress().getHostName());

        Message message = new Message();
        message.setData("连接成功");
        message.setType(Constant.Type_Receipt_Message);
        message.setIpAddress(serverIp);
        String content = ParserTool.getJson(message);

        PrintWriter pout = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(mSocket.getOutputStream(), Constant.Encode)), true);
        pout.println(content);
    }

}
