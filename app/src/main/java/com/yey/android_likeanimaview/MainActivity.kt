package com.yey.android_likeanimaview

import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.yey.likeanimaview.ViewLikeUtils

class MainActivity : AppCompatActivity() {
    lateinit var mBtn: Button
    lateinit var mBtnSkip: Button

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mBtn = findViewById<Button>(R.id.btn_anim)
        mBtnSkip = findViewById<Button>(R.id.btn_skip)
        initListener()
//        val sdf = SimpleDateFormat("yyyy-MM-dd")
//        val date1: Date = sdf.parse("2019-10-19")
//        val date2: Date = sdf.parse("2019-10-19")
//        Log.e("onCreate", "date1 : " + sdf.format(date1))
//        Log.e("onCreate", "date1 : " + sdf.format(date2))
//        if (date1 > date2) {
//            Log.e("onCreatedate2", "date1 大")
//        }
//        if (date1 < date2) {
//            Log.e("onCreatedate2", "date2 大")
//        }
//        if (date1 == date2) {
//            Log.e("onCreatedate2", "相等")
//        }
    }

    override fun onResume() {
        super.onResume()
        val textView = TextView(this@MainActivity)
        textView.text = "+1"
        textView.setTextColor(Color.RED)
        textView.setTextSize(mBtnSkip.textSize)
        val animator = ValueAnimator.ofInt(10, 200)
        animator.duration = 800
        ViewLikeUtils(mBtn, textView) { clickView, toggle, mUtils ->
            mUtils.startLikeAnim(animator)
        }
    }

    private fun initListener() {
        mBtnSkip.setOnClickListener {
            startActivity(Intent(this, MainActivity2::class.java))
        }
    }
}