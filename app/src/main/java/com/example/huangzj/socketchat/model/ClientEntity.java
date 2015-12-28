package com.example.huangzj.socketchat.model;

import com.example.huangzj.socketchat.service.ServiceThread;

import java.net.Socket;

/**
 * Created by huangzj on 2015/10/7.
 * <p/>
 * 客户端连接状态实体实体
 */
public class ClientEntity {

    private String id;

    private Socket socket;


    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }


    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

}
