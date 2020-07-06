package com.github.xiaofeidev.round.round;

import java.util.Arrays;

/**
 * 记录一个 View 四角的圆角半径状态
 * @author 黎曼
 * @date 2020/7/6
 */
public class RoundStatusImpl implements RoundStatus {

    //ViewGroup 整体的圆角半径
    private float mRadius;
    private float mTopLeftRadius;
    private float mTopRightRadius;
    private float mBottomLeftRadius;
    private float mBottomRightRadius;

    private float[] mRadiusList = new float[8];

    @Override
    public void fillRadius(){
        setRadius(mRadius);

        setTopLeftRadius(mTopLeftRadius);
        setTopRightRadius(mTopRightRadius);
        setBottomRightRadius(mBottomRightRadius);
        setBottomLeftRadius(mBottomLeftRadius);
    }

    @Override
    public void setRadius(float radius) {
        mRadius = radius;
        Arrays.fill(mRadiusList, mRadius);
    }

    @Override
    public void setTopLeftRadius(float topLeftRadius) {
        mTopLeftRadius = topLeftRadius;
        if (mTopLeftRadius >= 0){
            Arrays.fill(mRadiusList, 0, 2, mTopLeftRadius);
        }
    }

    @Override
    public void setTopRightRadius(float topRightRadius) {
        mTopRightRadius = topRightRadius;
        if (mTopRightRadius >= 0){
            Arrays.fill(mRadiusList, 2, 4, mTopRightRadius);
        }
    }

    @Override
    public void setBottomRightRadius(float bottomRightRadius) {
        mBottomRightRadius = bottomRightRadius;
        if (mBottomRightRadius >= 0){
            Arrays.fill(mRadiusList, 4, 6, mBottomRightRadius);
        }
    }

    @Override
    public void setBottomLeftRadius(float bottomLeftRadius) {
        mBottomLeftRadius = bottomLeftRadius;
        if (mBottomLeftRadius >= 0){
            Arrays.fill(mRadiusList, 6, 8, mBottomLeftRadius);
        }
    }

    @Override
    public float getRadius() {
        return mRadius;
    }

    @Override
    public float getTopLeftRadius() {
        return mTopLeftRadius;
    }

    @Override
    public float getTopRightRadius() {
        return mTopRightRadius;
    }

    @Override
    public float getBottomRightRadius() {
        return mBottomRightRadius;
    }

    @Override
    public float getBottomLeftRadius() {
        return mBottomLeftRadius;
    }

    @Override
    public float[] getRadiusList() {
        return mRadiusList;
    }

    //RoundStatus 的生成器
    public static final class RoundStatusBuilder {
        //ViewGroup 整体的圆角半径
        private float mRadius;
        private float mTopLeftRadius;
        private float mTopRightRadius;
        private float mBottomLeftRadius;
        private float mBottomRightRadius;

        public RoundStatusBuilder() {
        }

        public RoundStatusBuilder setMRadius(float mRadius) {
            this.mRadius = mRadius;
            return this;
        }

        public RoundStatusBuilder setMTopLeftRadius(float mTopLeftRadius) {
            this.mTopLeftRadius = mTopLeftRadius;
            return this;
        }

        public RoundStatusBuilder setMTopRightRadius(float mTopRightRadius) {
            this.mTopRightRadius = mTopRightRadius;
            return this;
        }

        public RoundStatusBuilder setMBottomLeftRadius(float mBottomLeftRadius) {
            this.mBottomLeftRadius = mBottomLeftRadius;
            return this;
        }

        public RoundStatusBuilder setMBottomRightRadius(float mBottomRightRadius) {
            this.mBottomRightRadius = mBottomRightRadius;
            return this;
        }

        public RoundStatusImpl build() {
            RoundStatusImpl roundStatusImpl = new RoundStatusImpl();
            roundStatusImpl.mRadius = this.mRadius;
            roundStatusImpl.mBottomRightRadius = this.mBottomRightRadius;
            roundStatusImpl.mTopLeftRadius = this.mTopLeftRadius;
            roundStatusImpl.mTopRightRadius = this.mTopRightRadius;
            roundStatusImpl.mBottomLeftRadius = this.mBottomLeftRadius;
            return roundStatusImpl;
        }
    }
}
