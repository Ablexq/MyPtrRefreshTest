package com.example.lenovo.myptrrefreshtest.view;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lenovo.myptrrefreshtest.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

/**
 *
 * 美团下拉：
 *
 * step0:下拉/准备下拉状态
 * step1:下拉->可刷新状态
 * step2:刷新状态
 * step3:刷新结束状态
 */

public class PtrMeiTuanHeader extends LinearLayout implements PtrUIHandler {
    private static final String TAG = PtrMeiTuanHeader.class.getSimpleName();
    @BindView(R.id.ivFirst)
    ImageView ivFirst;
    @BindView(R.id.ivSecond)
    ImageView ivSecond;
    @BindView(R.id.ivThird)
    ImageView ivThird;
    @BindView(R.id.tvMsg)
    TextView tvMsg;
    private Matrix mMatrix = new Matrix();
    private AnimationDrawable mSecondAnimation;
    private AnimationDrawable mThirdAnimation;

    public PtrMeiTuanHeader(Context context) {
        this(context, null);
    }

    public PtrMeiTuanHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PtrMeiTuanHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View headerView = LayoutInflater.from(context).inflate(R.layout.header_meituan, this);//
        ButterKnife.bind(headerView);
        init();
    }

    /**
     * 初始化帧动画
     */
    private void init() {
        mSecondAnimation = (AnimationDrawable) ivSecond.getDrawable();
        mThirdAnimation = (AnimationDrawable) ivThird.getDrawable();
    }


    /**
     * 刷新准备 : Header 将要出现时调用。
     *
     * @param frame
     */
    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {//调用1
        Log.e("111111","onUIRefreshPrepare");

        pullStep0(0.0f);
    }

    /**
     * 开始刷新 : Header 进入刷新状态之前调用。
     *
     * @param frame
     */
    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {//调用4
        Log.e("111111","onUIRefreshBegin");

        pullStep2();
    }

    /**
     * 刷新结束 : Header 开始向上移动之前调用。
     *
     * @param frame
     */
    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {//调用5
        Log.e("111111","onUIRefreshComplete");

        pullStep3();
    }

    /**
     * 刷新重置 : Content重新回到顶部Header消失，重置 View。
     *
     * @param frame
     */
    @Override
    public void onUIReset(PtrFrameLayout frame) {//调用6
        Log.e("111111","onUIReset");

        resetView();
    }

    /**
     * isUnderTouch: 是否松手（true、false）
     * status: 状态（1:init、2:prepare、3:loading、4：complete）
     */
    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        final int mOffsetToRefresh = frame.getOffsetToRefresh();//可释放刷新位置
        final int currentPos = ptrIndicator.getCurrentPosY();//当前位置
        final int lastPos = ptrIndicator.getLastPosY();//前一个位置

        if (isUnderTouch) {//下拉过程的两个阶段：到达可释放刷新位置前后
            if (lastPos < currentPos && currentPos < mOffsetToRefresh) {//调用2
                Log.e("111111","onUIPositionChange ----");

                float scale = lastPos / (float) mOffsetToRefresh;
                pullStep0(scale);
            } else {//调用3
                Log.e("111111","onUIPositionChange ++++");

                pullStep1(frame);
            }
        }


    }

    private void pullStep0(float scale) {
        ivFirst.setVisibility(View.VISIBLE);
        ivSecond.setVisibility(View.INVISIBLE);
        ivThird.setVisibility(View.INVISIBLE);
        scaleImage(scale);
        tvMsg.setText(getResources().getString(R.string.cube_ptr_pull_down_to_refresh));
    }

    private void pullStep1(PtrFrameLayout frame) {
        if (!frame.isPullToRefresh()) {
            ivFirst.setVisibility(View.INVISIBLE);
            ivSecond.setVisibility(View.VISIBLE);
            ivThird.setVisibility(View.INVISIBLE);
            mSecondAnimation.start();
            tvMsg.setText(getResources().getString(R.string.cube_ptr_release_to_refresh));
        }
    }

    private void pullStep2() {
        ivFirst.setVisibility(View.INVISIBLE);
        ivSecond.setVisibility(View.INVISIBLE);
        ivThird.setVisibility(View.VISIBLE);
        cancelAnimationSecond();
        mThirdAnimation.start();
        tvMsg.setText(R.string.cube_ptr_refreshing);
    }

    private void pullStep3() {
        ivFirst.setVisibility(View.INVISIBLE);
        ivSecond.setVisibility(View.INVISIBLE);
        ivThird.setVisibility(View.VISIBLE);
        cancelAnimationThird();
        tvMsg.setText(getResources().getString(R.string.cube_ptr_refresh_complete));
    }

    private void scaleImage(float scale) {
        mMatrix.setScale(scale, scale, ivFirst.getWidth() / 2, ivFirst.getHeight() / 2);
        ivFirst.setImageMatrix(mMatrix);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        resetView();
    }

    private void resetView() {
        cancelAnimations();
    }

    private void cancelAnimations() {
        cancelAnimationSecond();
        cancelAnimationThird();
    }

    private void cancelAnimationSecond() {
        if (mSecondAnimation != null && mSecondAnimation.isRunning()) {
            mSecondAnimation.stop();
        }
    }

    private void cancelAnimationThird() {
        if (mThirdAnimation != null && mThirdAnimation.isRunning()) {
            mThirdAnimation.stop();
        }
    }
}
