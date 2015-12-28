package com.example.huangzj.socketchat.model;

import com.example.huangzj.socketchat.common.ParserTool;

/**
 * Created by huangzj on 2015/10/7.
 */
public interface OnMessageReceiveListener {

    void onReceive(Message message);

    void onFail(String error);
}
