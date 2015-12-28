package com.example.huangzj.socketchat.common;

import com.example.huangzj.socketchat.model.Message;
import com.google.gson.Gson;

/**
 * Created by huangzj on 2015/10/7.
 * <p/>
 * 数据解析工具
 */
public class ParserTool {

    public static Message getMessage(String msgStr) {
        Gson gson = new Gson();
        return gson.fromJson(msgStr, Message.class);

    }

    public static String getJson(Message message) {
        Gson gson = new Gson();
        return gson.toJson(message);
    }
}
