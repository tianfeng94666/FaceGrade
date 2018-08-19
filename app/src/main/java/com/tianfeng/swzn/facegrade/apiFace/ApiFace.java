package com.tianfeng.swzn.facegrade.apiFace;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.baidu.aip.face.AipFace;
import com.tianfeng.swzn.facegrade.bean.MessageBean;
import com.tianfeng.swzn.facegrade.constant.Global;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class ApiFace {


    private ApiFace() {
        if (SingletonHolder.instance != null) {
            throw new IllegalStateException();
        }
    }

    private static class SingletonHolder {
        private static ApiFace instance = new ApiFace();
    }

    public static ApiFace getInstance() {
        return SingletonHolder.instance;
    }

    public void getResult(final byte[] data) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                // 初始化一个AipFace
                AipFace client = new AipFace(Global.APP_ID, Global.API_KEY, Global.SECRET_KEY);

                // 可选：设置网络连接参数
                client.setConnectionTimeoutInMillis(2000);
                client.setSocketTimeoutInMillis(60000);
                // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
                // 也可以直接通过jvm启动参数设置此环境变量
                System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");

                // 调用接口
                String image = Base64.encodeToString(data, Base64.DEFAULT);
                String imageType = "BASE64";
                String result = null;
                // 传入可选参数调用接口
                HashMap<String, String> options = new HashMap<String, String>();
                options.put("face_field", "age,beauty,gender,race,glasses,face_type");
                options.put("max_face_num", "2");
                options.put("face_type", "LIVE");
                JSONObject res = client.detect(image, imageType, options);
                try {

                    result = res.toString(2);
                    EventBus.getDefault().post(new MessageBean(Global.FACE_RESULT, result,data));
                    Log.e("tag", result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }).start();


    }

    /**
     * 将图片转换成Base64编码的字符串
     *
     * @param path
     * @return base64编码的字符串
     */
    public static String imageToBase64(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        InputStream is = null;
        byte[] data = null;
        String result = null;
        try {
            is = new FileInputStream(path);
            //创建一个字符流大小的数组。
            data = new byte[is.available()];
            //写入数组
            is.read(data);
            //用默认的编码格式进行编码
            result = Base64.encodeToString(data, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return result;
    }

}
