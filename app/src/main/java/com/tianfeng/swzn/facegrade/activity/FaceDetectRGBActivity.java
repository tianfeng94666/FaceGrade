package com.tianfeng.swzn.facegrade.activity;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.media.FaceDetector;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.recker.flybanner.FlyBanner;
import com.tianfeng.swzn.facegrade.R;
import com.tianfeng.swzn.facegrade.apiFace.ApiFace;
import com.tianfeng.swzn.facegrade.base.BaseActivity;
import com.tianfeng.swzn.facegrade.base.Constants;
import com.tianfeng.swzn.facegrade.bean.FaceBean;
import com.tianfeng.swzn.facegrade.bean.MessageBean;
import com.tianfeng.swzn.facegrade.jsonBean.FaceResult;
import com.tianfeng.swzn.facegrade.utils.BitmapUtils;
import com.tianfeng.swzn.facegrade.utils.CameraErrorCallback;
import com.tianfeng.swzn.facegrade.utils.ImageUtils;
import com.tianfeng.swzn.facegrade.utils.SpUtils;
import com.tianfeng.swzn.facegrade.utils.Util;
import com.tianfeng.swzn.facegrade.viewUtils.FaceOverlayView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Nguyen on 5/20/2016.
 */

/**
 * FACE DETECT EVERY FRAME WIL CONVERT TO RGB BITMAP SO THIS HAS LOWER PERFORMANCE THAN GRAY BITMAP
 * COMPARE FPS (DETECT FRAME PER SECOND) OF 2 METHODs FOR MORE DETAIL
 */


public final class FaceDetectRGBActivity extends BaseActivity implements SurfaceHolder.Callback, Camera.PreviewCallback {

    @BindView(R.id.surfaceview)
    SurfaceView surfaceview;
    @BindView(R.id.topLayout)
    RelativeLayout topLayout;
    @BindView(R.id.iv_top)
    ImageView ivTop;
    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.iv_wai)
    ImageView ivWai;
    @BindView(R.id.iv_nei)
    ImageView ivNei;
    @BindView(R.id.iv_decorate_top)
    ImageView ivDecorateTop;
    @BindView(R.id.tv_mark)
    TextView tvMark;
    @BindView(R.id.tv_descript)
    TextView tvDescript;
    @BindView(R.id.rl_result)
    RelativeLayout rlResult;
    @BindView(R.id.flybanner)
    FlyBanner flybanner;
    // Number of Cameras in device.
    private int numberOfCameras;

    public static final String TAG = FaceDetectRGBActivity.class.getSimpleName();

    private Camera mCamera;
    private int cameraId = 1;

    // Let's keep track of the display rotation and orientation also:
    private int mDisplayRotation;
    private int mDisplayOrientation;

    private int previewWidth;
    private int previewHeight;

    // The surface view for the camera data


    // Draw rectangles and other fancy stuff:
//    private FaceOverlayView mFaceView;

    // Log all errors:
    private final CameraErrorCallback mErrorCallback = new CameraErrorCallback();


    private boolean isThreadWorking = false;
    private static final int MAX_FACE = 1;
    private Handler handler;
    private FaceDetectThread detectThread = null;
    private int prevSettingWidth;
    private int prevSettingHeight;
    private FaceDetector fdet;

    private FaceBean faces[];
    private FaceBean faces_previous[];
    private int Id = SpUtils.getInstace(this).getInt("Id", 0);

    private String BUNDLE_CAMERA_ID = "camera";
    private HashMap<Integer, Integer> facesCount = new HashMap<>();//五帧存一个

    String[] permissions = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    List<String> mPermissionList = new ArrayList<>();
    private ApiFace apiFace;
    private RotateAnimation rotateAnimation;
    private ObjectAnimator anim;
    private Handler uiHandler;
    private final static int PERMISSION_CODE = 22;
    private static final int RLRESULT_CHANGE = 2;
    private int saveImageNum = 17;//保存图片计数器
    private final int SAVEIMAGEMAX = 17;//单个faceID保存图片最大图片数
    private int rotate;//图片旋转角度
    private boolean isStart;
    //==============================================================================================
    // Activity Methods
    //==============================================================================================

    private void getPermission() {

        /**
         * 判断哪些权限未授予
         */
        mPermissionList.clear();
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }

