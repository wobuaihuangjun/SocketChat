package com.example.huangzj.socketchat.model;

/**
 * Created by huangzj on 2015/10/7.
 */
public interface OnClientConnectListener {

    void onClientConnect(String name);

    void onClientDisConnect(String name);
}
