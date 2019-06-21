package com.appman.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.appman.sfs.R;

/**
 * -----------------------------------------------------------------
 * Copyright (C) by AppMan, All rights reserved.
 * -----------------------------------------------------------------
 *
 * @author AppMan
 * @date Created on 2019/06/05
 */
public final class SwitchLayoutView extends RelativeLayout {
    private static final String TAG = SwitchLayoutView.class.getSimpleName();
    private FrameLayout layLarge;
    private FrameLayout laySmall;
    private ViewGroup.LayoutParams paramsLarge;
    private ViewGroup.LayoutParams paramsSmall;
    private static final float TOP = 10.0F;
    private static final float BOTTOM = 0.0F;
    private boolean isSurfaceView = false;
    private boolean isNeedRemoveGLView = false;

    private boolean isSmall = false;
    private ISwitchListener switchListener;
    private View childSmallView;

    public SwitchLayoutView(Context context) {
        this(context, null);
    }

    public SwitchLayoutView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initChildView() {
        this.layLarge = findViewById(R.id.a_view);
        this.laySmall = findViewById(R.id.b_view);
        if ((this.paramsLarge == null) || (this.paramsSmall == null)) {
            this.paramsLarge = this.layLarge.getLayoutParams();
            this.paramsSmall = this.laySmall.getLayoutParams();
        }
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if ((this.layLarge == null) || (this.laySmall == null)) {
            initChildView();
            if ((this.laySmall != null) && (this.laySmall.getChildCount() == 0) && (this.childSmallView != null)) {
                this.isSurfaceView = (this.childSmallView instanceof SurfaceView);
                ///this.isNeedRemoveGLView = (((this.childSmallView instanceof om.android.opengles.GLFrameSurface)) && (Build.VERSION.SDK_INT < Build.VERSION_CODES.O));
                this.laySmall.addView(this.childSmallView);
                // 初始化调用一次，则显示小地图
//                switchLayout();
            }
        }
    }

    public void switchLayout() {
        if ((this.layLarge == null) || (this.laySmall == null) || (this.paramsLarge == null) || (this.paramsSmall == null)) {
            return;
        }
        if (this.isSmall) {
            this.layLarge.setLayoutParams(this.paramsLarge);
            this.layLarge.setZ(BOTTOM);
            if (this.isNeedRemoveGLView) {
                this.laySmall.removeAllViews();
                this.laySmall.setLayoutParams(this.paramsSmall);
                this.laySmall.setZ(TOP);
                this.laySmall.addView(this.childSmallView);
            } else {
                this.laySmall.setLayoutParams(this.paramsSmall);
                this.laySmall.setZ(TOP);
            }
        } else {
            this.layLarge.setLayoutParams(this.paramsSmall);
            this.layLarge.setZ(TOP);
            if (this.isNeedRemoveGLView) {
                this.laySmall.removeAllViews();
                this.laySmall.setLayoutParams(this.paramsLarge);
                this.laySmall.setZ(BOTTOM);
                this.laySmall.addView(this.childSmallView);
            } else {
                this.laySmall.setLayoutParams(this.paramsLarge);
                this.laySmall.setZ(BOTTOM);
            }
        }
        setChildViewZOverlay(this.isSmall);

        this.isSmall = (!this.isSmall);
        if (this.switchListener != null) {
            this.switchListener.onChange(this.isSmall);
        }
        Log.i(TAG, "switchLayout isSwitch = " + this.isSmall + "  zA = " + this.layLarge.getZ() + "  zB = " + this.laySmall.getZ());
    }

    public void setSwitchListener(ISwitchListener listener) {
        this.switchListener = listener;
    }

    public void changeSmallViewVisibility(boolean is_show) {
        if (this.isSmall)
            this.layLarge.setVisibility(is_show ? VISIBLE : GONE);
        else
            this.laySmall.setVisibility(is_show ? VISIBLE : GONE);
    }

    private void setChildViewZOverlay(boolean overlay) {
        if (this.isSurfaceView) {
            ((SurfaceView) this.childSmallView).setZOrderMediaOverlay(overlay);
        }
    }

    public void addSmallView(View view) {
        this.childSmallView = view;
    }

    public void resetBigView() {
        if (this.isSmall) {
            switchLayout();
        }
        this.laySmall.setBackgroundColor(Color.BLACK);
    }

    public static abstract interface ISwitchListener {
        public abstract void onChange(boolean isSmall);
    }
}
