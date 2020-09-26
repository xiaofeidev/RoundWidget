package com.github.xiaofeidev.roundwidget

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import com.bumptech.glide.Glide
import com.github.xiaofeidev.round.utils.SizeUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                textDp.text = getString(R.string.str_dp, progress)
                imgS1.radius = SizeUtils.dp2px(progress.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
        seek.progress = SizeUtils.px2dp(imgS1.radius)
        Glide
            .with(this)
            .asBitmap()
            //下面这是我随便找的一张网图，如果图挂了再随便找张别的网图就行！
            .load("https://pic2.zhimg.com/80/v2-68d714f04f0fa74100d5b122b7c91254_1440w.jpg")
            .into(img2)
    }
}