/**
 * 判断是否为空
 */
        if (mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了
//            delayEntryPage();
        } else {//请求权限方法
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE:
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        //判断是否勾选禁止后不再询问

                        boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(FaceDetectRGBActivity.this, permissions[i]);
                        if (showRequestPermission) {//
                            Toast.makeText(FaceDetectRGBActivity.this, "请开启对应权限，程序无法正常运行", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
//                            getPermission();
                        }
                    }
                }
                recreate();
                break;
            default:
                break;
        }
    }


    private void initView(Bundle icicle) {
        apiFace = ApiFace.getInstance();
        EventBus.getDefault().register(this);

        // Now create the OverlayView:
//        mFaceView = new FaceOverlayView(this);
//        addContentView(mFaceView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        // Create and Start the OrientationListener:


        handler = new Handler();
        faces = new FaceBean[MAX_FACE];
        faces_previous = new FaceBean[MAX_FACE];
        for (int i = 0; i < MAX_FACE; i++) {
            faces[i] = new FaceBean();
            faces_previous[i] = new FaceBean();
        }

        if (icicle != null) {
            cameraId = icicle.getInt(BUNDLE_CAMERA_ID, 1);
        }
        uiHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case RLRESULT_CHANGE:
                        startRlDismiss();
                        break;
                }
            }
        };

        List<Integer> images = new ArrayList<>();
        images.add(R.mipmap.pic1);
        images.add(R.mipmap.pic2);
        images.add(R.mipmap.pic3);
        flybanner.setImages(images);
    }

    private void startRlDismiss() {
        AlphaAnimation alphaAnimation1 = new AlphaAnimation(1, 0);
        alphaAnimation1.setDuration(300);
        alphaAnimation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rlResult.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        rlResult.startAnimation(alphaAnimation1);


    }

    private void startAnimation() {
        rotateAnimation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        LinearInterpolator lin = new LinearInterpolator();
        rotateAnimation.setInterpolator(lin);
        rotateAnimation.setDuration(5000);//设置动画持续周期
        rotateAnimation.setRepeatCount(-1);//设置重复次数
        rotateAnimation.setFillAfter(true);//动画执行完后是否停留在执行完的状态
//        rotateAnimation.setStartOffset(10);//执行前的等待时间
        ivWai.setAnimation(rotateAnimation);


        anim = ObjectAnimator.ofFloat(ivNei, "rotation", 0f, -360f);
        anim.setDuration(10000);
        anim.setInterpolator(lin);
        anim.setRepeatCount(-1);
        anim.start();

    }

    /**
     * 接收百度那边人脸检测的结果
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageBean event) {

        JsonObject jsonObject = new Gson().fromJson(event.getResult(), JsonObject.class);
        String error = jsonObject.get("error_code").getAsString();
        if (error.equals("0")) {
            FaceResult result = new Gson().fromJson(event.getResult(), FaceResult.class);
            for (int i = 0; i < result.getResult().getFace_list().size(); i++) {
                double beauty = result.getResult().getFace_list().get(i).getBeauty();
                int age = result.getResult().getFace_list().get(i).getAge();
                faces[i].setBeauty(beauty);
                faces[i].setAge(age);
                startShowResult((int) (Math.sqrt(beauty / 100 * 0.7 + 0.3) * 100), result.getResult().getFace_list().get(i));

            }

        }

    }

    private void startShowResult(int i, FaceResult.ResultBean.FaceListBean faceListBean) {
        if (!isStart) {

            tvMark.setText(i + "");
            tvDescript.setText(Constants.getDescript(i, faceListBean));

            ScaleAnimation scaleAnimation = new ScaleAnimation(0f, 1.1f, 0f, 1.1f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setDuration(500);
            AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
            alphaAnimation.setDuration(400);

            AnimationSet animationSet = new AnimationSet(false);
            animationSet.addAnimation(scaleAnimation);
            animationSet.addAnimation(alphaAnimation);
            rlResult.startAnimation(animationSet);
            animationSet.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    rlResult.setVisibility(View.VISIBLE);
                    isStart = true;

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    isStart = false;

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });


            uiHandler.removeMessages(RLRESULT_CHANGE);
            uiHandler.sendEmptyMessageDelayed(RLRESULT_CHANGE, 7000); // 5秒后执行runnable 的run方法

        }


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Check for the camera permission before accessing the camera.  If the
        // permission is not granted yet, request permission.
        startCamera();
    }

    private void startCamera() {
        SurfaceHolder holder = surfaceview.getHolder();
        holder.addCallback(this);
        holder.setFormat(ImageFormat.NV21);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_CAMERA_ID, cameraId);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {


        //Find the total number of cameras available
        numberOfCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                if (cameraId == 0) cameraId = i;
            }
        }

        mCamera = Camera.open(cameraId);

        Camera.getCameraInfo(cameraId, cameraInfo);
//        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
//            mFaceView.setFront(true);
//        }

        try {
            mCamera.setPreviewDisplay(surfaceview.getHolder());
        } catch (Exception e) {
            Log.e(TAG, "Could not preview the image.", e);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        // We have no surface, return immediately:
        if (surfaceHolder.getSurface() == null) {
            return;
        }
        // Try to stop the current preview:
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // Ignore...
        }

        configureCamera(width, height);
        setDisplayOrientation();
        setErrorCallback();

        // Create media.FaceDetector
        float aspect = (float) previewHeight / (float) previewWidth;
        fdet = new FaceDetector(prevSettingWidth, (int) (prevSettingWidth * aspect), MAX_FACE);


        // Everything is configured! Finally start the camera preview again:
        startPreview();
    }

    private void setErrorCallback() {
        mCamera.setErrorCallback(mErrorCallback);
    }

    private void setDisplayOrientation() {
        // Now set the display orientation:
        mDisplayRotation = Util.getDisplayRotation(FaceDetectRGBActivity.this);
        mDisplayOrientation = Util.getDisplayOrientation(mDisplayRotation, cameraId);
        //公司摄像头是个倒的
        mDisplayOrientation = 270;
        mCamera.setDisplayOrientation(mDisplayOrientation);

//        if (mFaceView != null) {
//            mFaceView.setDisplayOrientation(mDisplayOrientation);
//        }
    }

    private void configureCamera(int width, int height) {
        Camera.Parameters parameters = mCamera.getParameters();
        // Set the PreviewSize and AutoFocus:
        setOptimalPreviewSize(parameters, width, height);
        setAutoFocus(parameters);
        // And set the parameters:
        mCamera.setParameters(parameters);
    }

    private void setOptimalPreviewSize(Camera.Parameters cameraParameters, int width, int height) {
        List<Camera.Size> previewSizes = cameraParameters.getSupportedPreviewSizes();
        float targetRatio = (float) width / height;
        Camera.Size previewSize = Util.getOptimalPreviewSize(this, previewSizes, targetRatio);
        previewWidth = previewSize.width;
        previewHeight = previewSize.height;

        Log.e(TAG, "previewWidth" + previewWidth);
        Log.e(TAG, "previewHeight" + previewHeight);

        /**
         * Calculate size to scale full frame bitmap to smaller bitmap
         * Detect face in scaled bitmap have high performance than full bitmap.
         * The smaller image size -> detect faster, but distance to detect face shorter,
         * so calculate the size follow your purpose
         */
        if (previewWidth / 4 > 320) {
            prevSettingWidth = 800;
            prevSettingHeight = 480;
        } else if (previewWidth / 4 > 240) {
            prevSettingWidth = 240;
            prevSettingHeight = 160;
        } else {
            prevSettingWidth = 160;
            prevSettingHeight = 120;
        }

        cameraParameters.setPreviewSize(previewWidth, previewHeight);

