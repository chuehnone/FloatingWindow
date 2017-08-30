package com.viovie.floatingwindow;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class FloatingViewBuilder implements View.OnTouchListener {
    public static interface OnClickListener {
        void onClick();
    }

    private static class Holder {
        private static final FloatingViewBuilder INSTANCE = new FloatingViewBuilder();
    }

    private Context mContext;
    private FloatingActionButton mFloatingView;
    private WindowManager mWM;
    private WindowManager.LayoutParams mParams;
    private boolean mIsMove;
    private int mLastX, mLastY, mParamX, mParamY;
    private OnClickListener mOnClickListener;

    private FloatingViewBuilder() {}

    public static FloatingViewBuilder getInstance() {
        return Holder.INSTANCE;
    }

    public void buildView(Context context, OnClickListener onClickListener) {
        destroy();
        mOnClickListener = onClickListener;

        mFloatingView = new FloatingActionButton(context);
        mFloatingView.setImageResource(R.drawable.ic_open_in_new);

        mWM = (WindowManager) context.getApplicationContext().getSystemService(
                Context.WINDOW_SERVICE);

        mParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= 19) {
            mParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        } else {
            mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }

        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.format = PixelFormat.RGBA_8888;

        mFloatingView.setOnTouchListener(this);

        mWM.addView(mFloatingView, mParams);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = (int) motionEvent.getRawX();
                mLastY = (int) motionEvent.getRawY();
                mParamX = mParams.x;
                mParamY = mParams.y;
                mIsMove = false;
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = (int) motionEvent.getRawX() - mLastX;
                int dy = (int) motionEvent.getRawY() - mLastY;
                mParams.x = mParamX + dx;
                mParams.y = mParamY + dy;
                mIsMove = true;

                // Update view location
                mWM.updateViewLayout(view, mParams);
                break;
            case MotionEvent.ACTION_UP:
                if (!mIsMove) {
                    if (mOnClickListener != null) {
                        mOnClickListener.onClick();
                    }
                    view.setVisibility(View.GONE);
                }
                break;
        }
        return true;
    }

    public boolean showFloatingView() {
        if (mFloatingView != null) {
            mFloatingView.setVisibility(View.VISIBLE);
            return true;
        }
        return false;
    }

    public void destroy() {
        if (mWM != null && mFloatingView != null) {
            mWM.removeView(mFloatingView);
        }
        mOnClickListener = null;
        mWM = null;
        mFloatingView = null;
        mContext = null;
    }
}
