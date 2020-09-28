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

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.github.xiaofeidev.round.round.RoundStatus;
import com.github.xiaofeidev.round.round.RoundStatusImpl;

/**
 * <p>圆角或圆形裁剪的 ImageView，裁剪画布实现我暂时想不到方法能消除边缘锯齿，所以只能用 PorterDuffXfermode</p>
 * @author 黎曼
 * date 2020/7/5
 */
public class RoundImageView extends AppCompatImageView implements RoundStatus {
    private final int INVALID_VALUE = -1;
    //描边绘制模式，扩大 View 内边距，不覆盖图片的像素，默认是此模式
    public static final int STROKE_MODE_PADDING = 0;
    //描边绘制模式，直接覆盖到图片的像素上面
    public static final int STROKE_MODE_OVERLAY = 1;
    //整个 View 的圆角信息都在这个对象里面
    private RoundStatus mRoundStatus;
    //描边的圆角信息
    private RoundStatus mRoundStatusStroke;
    //圆角效果主要存在这个路径里
    private Path mPath;
    //圆角矩形描述
    private RectF mRectF;
    //画笔
    private Paint mPaint;

    //描边的路径
    private Path mPathStroke;
    //描边的画笔
    private Paint mPaintStroke;
    //描边宽度
    private int mStrokeWidth;
    //描边颜色
    @ColorInt
    private int mStrokeColor;
    //描边模式
    private int mStrokeMode;

    //初始的内边距
    private int mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom;

    //绘制缓冲，避免击穿整个 window 的画布出现黑色背景的情况
    private Bitmap mBitmap = null;
    private Canvas mCanvas = new Canvas();
    private Matrix mMatrix = new Matrix();

    public RoundImageView(@NonNull Context context) {
        this(context, null);
    }

