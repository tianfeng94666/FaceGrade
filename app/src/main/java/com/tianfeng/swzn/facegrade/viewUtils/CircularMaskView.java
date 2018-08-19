package com.tianfeng.swzn.facegrade.viewUtils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class CircularMaskView extends View {
    public CircularMaskView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        canvas.save();
        Paint mPaint = new Paint();
        //画笔颜色设置为浅蓝色
        mPaint.setColor(Color.parseColor("#00000000"));
        //画笔画一个矩形
        canvas.drawRect(new RectF(0, 0, width, height), mPaint);
        //画笔画一个圆形
        canvas.drawCircle(width/2, UIUtils.dip2px(210), UIUtils.dip2px(121), mPaint);
        //画笔颜色设置为浅红色
        mPaint.setColor(Color.parseColor("#cc090c14"));
        //画布裁剪一个矩形
        canvas.clipRect(new RectF(0, 0, width, height));//第一个裁剪一个形状相当于A
        //画布裁剪一个圆形
        Path mPath = new Path();
        mPath.addCircle(width/2, UIUtils.dip2px(210), UIUtils.dip2px(121), Path.Direction.CCW);
        /**这里只是改变第二个参数Region.Op.来观察效果*/
        canvas.clipPath(mPath, Region.Op.DIFFERENCE);//第二个裁剪一个形状相当于B
        //裁剪完之后,画一个长宽全覆盖的红色矩形观察效果
        canvas.drawRect(new RectF(0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE), mPaint);
        canvas.restore();
    }
}
