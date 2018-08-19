package com.tianfeng.swzn.facegrade.base;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import com.tianfeng.swzn.facegrade.drag.component.AppComponent;
import com.tianfeng.swzn.facegrade.drag.component.DaggerAppComponent;
import com.tianfeng.swzn.facegrade.drag.module.AppModule;
import com.tianfeng.swzn.facegrade.utils.SpUtils;


public class BaseApp extends Application {
    AppComponent mAppComponent;
    private static BaseApp mBaseApplication = null;
    private static Looper mMainThreadLooper = null;
    private static Handler mMainThreadHandler = null;
    private static int mMainThreadId;
    private static Thread mMainThread = null;
    public  static SpUtils spUtils;
    @Override
    public void onCreate() {
        super.onCreate();
        BaseApp.mBaseApplication = this;
        BaseApp.mMainThreadLooper = getMainLooper();
        BaseApp.mMainThreadHandler = new Handler();
        BaseApp.mMainThreadId = android.os.Process.myTid();
        BaseApp.mMainThread = Thread.currentThread();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
        spUtils = SpUtils.getInstace(this);

        mAppComponent= DaggerAppComponent.builder().appModule(new AppModule(getBaseContext(),Constants.BASE_URL)).build();
    }
    public AppComponent getAppCommponent(){
        return mAppComponent;
    }

    public AppComponent getmAppComponent() {
        return mAppComponent;
    }

    public void setmAppComponent(AppComponent mAppComponent) {
        this.mAppComponent = mAppComponent;
    }

    public static BaseApp getmBaseApplication() {
        return mBaseApplication;
    }

    public static void setmBaseApplication(BaseApp mBaseApplication) {
        BaseApp.mBaseApplication = mBaseApplication;
    }

    public static Looper getmMainThreadLooper() {
        return mMainThreadLooper;
    }

    public static void setmMainThreadLooper(Looper mMainThreadLooper) {
        BaseApp.mMainThreadLooper = mMainThreadLooper;
    }

    public static Handler getmMainThreadHandler() {
        return mMainThreadHandler;
    }

    public static void setmMainThreadHandler(Handler mMainThreadHandler) {
        BaseApp.mMainThreadHandler = mMainThreadHandler;
    }

    public static int getmMainThreadId() {
        return mMainThreadId;
    }

    public static void setmMainThreadId(int mMainThreadId) {
        BaseApp.mMainThreadId = mMainThreadId;
    }

    public static Thread getmMainThread() {
        return mMainThread;
    }

    public static void setmMainThread(Thread mMainThread) {
        BaseApp.mMainThread = mMainThread;
    }

    public static SpUtils getSpUtils() {
        return spUtils;
    }

    public static void setSpUtils(SpUtils spUtils) {
        BaseApp.spUtils = spUtils;
    }
}