    public RoundImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public RoundImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaddingLeft = getPaddingLeft();
        mPaddingTop = getPaddingTop();
        mPaddingRight = getPaddingRight();
        mPaddingBottom = getPaddingBottom();

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.RoundImageView,
                0, 0);

        try {
            //View 整体的圆角半径
            float radius;
            float topLeftRadius;
            float topRightRadius;
            float bottomLeftRadius;
            float bottomRightRadius;

            if (typedArray.peekValue(R.styleable.RoundImageView_rd_radius) != null &&
                    typedArray.peekValue(R.styleable.RoundImageView_rd_radius).type == TypedValue.TYPE_DIMENSION){
                radius = typedArray.getDimension(R.styleable.RoundImageView_rd_radius, 0);
            }else {//TypedValue.TYPE_INT_DEC
                radius = typedArray.getInteger(R.styleable.RoundImageView_rd_radius, 0);
            }
            topLeftRadius = typedArray.getDimension(R.styleable.RoundImageView_rd_top_left_radius, INVALID_VALUE);
            topRightRadius = typedArray.getDimension(R.styleable.RoundImageView_rd_top_right_radius, INVALID_VALUE);
            bottomRightRadius = typedArray.getDimension(R.styleable.RoundImageView_rd_bottom_right_radius, INVALID_VALUE);
            bottomLeftRadius = typedArray.getDimension(R.styleable.RoundImageView_rd_bottom_left_radius, INVALID_VALUE);

            mRoundStatus = new RoundStatusImpl.RoundStatusBuilder()
                    .setMRadius(radius)
                    .setMTopLeftRadius(topLeftRadius)
                    .setMTopRightRadius(topRightRadius)
                    .setMBottomRightRadius(bottomRightRadius)
                    .setMBottomLeftRadius(bottomLeftRadius)
                    .build();

            mStrokeColor = typedArray.getColor(R.styleable.RoundImageView_rd_stroke_color, Color.TRANSPARENT);
            mStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.RoundImageView_rd_stroke_width, 0);
            if (mStrokeWidth < 0){
                mStrokeWidth = 0;
            }
            mStrokeMode = typedArray.getInteger(R.styleable.RoundImageView_rd_stroke_mode, STROKE_MODE_PADDING);
            mRoundStatusStroke = new RoundStatusImpl();
        } finally {
            //注意资源回收
            typedArray.recycle();
        }
        init();
    }

    private void init(){
        //关闭硬件加速
        if (Build.VERSION.SDK_INT < 16){
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        mPaintStroke = new Paint();
        mPaintStroke.setAntiAlias(true);
        mPaintStroke.setStyle(Paint.Style.STROKE);
        initStroke();

        mPath = new Path();
        mPathStroke = new Path();
        mRectF = new RectF();
        fillRadius();
        mRoundStatusStroke.fillRadius();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
    }

    private void initStroke(){
        mPaintStroke.setStrokeWidth(mStrokeWidth);
        mPaintStroke.setColor(mStrokeColor);

        float strokeOffset = mStrokeWidth/2f;
        mRoundStatusStroke.setRadius(getRadius() - strokeOffset);
        mRoundStatusStroke.setTopLeftRadius(getTopLeftRadius() - strokeOffset);
        mRoundStatusStroke.setTopRightRadius(getTopRightRadius() - strokeOffset);
        mRoundStatusStroke.setBottomRightRadius(getBottomRightRadius() - strokeOffset);
        mRoundStatusStroke.setBottomLeftRadius(getBottomLeftRadius() - strokeOffset);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w > 0){
            mPath.reset();
            //Path 的填充模式为反奇偶规则
            mPath.setFillType(Path.FillType.INVERSE_EVEN_ODD);
            mPathStroke.reset();
            if (getRadius() < 0){
                mRectF.set(0, 0, w, h);
                mPath.addCircle((float) (w / 2), (float) (h / 2), (float) (Math.min(w, h) / 2), Path.Direction.CW);
                mPathStroke.addCircle((float) (w / 2), (float) (h / 2), (float) (Math.min(w, h) / 2) - mStrokeWidth/2f, Path.Direction.CW);
            } else {
                mRectF.set(0, 0, w, h);
                mPath.addRoundRect(mRectF, getRadiusList(), Path.Direction.CW);
                float strokeOffset = mStrokeWidth/2f;
                mRectF.set(strokeOffset, strokeOffset,w - strokeOffset, h - strokeOffset);
                mPathStroke.addRoundRect(mRectF, mRoundStatusStroke.getRadiusList(), Path.Direction.CW);
                mRectF.set(0, 0, w, h);
            }
            checkPadding();

            if (mBitmap == null || mBitmap.getWidth() != w || mBitmap.getHeight() != h){
                mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                mCanvas.setBitmap(mBitmap);
            }
        }
    }

    //检查内边距是否需要调整
    private void checkPadding(){
        if (mStrokeMode == STROKE_MODE_PADDING){
            setPadding(mPaddingLeft + mStrokeWidth,
                    mPaddingTop + mStrokeWidth,
                    mPaddingRight + mStrokeWidth,
                    mPaddingBottom + mStrokeWidth);
        }
    }
    @Override
    public void onDraw(Canvas canvas) {
        mBitmap.eraseColor(Color.TRANSPARENT);
        super.onDraw(mCanvas);
        mCanvas.drawPath(mPath, mPaint);
        //开始绘制描边
        if (mStrokeWidth > 0){
            mCanvas.drawPath(mPathStroke, mPaintStroke);
        }
        canvas.drawBitmap(mBitmap, mMatrix, null);
    }

    private void update(){
        int w = getWidth();
        int h = getHeight();
        if (w > 0){
            mPath.reset();
            //Path 的填充模式为反奇偶规则
            mPath.setFillType(Path.FillType.INVERSE_EVEN_ODD);
            mPathStroke.reset();
            if (getRadius() < 0){
                mPath.addCircle((float) (w / 2), (float) (h / 2), (float) (Math.min(w, h) / 2), Path.Direction.CW);
                mPathStroke.addCircle((float) (w / 2), (float) (h / 2), (float) (Math.min(w, h) / 2) - mStrokeWidth/2f, Path.Direction.CW);
            } else {
                mRectF.set(0, 0, w, h);
                mPath.addRoundRect(mRectF, getRadiusList(), Path.Direction.CW);
                float strokeOffset = mStrokeWidth/2f;
                mRectF.set(strokeOffset, strokeOffset,w - strokeOffset, h - strokeOffset);
                mPathStroke.addRoundRect(mRectF, mRoundStatusStroke.getRadiusList(), Path.Direction.CW);
                mRectF.set(0, 0, w, h);
            }
        }
        invalidate();
    }

    public int getStrokeWidth() {
        return mStrokeWidth;
    }

    public void setStrokeWidth(int mStrokeWidth) {
        this.mStrokeWidth = mStrokeWidth;
        initStroke();
        checkPadding();
        update();
        invalidate();
    }

    public int getStrokeColor() {
        return mStrokeColor;
    }

    public void setStrokeColor(int mStrokeColor) {
        this.mStrokeColor = mStrokeColor;
        initStroke();
        invalidate();
    }

    public int getStrokeMode() {
        return mStrokeMode;
    }

    public void setStrokeMode(int mStrokeMode) {
        this.mStrokeMode = mStrokeMode;
        update();
    }

    @Override
    public void fillRadius() {
        mRoundStatus.fillRadius();
    }

    @Override
    public void setRadius(float radius) {
        mRoundStatus.setRadius(radius);
        initStroke();
        update();
    }

    @Override
    public void setTopLeftRadius(float topLeftRadius) {
        mRoundStatus.setTopLeftRadius(topLeftRadius);
        initStroke();
        update();
    }

    @Override
    public void setTopRightRadius(float topRightRadius) {
        mRoundStatus.setTopRightRadius(topRightRadius);
        initStroke();
        update();
    }

    @Override
    public void setBottomRightRadius(float bottomRightRadius) {
        mRoundStatus.setBottomRightRadius(bottomRightRadius);
        initStroke();
        update();
    }

    @Override
    public void setBottomLeftRadius(float bottomLeftRadius) {
        mRoundStatus.setBottomLeftRadius(bottomLeftRadius);
        initStroke();
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
        return mRoundStatus.getBottomLeftRadius();
    }

    @Override
    public float[] getRadiusList() {
        return mRoundStatus.getRadiusList();
    }
}
