package com.tianfeng.swzn.facegrade.bean;

public class MessageBean {
    private int type;
    private String result;
    private byte[] data;

    public MessageBean(int type, String result) {
        this.type = type;
        this.result = result;
    }

    public MessageBean(int type, String result, byte[] data) {
        this.type = type;
        this.result = result;
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
