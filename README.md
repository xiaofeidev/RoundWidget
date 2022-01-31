# RoundWidget
Java 实现的 Android 原生圆角矩形控件，圆角 `FrameLayout` 和圆角 `ImageView`，
详见项目代码中的 `RoundFrameLayout` 和 `RoundImageView`。

# 更新日志
[点击查看](https://github.com/xiaofei-dev/RoundWidget/blob/master/release_log.md)

# 界面预览

<img src="https://github.com/xiaofei-dev/RoundWidget/blob/master/art/img_shot.jpg" width="40%" height="40%">

# 使用

`Gradle`依赖：

```groovy
implementation 'com.github.xiaofeidev:round:$latest_version'
```

~~*JCenter 的工件库托管服务将于 2022 年 2 月 1 号停止，本库将会在此之前及时迁移到新的远程 Maven 仓库，大家请放心使用。*~~

JCenter 官方的最新声明如下：

> We listened to the community and will keep JCenter as a read-only repository indefinitely. Our customers and the community can continue to rely on JCenter as a reliable mirror for Java packages.
>
> 我们听取了社区的意见，并**<u>将 JCenter 无限期地保留为只读存储库</u>**。我们的客户和社区可以继续依赖 JCenter 作为 Java 包的可靠镜像。

[声明链接](https://jfrog.com/blog/into-the-sunset-bintray-jcenter-gocenter-and-chartcenter/)

因此，在本库发新版本之前，老的版本可以继续依赖于 JCenter 下载。在发布下一个版本的时候我计划整体迁移到 MavenCentral，敬请关注。当前的版本无需做任何变动便可继续正常使用。

**本库只支持在 `AndroidX` 的依赖基础上使用！**`miniSDK` = 14

项目中主要有  `RoundFrameLayout` 和 `RoundImageView`这两个控件，它们具有如下公共属性：

|         属性名         |                           解释                           |
| :--------------------: | :------------------------------------------------------: |
|       rd_radius        | 尺寸值，设置 View 整体四个圆角的圆角半径，会被下面的值覆盖 |
|   rd_top_left_radius   |            尺寸值，设置 View 左上角的圆角半径            |
|  rd_top_right_radius   |            尺寸值，设置 View 右上角的圆角半径            |
| rd_bottom_left_radius  |            尺寸值，设置 View 左下角的圆角半径            |
| rd_bottom_right_radius |            尺寸值，设置 View 右下角的圆角半径             |

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
|        `overlay`         |                描边的像素直接覆盖到下方图片上                |



### 示例

```xml
<com.github.xiaofeidev.round.RoundFrameLayout
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
</com.github.xiaofeidev.round.RoundFrameLayout>
```

效果：

<img src="https://github.com/xiaofei-dev/RoundWidget/blob/master/art/img_shot_frame.jpg">



```xml
<com.github.xiaofeidev.round.RoundImageView
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
