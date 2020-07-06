package com.github.xiaofeidev.round.round;

/**
 * 记录一个 View 四角的圆角半径状态
 * @author 黎曼
 * @date 2020/7/6
 */
public interface RoundStatus {
    //设置所有圆角的半径,，再分别设置每个单个圆角的半径
    void fillRadius();

    //设置所有圆角的半径
    void setRadius(float radius);

    //设置左上角圆角半径
    void setTopLeftRadius(float topLeftRadius);

    //设置右上角圆角半径
    void setTopRightRadius(float topRightRadius);

    //设置右下角圆角半径
    void setBottomRightRadius(float bottomRightRadius);

    //设置左下角圆角半径
    void setBottomLeftRadius(float bottomLeftRadius);

    //获取所有圆角的半径
    float getRadius();

    //获取左上角圆角半径
    float getTopLeftRadius();

    //获取右上角圆角半径
    float getTopRightRadius();

    //获取右下角圆角半径
    float getBottomRightRadius();

    //获取左下角圆角半径
    float getBottomLeftRadius();

    //获取记录各个角的圆角半径的数组
    float[] getRadiusList();
}
