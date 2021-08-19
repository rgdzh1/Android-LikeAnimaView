package com.yey.likeanimaview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.PointF;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BaseInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Random;

public class ViewLikeBesselUtils {
    public interface ViewLikeClickListener {
        /**
         * @param view                被点赞的按钮
         * @param toggle              开关
         * @param viewLikeBesselUtils 工具类本身
         */
        void onClick(View view, boolean toggle, ViewLikeBesselUtils viewLikeBesselUtils);
    }

    // 被点击的按钮
    private View mClickView;
    private View[] mAnimViews;
    private ViewLikeClickListener mListener;
    private boolean toggle = false; // 点击开关标识
    private int mX; // 距离屏幕左侧距离
    private int mY; // 距离屏幕顶端距离, 越往下数值越大
    private Random mRandom = new Random(); // 随机数

    /**
     * @param mClickView 被点击的View
     * @param mAnimViews 点赞后, 向上浮动的View数组
     * @param mListener  被点击的View,点击后的回调事件.
     */
    public ViewLikeBesselUtils(View mClickView, View[] mAnimViews, @NonNull ViewLikeClickListener mListener) {
        this.mClickView = mClickView;
        this.mAnimViews = mAnimViews;
        this.mListener = mListener;
        initListener();
    }

    /**
     * 设置View的监听
     */
    private void initListener() {
        mClickView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP || motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                    getLocation(); // 获取被点击View的坐标
                    toggle = !toggle;
                    if (mListener != null) {
                        mListener.onClick(mClickView, toggle, ViewLikeBesselUtils.this);
                    }
                    // mView.performClick();
                }
                // 正常的OnClickListener将无法调用
                return true;
            }
        });
    }

    /**
     * 获取View在屏幕中的坐标
     */
    private void getLocation() {
        int[] mLocation = new int[2];
        mClickView.getLocationInWindow(mLocation);
        mX = mLocation[0];
        mY = mLocation[1];
    }

    /**
     * 开始动画
     *
     * @param mAnimView
     */
    private void startAnim(View mAnimView, int mTime) {
        AnimatorSet animatorSet = new AnimatorSet();
        ArrayList<BaseInterpolator> interpolators = new ArrayList<>();
        interpolators.add(new AccelerateInterpolator());
        interpolators.add(new DecelerateInterpolator());
        interpolators.add(new AccelerateDecelerateInterpolator());
        interpolators.add(new LinearInterpolator());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            animatorSet.setInterpolator(interpolators.get(mRandom.nextInt(4)));
        }
        // 合并动画
        animatorSet.playTogether(getAnimationSet(mAnimView), getBezierAnimatorSet(mAnimView));
        animatorSet.setTarget(mAnimView);
        animatorSet.setDuration(mTime);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                removeChildView(mAnimView);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animatorSet.start();
    }

    /**
     * 将上浮控件添加到屏幕中
     *
     * @param animview
     */
    private void addAnimView(View animview) {
        Activity activityFromView = getActivityFromView(mClickView);
        if (activityFromView != null) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            FrameLayout mRootView = (FrameLayout) activityFromView.getWindow().getDecorView().getRootView();
            mRootView.addView(animview, params);
        }
    }

    /**
     * 开始动画
     */
    public void startLikeAnim() {
        for (View mAnimView : mAnimViews) {
            removeChildView(mAnimView);
            addAnimView(mAnimView);
            startAnim(mAnimView, mRandom.nextInt(1500));
        }
    }

    /**
     * 获取属性动画
     */
    private AnimatorSet getAnimationSet(View mView) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mView, "scaleX", 0.4f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mView, "scaleY", 0.4f, 1f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(mView, "alpha", 1f, 0.2f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY, alpha);
        return animatorSet;
    }

    /**
     * 获取贝塞尔动画
     */
    private ValueAnimator getBezierAnimatorSet(View mView) {
        // 测量view
        mView.measure(0, 0);
        // 屏幕宽
        int width = getActivityFromView(mClickView).getWindowManager().getDefaultDisplay().getWidth();
        int mPointF0X = mX + mRandom.nextInt(mView.getMeasuredWidth());
        int mPointF0Y = mY - mView.getMeasuredHeight() / 2;
        // 起点
        PointF pointF0 = new PointF(mPointF0X, mPointF0Y);
        // 终点
        PointF pointF3 = new PointF(mRandom.nextInt(width - 100), 0f);
        // 第二点
        PointF pointF1 = new PointF(mRandom.nextInt(width - 100), (float) (mY * 0.7));
        // 第三点
        PointF pointF2 = new PointF(mRandom.nextInt(width - 100), (float) (mY * 0.3));
        BezierEvaluator be = new BezierEvaluator(pointF1, pointF2);
        ValueAnimator bezierAnimator = ValueAnimator.ofObject(be, pointF0, pointF3);
        bezierAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                PointF pointF = (PointF) valueAnimator.getAnimatedValue();
                mView.setX(pointF.x);
                mView.setY(pointF.y);
            }
        });
        return bezierAnimator;
    }

    /**
     * 获取Activity
     *
     * @param view
     * @return
     */
    public Activity getActivityFromView(View view) {
        if (null != view) {
            Context context = view.getContext();
            while (context instanceof ContextWrapper) {
                if (context instanceof Activity) {
                    return (Activity) context;
                }
                context = ((ContextWrapper) context).getBaseContext();
            }
        }
        return null;
    }

    /**
     * 将子View从父容器中去除
     */
    private void removeChildView(View mChildView) {
        ViewGroup parentViewGroup = (ViewGroup) mChildView.getParent();
        if (parentViewGroup != null) {
            parentViewGroup.removeView(mChildView);
        }
    }
}
