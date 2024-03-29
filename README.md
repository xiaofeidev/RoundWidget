# RoundWidget

本库的最新版本已发布至 mavenCentral，详见底部的说明

Java 实现的 Android 原生圆角矩形控件，圆角`ImageView`和圆角`FrameLayout`，详见项目代码中的`RoundImageView`和`RoundFrameLayout`。

# 更新日志

[点击查看](https://github.com/xiaofei-dev/RoundWidget/blob/master/release_log.md)

# 界面预览

<img src="https://github.com/xiaofei-dev/RoundWidget/blob/master/art/img_shot.jpg" width="40%" height="40%">

# 使用

`Gradle`依赖：

```groovy
implementation 'io.github.xiaofeidev:round:1.1.2'
```



**本库只支持在 `AndroidX` 的依赖基础上使用！**`miniSDK` = 14

项目中主要有`RoundImageView`和`RoundFrameLayout`这两个控件，它们具有如下公共属性：

|         属性名         |                            解释                            |
| :--------------------: | :--------------------------------------------------------: |
|       rd_radius        | 尺寸值，设置 View 整体四个圆角的圆角半径，会被下面的值覆盖 |
|   rd_top_left_radius   |             尺寸值，设置 View 左上角的圆角半径             |
|  rd_top_right_radius   |             尺寸值，设置 View 右上角的圆角半径             |
| rd_bottom_left_radius  |             尺寸值，设置 View 左下角的圆角半径             |
| rd_bottom_right_radius |             尺寸值，设置 View 右下角的圆角半径             |

关于 `rd_radius` 属性，其除了通常尺寸值外还可以设置为一个内置的枚举值：`circle`，这样整个 View 会被裁剪成一个正圆形！

当然你还可以通过把 `rd_radius` 属性的值设的很大或随便一个负尺寸值，这样也可以得到一个正圆形！

`RoundImageView`另外多了这几个独有的属性：

|     属性名      |                      解释                       |
| :-------------: | :---------------------------------------------: |
| rd_stroke_width |        尺寸值，设置 ImageView 的描边宽度        |
| rd_stroke_color |        颜色值，设置 ImageView 的描边颜色        |
| rd_stroke_mode  | 枚举值，设置描边模式，取值为 padding 或 overlay |

关于`rd_stroke_mode`属性，其含义是当前`RoundImageView`的描边模式，是一个枚举值，只能取 `padding` 或 `overlay`这两者之一。这两个枚举值含义如下：

| `rd_stroke_mode` 枚举值 |                             解释                             |
| :---------------------: | :----------------------------------------------------------: |
|        `padding`        | 描边的像素不覆盖到下方的图片，通过增加 View padding 的方式实现，此为默认值 |
|        `overlay`        |                描边的像素直接覆盖到下方图片上                |

### 示例

`RoundImageView`：

```xml
<io.github.xiaofeidev.round.RoundImageView
        android:id="@+id/imgS1"
        android:layout_width="0dp"
        android:layout_height="0dp"
        app:layout_constraintTop_toBottomOf="@id/text3"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toStartOf="@id/imgS2"
        app:layout_constraintHorizontal_weight="1"
        app:layout_constraintDimensionRatio="1"
        android:layout_marginStart="10dp"
        android:layout_marginEnd="5dp"
        app:rd_radius="10dp"
        app:rd_stroke_width="4dp"
        app:rd_stroke_color="@android:color/black"
        android:scaleType="fitXY"
        android:adjustViewBounds="true"
        app:srcCompat="@drawable/ic_profile"/>
```

效果：

<img src="https://github.com/xiaofei-dev/RoundWidget/blob/master/art/img_shot_image.jpg">

`RoundFrameLayout`：

```xml
<io.github.xiaofeidev.round.RoundFrameLayout
        android:id="@+id/frame1"
        android:layout_width="0dp"
        android:layout_height="0dp"
        app:layout_constraintTop_toBottomOf="@id/text1"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toStartOf="@id/frame2"
        app:layout_constraintHorizontal_weight="1"
        app:layout_constraintDimensionRatio="1"
        android:layout_marginStart="10dp"
        android:layout_marginEnd="5dp"
        app:rd_radius="10dp"
        app:rd_top_left_radius="40dp"
        app:rd_bottom_right_radius="40dp">
        <androidx.appcompat.widget.AppCompatImageView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:scaleType="fitXY"
            android:adjustViewBounds="true"
            app:srcCompat="@drawable/ic_profile"/>
</io.github.xiaofeidev.round.RoundFrameLayout>
```

效果：

<img src="https://github.com/xiaofei-dev/RoundWidget/blob/master/art/img_shot_frame.jpg">

# 说明

> *本库的最新版本 1.1.2 已发布至 mavenCentral，注意 mavenCentral 仅提供 1.1.2 及更高版本的依赖！且本库自 1.1.2 版本(含 1.1.2 版本)之后仅会发布到 mavenCentral（旧版本依然可通过 jcenter 依赖且只能通过 jcenter 依赖）*
>
> *还有一点需要注意的是本库自 1.1.2 版本起修改了控件的包路径，从：*
>
> *`com.github.xiaofeidev.xxx`*
>
> *修改为了：*
>
> *`io.github.xiaofeidev.xxx`*
>
> *(这是 mavenCentral 对只有 github 地址的库的发布要求，要求库的 groupID 必须是 io.github.XXX，而不能是 com.github.XXX)*
>
> *从老版本更新过来的朋友一定要注意在使用控件的地方同步修改下包路径*
>
> *包括本库的 gradle 依赖脚本也要由原来的：*
>
> *`implementation 'com.github.xxx'`*
>
> *改为：*
>
> *`implementation 'io.github.xxx'`* 
