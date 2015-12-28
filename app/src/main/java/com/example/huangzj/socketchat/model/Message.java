package com.example.huangzj.socketchat.model;

/**
 * Created by huangzj on 2015/10/7.
 * 数据发送协议
 */
public class Message {

    /**
     * 数据实体
     */
    private String data;

    /**
     * 数据说明
     */
    private String describe;

    /**
     * 额外数据
     */
    private String extra;

    /**
     * 消息类型
     */
    private int type;

    /**
     * 消息发起者别称
     */
    private String name;

    /**
     * 消息发起者的ip地址
     */
    private String ipAddress;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public String getDescribe() {
        return describe;
    }

    public String getExtra() {
        return extra;
    }

    public int getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Message{" +
                "data='" + data + '\'' +
                ", describe='" + describe + '\'' +
                ", extra='" + extra + '\'' +
                ", type=" + type +
                '}';
    }
}
