package com.tianfeng.swzn.facegrade.http;

import com.google.gson.JsonObject;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;


public interface APIService {
    /**
     * 登录
     */
    @GET("userLoginDo?QxVersion=2.2.3&")
    Observable<JsonObject> login(@Query("userName") String name, @Query("password") String pwd);
}
