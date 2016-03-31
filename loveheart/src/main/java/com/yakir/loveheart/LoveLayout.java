package com.yakir.loveheart;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by user on 2016年03月28日.
 */
public class LoveLayout extends RelativeLayout {

    private Drawable oneDrawable;
    private Drawable twoDrawable;
    private Drawable threeDrawable;
    private int dWidth;
    private int dHeight;
    private int mWidth;
    private int mHeight;

    List<Drawable> mDrawableslist = new ArrayList<>();

    private LayoutParams params;
    private Random random = new Random();

    public LoveLayout(Context context) {
        super(context);
        initView();
    }

    public LoveLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LoveLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {

        oneDrawable = getResources().getDrawable(R.mipmap.pic1);
        twoDrawable = getResources().getDrawable(R.mipmap.pic2);
        threeDrawable = getResources().getDrawable(R.mipmap.pic3);
        mDrawableslist.add(oneDrawable);
        mDrawableslist.add(twoDrawable);
        mDrawableslist.add(threeDrawable);

        // 得到桃心的宽高
        dWidth = oneDrawable.getIntrinsicWidth();
        dHeight = oneDrawable.getIntrinsicHeight();
        // 这是桃心出发位置
        params = new LayoutParams(dWidth, dHeight);
        params.addRule(CENTER_HORIZONTAL);
        params.addRule(ALIGN_PARENT_BOTTOM);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 得到当前布局的宽和高
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    public void addLove() {
        ImageView image = new ImageView(getContext());
        image.setImageDrawable(mDrawableslist.get(random.nextInt(3)));
        image.setLayoutParams(params);
        addView(image);

        // 属性动画控制坐标
        AnimatorSet set = getAnimator(image);
        set.start();
    }

    /**
     * 得到所有动画集合
     *
     * @param imageView
     * @return
     */
    private AnimatorSet getAnimator(ImageView imageView) {

        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(imageView, "alpha", 0.3f, 1.0f);
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(imageView, "scaleX", 0.2f, 1.0f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(imageView, "scaleY", 0.2f, 1.0f);

        AnimatorSet mAnimatorSet = new AnimatorSet();
        mAnimatorSet.setDuration(500);
        // 将三个动画组成动画集合
        mAnimatorSet.playTogether(alphaAnimator, scaleXAnimator, scaleYAnimator);
        mAnimatorSet.setTarget(imageView);

        // 不断修改image坐标，实现贝塞尔曲线轨迹动画
        ValueAnimator bezierValueAnimator = getBezierValueAnimator(imageView);
        AnimatorSet bezierAnimatorSet = new AnimatorSet();
        // 按顺序播放动画
        bezierAnimatorSet.playSequentially(mAnimatorSet, bezierValueAnimator);
        bezierAnimatorSet.setTarget(imageView);

        return bezierAnimatorSet;
    }

    /**
     * 构造一个贝塞尔曲线动画
     *
     * @param imageView
     * @return
     */
    private ValueAnimator getBezierValueAnimator(final ImageView imageView) {

        PointF pointF0 = new PointF(mWidth / 2 - dWidth / 2, mHeight - dHeight);
        PointF pointF1 = getPointF(1);
        PointF pointF2 = getPointF(2);
        PointF pointF3 = new PointF(random.nextInt(mWidth), 0);

        BezierEvaluator bezierEvaluator = new BezierEvaluator(pointF1,pointF2);

        ValueAnimator animator = ValueAnimator.ofObject(bezierEvaluator,pointF0,pointF3);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF pointF = (PointF) animation.getAnimatedValue();
                imageView.setX(pointF.x);
                imageView.setY(pointF.y);
                imageView.setAlpha(1-animation.getAnimatedFraction());
            }
        });
        animator.setTarget(imageView);
        animator.setDuration(3000);

        return animator;
    }

    /**
     * 得到点的坐标
     *
     * @param i
     * @return
     */
    private PointF getPointF(int i) {
        PointF pointF = new PointF();
        pointF.x = random.nextInt(mWidth);

        if (i == 1) {
            pointF.y = random.nextInt(mHeight / 2) + (mHeight / 2);
        } else if (i == 2) {
            pointF.y = random.nextInt(mHeight / 2);
        }
        return pointF;
    }
}