//        mFaceView.setPreviewWidth(previewWidth);
//        mFaceView.setPreviewHeight(previewHeight);
    }

    private void setAutoFocus(Camera.Parameters cameraParameters) {
        List<String> focusModes = cameraParameters.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE))
            cameraParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
    }

    private void startPreview() {
        if (mCamera != null) {
            isThreadWorking = false;
            mCamera.startPreview();
            mCamera.setPreviewCallback(this);
            counter = 0;
        }
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mCamera.setPreviewCallbackWithBuffer(null);
        mCamera.setErrorCallback(null);
        mCamera.release();
        mCamera = null;
    }


    @Override
    public void onPreviewFrame(byte[] _data, Camera _camera) {
        if (!isThreadWorking) {
            if (counter == 0)
                start = System.currentTimeMillis();

            isThreadWorking = true;
            waitForFdetThreadComplete();
            detectThread = new FaceDetectThread(handler, this);
            detectThread.setData(_data);
            detectThread.start();
        }
    }

    private void waitForFdetThreadComplete() {
        if (detectThread == null) {
            return;
        }

        if (detectThread.isAlive()) {
            try {
                detectThread.join();
                detectThread = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


    // fps detect face (not FPS of camera)
    long start, end;
    int counter = 0;
    double fps;

    /**
     * Initializes the UI and initiates the creation of a face detector.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//取消标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏
        setContentView(R.layout.activity_camera_viewer);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= 23) {
            getPermission();

        }
        initView(icicle);

    }

    /**
     * Restarts the camera.
     */
    @Override
    protected void onResume() {
        super.onResume();

        Log.i(TAG, "onResume");
        startPreview();
        startAnimation();
    }

    /**
     * Stops the camera.
     */
    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
        if (mCamera != null) {
            mCamera.stopPreview();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SpUtils.getInstace(this).saveInt("Id", Id);
        anim.cancel();
        rotateAnimation.cancel();
    }

    /**
     * Releases the resources associated with the camera source, the associated detector, and the
     * rest of the processing pipeline.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * Do face detect in thread
     */
    private class FaceDetectThread extends Thread {
        private Handler handler;
        private byte[] data = null;
        private Context ctx;
        private Bitmap faceCroped;

        public FaceDetectThread(Handler handler, Context ctx) {
            this.ctx = ctx;
            this.handler = handler;
        }


        public void setData(byte[] data) {
            this.data = data;
        }

        public void run() {
//            Log.i("FaceDetectThread", "running");

            float aspect = (float) previewHeight / (float) previewWidth;
            int w = prevSettingWidth;
            int h = (int) (prevSettingWidth * aspect);

            Bitmap bitmap = Bitmap.createBitmap(previewWidth, previewHeight, Bitmap.Config.RGB_565);
            // face detection: first convert the image from NV21 to RGB_565
            YuvImage yuv = new YuvImage(data, ImageFormat.NV21,
                    bitmap.getWidth(), bitmap.getHeight(), null);
            // TODO: make rect a member and use it for width and height values above
            Rect rectImage = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

            // TODO: use a threaded option or a circular buffer for converting streams?
            //see http://ostermiller.org/convert_java_outputstream_inputstream.html
            ByteArrayOutputStream baout = new ByteArrayOutputStream();
            if (!yuv.compressToJpeg(rectImage, 100, baout)) {
                Log.e("CreateBitmap", "compressToJpeg failed");
            }

            BitmapFactory.Options bfo = new BitmapFactory.Options();
            bfo.inPreferredConfig = Bitmap.Config.RGB_565;
            bitmap = BitmapFactory.decodeStream(
                    new ByteArrayInputStream(baout.toByteArray()), null, bfo);

            Bitmap bmp = Bitmap.createScaledBitmap(bitmap, w, h, false);

            float xScale = (float) previewWidth / (float) prevSettingWidth;
            float yScale = (float) previewHeight / (float) h;

            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(cameraId, info);
            rotate = mDisplayOrientation;
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT && mDisplayRotation % 180 == 0) {
                if (rotate + 180 > 360) {
                    rotate = rotate - 180;
                } else
                    rotate = rotate + 180;
            }

            switch (rotate) {
                case 90:
                    bmp = ImageUtils.rotate(bmp, 90);
                    xScale = (float) previewHeight / bmp.getWidth();
                    yScale = (float) previewWidth / bmp.getHeight();
                    break;
                case 180:
                    bmp = ImageUtils.rotate(bmp, 180);
                    break;
                case 270:
                    bmp = ImageUtils.rotate(bmp, 270);
                    xScale = (float) previewHeight / (float) h;
                    yScale = (float) previewWidth / (float) prevSettingWidth;
                    break;
            }

            fdet = new FaceDetector(bmp.getWidth(), bmp.getHeight(), MAX_FACE);

            FaceDetector.Face[] fullResults = new FaceDetector.Face[MAX_FACE];
            int facenum = fdet.findFaces(bmp, fullResults);
            Log.e("tag", "face数量" + facenum);
            for (int i = 0; i < MAX_FACE; i++) {
                if (fullResults[i] == null) {
                    faces[i].clear();
                } else {
                    PointF mid = new PointF();
                    fullResults[i].getMidPoint(mid);

                    mid.x *= xScale;
                    mid.y *= yScale;

                    float eyesDis = fullResults[i].eyesDistance() * xScale;
                    float confidence = fullResults[i].confidence();
                    float pose = fullResults[i].pose(FaceDetector.Face.EULER_Y);
                    int idFace = Id;

                    Rect rect = new Rect(
                            (int) (mid.x - eyesDis * 1.5f),
                            (int) (mid.y - eyesDis * 1.85f),
                            (int) (mid.x + eyesDis * 1.5f),
                            (int) (mid.y + eyesDis * 1.85f));

                    /**
                     * Only detect face size > 100x100
                     */
                    if (rect.height() * rect.width() > 100 * 100) {
                        for (int j = 0; j < MAX_FACE; j++) {
                            float eyesDisPre = faces_previous[j].eyesDistance();
                            PointF midPre = new PointF();
                            faces_previous[j].getMidPoint(midPre);

                            RectF rectCheck = new RectF(
                                    (midPre.x - eyesDisPre * 1.5f),
                                    (midPre.y - eyesDisPre * 1.85f),
                                    (midPre.x + eyesDisPre * 1.5f),
                                    (midPre.y + eyesDisPre * 1.85f));

                            if (rectCheck.contains(mid.x, mid.y) && (System.currentTimeMillis() - faces_previous[j].getTime()) < 1000) {
                                idFace = faces_previous[j].getId();
                                break;
                            }
                        }
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] datas = baos.toByteArray();
                        if (idFace == Id) {
                            Id++;
                            saveImageNum = SAVEIMAGEMAX;
                        } else {
                            //
                            // 5帧保存一次图片
                            // because of some first frame have low quality
                            //
                            if (facesCount.get(idFace) == null) {
                                facesCount.put(idFace, 0);
                            } else {
                                int count = facesCount.get(idFace) + 1;
                                if (count <= 2 * SAVEIMAGEMAX) {
                                    facesCount.put(idFace, count);
                                    if (count == 2) {
                                        apiFace.getResult(datas);
                                    }
                                    if (count % 2 == 0) {
                                        --saveImageNum;
                                        if (saveImageNum > 0) {
                                            saveImage(bitmap);
                                        }
                                    }
                                }

                            }

                        }

                        faces[i].setFace(idFace, mid, eyesDis, confidence, pose, System.currentTimeMillis());

                        faces_previous[i].set(faces[i].getId(), faces[i].getMidEye(), faces[i].eyesDistance(), faces[i].getConfidence(), faces[i].getPose(), faces[i].getTime());


                    }
                }
            }
            isThreadWorking = false;
//            handler.post(new Runnable() {
//                public void run() {
//                    //send face to FaceView to draw rect
//                    mFaceView.setFaces(faces);
//
//                    //calculate FPS
//                    end = System.currentTimeMillis();
//                    counter++;
//                    double time = (double) (end - start) / 1000;
//                    if (time != 0)
//                        fps = counter / time;
//
//                    mFaceView.setFPS(fps);
//
//                    if (counter == (Integer.MAX_VALUE - 1000))
//                        counter = 0;
//

//                }
//            });


        }
    }

    private void saveImage(Bitmap bitmap) {
        String img_dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() +
                File.separator + Id;
        Log.e("path", img_dir);
        File file = new File(img_dir);
        if (!file.exists()) {
            //通过file的mkdirs()方法创建目录中包含却不存在的文件夹
            if (file.mkdirs()) {
                Log.e("tag", "文件夹创建成功");
            } else {
                Log.e("tag", "文件夹创建失败");
            }
        }
        String img_path = img_dir + File.separator  +System.currentTimeMillis() + ".jpeg";
        Log.e("path", img_path);
        if (!(faces[0].getMidEye().x == 0 && faces[0].getMidEye().y == 0)) {
            Bitmap faceCroped = ImageUtils.cropFace(faces[0], bitmap, rotate);
            BitmapUtils.saveJPGE_After(FaceDetectRGBActivity.this, faceCroped, img_path, 70);
        }
    }


}
