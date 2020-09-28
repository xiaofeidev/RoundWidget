package com.github.xiaofeidev.round;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.xiaofeidev.round.round.RoundStatus;
import com.github.xiaofeidev.round.round.RoundStatusImpl;

/**
 * <p>圆角或圆形裁剪的 FrameLayout，裁剪画布实现我暂时想不到方法能消除边缘锯齿，所以只能用 PorterDuffXfermode</p>
 * @author 黎曼
 * date 2020/7/5
 */
public class RoundFrameLayout extends FrameLayout implements RoundStatus {
    private final int INVALID_VALUE = -1;
    //整个 View 的圆角信息都在这个对象里面
    private RoundStatus mRoundStatus;
    //圆角效果主要存在这个路径里
    private Path mPath;
    //圆角矩形描述
    private RectF mRectF;
    //画笔
    private Paint mPaint;

    //绘制缓冲，避免击穿整个 window 的画布出现黑色背景的情况
    private Bitmap mBitmap = null;
    private Canvas mCanvas = new Canvas();
    private Matrix mMatrix = new Matrix();

    public RoundFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    public RoundFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public RoundFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.RoundFrameLayout,
                0, 0);

        try {
            //ViewGroup 整体的圆角半径
            float radius;
            float topLeftRadius;
            float topRightRadius;
            float bottomLeftRadius;
            float bottomRightRadius;
            if (typedArray.peekValue(R.styleable.RoundFrameLayout_rd_radius) != null &&
                    typedArray.peekValue(R.styleable.RoundFrameLayout_rd_radius).type == TypedValue.TYPE_DIMENSION){
                radius = typedArray.getDimension(R.styleable.RoundFrameLayout_rd_radius, 0);
            }else {//TypedValue.TYPE_INT_DEC
                radius = typedArray.getInteger(R.styleable.RoundFrameLayout_rd_radius, 0);
            }
            topLeftRadius = typedArray.getDimension(R.styleable.RoundFrameLayout_rd_top_left_radius, INVALID_VALUE);
            topRightRadius = typedArray.getDimension(R.styleable.RoundFrameLayout_rd_top_right_radius, INVALID_VALUE);
            bottomRightRadius = typedArray.getDimension(R.styleable.RoundFrameLayout_rd_bottom_right_radius, INVALID_VALUE);
            bottomLeftRadius = typedArray.getDimension(R.styleable.RoundFrameLayout_rd_bottom_left_radius, INVALID_VALUE);

            mRoundStatus = new RoundStatusImpl.RoundStatusBuilder()
                    .setMRadius(radius)
                    .setMTopLeftRadius(topLeftRadius)
                    .setMTopRightRadius(topRightRadius)
                    .setMBottomRightRadius(bottomRightRadius)
                    .setMBottomLeftRadius(bottomLeftRadius)
                    .build();
        } finally {
            //注意资源回收
            typedArray.recycle();
        }
        init();
    }

    private void init(){
        setWillNotDraw(false);
        //关闭硬件加速
        if (Build.VERSION.SDK_INT < 16){
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        mPath = new Path();
        mRectF = new RectF();
        fillRadius();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w > 0){
            mPath.reset();
            //Path 的填充模式为反奇偶规则
            mPath.setFillType(Path.FillType.INVERSE_EVEN_ODD);
            if (getRadius() < 0){
                mPath.addCircle((float) (w / 2), (float) (h / 2), (float) (Math.min(w, h) / 2), Path.Direction.CW);
            } else {
                mRectF.set(0, 0, w, h);
                mPath.addRoundRect(mRectF, getRadiusList(), Path.Direction.CW);
            }

            if (mBitmap == null || mBitmap.getWidth() != w || mBitmap.getHeight() != h){
                mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                mCanvas.setBitmap(mBitmap);
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        mBitmap.eraseColor(Color.TRANSPARENT);
        super.draw(mCanvas);
        mCanvas.drawPath(mPath, mPaint);
        canvas.drawBitmap(mBitmap, mMatrix, null);
    }

    //绘制刷新时的必要逻辑
    private void update(){
        mPath.reset();
        //Path 的填充模式为反奇偶规则
        mPath.setFillType(Path.FillType.INVERSE_EVEN_ODD);
        if (getRadius() < 0){
            mPath.addCircle((float) (getWidth() / 2), (float) (getHeight() / 2), (float) (Math.min(getWidth(), getHeight()) / 2), Path.Direction.CW);
        } else {
            mPath.addRoundRect(mRectF, getRadiusList(), Path.Direction.CW);
        }
        invalidate();
    }

    @Override
    public void fillRadius() {
        mRoundStatus.fillRadius();
    }

    @Override
    public void setRadius(float radius) {
        mRoundStatus.setRadius(radius);
        update();
    }

    @Override
    public void setTopLeftRadius(float topLeftRadius) {
        mRoundStatus.setTopLeftRadius(topLeftRadius);
        update();
    }

    @Override
    public void setTopRightRadius(float topRightRadius) {
        mRoundStatus.setTopRightRadius(topRightRadius);
        update();
    }

    @Override
    public void setBottomRightRadius(float bottomRightRadius) {
        mRoundStatus.setBottomRightRadius(bottomRightRadius);
        update();
    }

    @Override
    public void setBottomLeftRadius(float bottomLeftRadius) {
        mRoundStatus.setTopLeftRadius(bottomLeftRadius);
        update();
    }

    @Override
    public float getRadius() {
        return mRoundStatus.getRadius();
    }

    @Override
    public float getTopLeftRadius() {
        return mRoundStatus.getTopLeftRadius();
    }

    @Override
    public float getTopRightRadius() {
        return mRoundStatus.getTopRightRadius();
    }

    @Override
    public float getBottomRightRadius() {
        return mRoundStatus.getBottomRightRadius();
    }

    @Override
    public float getBottomLeftRadius() {
        return mRoundStatus.getTopLeftRadius();
    }

    @Override
    public float[] getRadiusList() {
        return mRoundStatus.getRadiusList();
    }
}
