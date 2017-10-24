package com.viovie.floatingwindow;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
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

    private Handler mHandler;
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

        mParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.format = PixelFormat.RGBA_8888;

        mFloatingView.setOnTouchListener(this);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (mFloatingView != null) {
                    switch (msg.what) {
                        case 0:
                            mFloatingView.setVisibility(View.GONE);
                            break;
                        case 1:
                            mFloatingView.setVisibility(View.VISIBLE);
                            break;
                    }
                }
            }
        };

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
                    if (mHandler != null) {
                        mHandler.sendEmptyMessage(0);
                    }
                }
                break;
        }
        return true;
    }

    public boolean showFloatingView() {
        if (mHandler != null) {
            mHandler.sendEmptyMessage(1);
            return true;
        }
        return false;
    }

    public void destroy() {
        if (mWM != null && mFloatingView != null) {
            mWM.removeView(mFloatingView);
        }
        if (mHandler != null) {
            mHandler.removeMessages(0);
            mHandler.removeMessages(1);
            mHandler = null;
        }
        mOnClickListener = null;
        mWM = null;
        mParams = null;
        mFloatingView = null;
    }
}
