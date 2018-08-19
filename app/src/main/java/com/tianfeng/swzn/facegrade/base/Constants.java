package com.tianfeng.swzn.facegrade.base;

import com.tianfeng.swzn.facegrade.jsonBean.FaceResult;

public class Constants {
    public static final String BASE_URL = "http://appapi1.fanerweb.com/api/aproxy/";

    public static String getDescript(int i, FaceResult.ResultBean.FaceListBean faceListBean) {
        StringBuilder stringBuilder = new StringBuilder();
        String fender = faceListBean.getGender().getType();
        String race = faceListBean.getRace().getType();
        String desript = null;
        if (fender.equals("male")) {
            stringBuilder.append("嗨，帅哥");
            int index = (int) (Math.random() * maleBeaty.length);
            desript = maleBeaty[index];
        } else {
            stringBuilder.append("嗨，美女");
            int index = (int) (Math.random() * maleBeaty.length);
            desript = femaleBeaty[index];
        }
//        stringBuilder.append(",你是" + getRace(race));
        stringBuilder.append(",年龄" + faceListBean.getAge());
        if(faceListBean.getGlasses().getType().equals("common")){

            stringBuilder.append(",戴着眼镜");
        }else if(faceListBean.getGlasses().getType().equals("sun")){
            stringBuilder.append(",戴着墨镜");
        }
        stringBuilder.append(",一句话形容你的颜值："+desript);
        return stringBuilder.toString();
    }

    public static String getRace(String st) {
        String race = null;
        switch (st) {
            case "yellow":
                race = "黄种人";
                break;
            case "white":
                race = "白种人";
                break;
            case "black":
                race = "黑种人";
                break;
            case "arabs":
                race = "阿拉伯人";
                break;
        }
        return race;
    }

    public static String[] maleBeaty = {
            "你每天早上是不是都是被自己帅醒的"
            , "表白的人多到干扰你生活了吧"
            , "你出去跑步，别人是不是都搂紧了自己的女朋友"
            , "你要记住，不要微笑，因为杀伤力太强"
            , "你帅的消息，千万不要走漏风声"
            , "不要随便偷人心哦"
            , "颜值高有多累，我想你是懂得"
            , "出门还要担心别人撞墙的你，很辛苦吧"
            , "快来呀，吴彦祖在这里"
            , "在这个看脸的时代，你已经接近巅峰了"
            , "才华你有没有，我不知道，反正颜值你是有了"
            , "帅的我无话可说了"
            , "帅到没朋友"
            , "不要走，让我在多看你一会"
            , "说！你是不是整容了"
            , "能带我回家吗？"
            , "你就是江湖人称第一帅吗"};
    public static String[] femaleBeaty = {
            "你每天早上一照镜子是不是被自己美呆了"
            , "表白的人多到干扰你生活了吧"
            , "你走在街上，经常看到男生撞柱子吧"
            , "你都是拍照不p图，直接发朋友圈，一直素颜出门"
            , "是不是好多人偷拍你"
            , "不要随便偷人心哦"
            , "颜值高有多累，我想你是懂得"
            , "出门还要担心别人撞墙的你，很辛苦吧"
            , "你出去跑步，别人是不是都搂紧了自己的男朋友"
            , "你的证件照又被人拿去做头像了"
            , "在这个看脸的时代，你已经接近巅峰了"
            , "回眸一笑百媚生"
            , "才华你有没有，我不知道，反正颜值你是有了"
            , "美的我无话可说了"
            , "说！你是不是整容了"
            , "能带我回家吗？"
            , "你就是江湖人称第一美吗"};
}
