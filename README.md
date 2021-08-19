# Android-LikeAnimaView
> [笔记](https://blog.csdn.net/MoLiao2046/article/details/119784407)
#### 使用
```kotlin
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
```
#### 效果
https://img-blog.csdnimg.cn/02106cddfddf4d209ee9dad595d3a895.gif#pic_center
#### 使用
```kotlin
// 效果View
val textView = TextView(this@MainActivity2)
textView.text = "+1"
textView.setTextColor(Color.RED)
textView.textSize = mBtn.textSize
// 效果View动画
val animator = ValueAnimator.ofInt(10, 200)
animator.duration = 800
ViewLikeUtils(findViewById<Button>(R.id.btn_anim), textView) { clickView, toggle, mUtils ->
    // 开始动画
    mUtils.startLikeAnim(animator)
}
```
#### 效果
https://img-blog.csdnimg.cn/b87148459a444c2c967b8c76c4afc59c.gif#pic_center