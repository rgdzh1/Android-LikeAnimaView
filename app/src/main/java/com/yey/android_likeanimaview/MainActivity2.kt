package com.yey.android_likeanimaview

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.yey.likeanimaview.ViewLikeBesselUtils


class MainActivity2 : AppCompatActivity() {
    lateinit var mBtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        mBtn = findViewById(R.id.btn_anim)
        val mTVS = arrayOfNulls<TextView>(200)
        for (i in 0..199) {
            val mTV = TextView(this@MainActivity2)
            mTV.text = "赞"
            mTV.setTextColor(Color.RED)
            mTV.textSize = mBtn.textSize
            mTVS[i] = mTV
        }
        ViewLikeBesselUtils(mBtn, mTVS) { view, toggle, viewLikeBesselUtils ->
            viewLikeBesselUtils.startLikeAnim()
        }
    }
}


