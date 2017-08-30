package com.viovie.floatingwindow;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createFloatingWindow();
    }

    private static FloatingActionButton mFloatingView;
    private WindowManager mWM;
    private WindowManager.LayoutParams mParams;
    private boolean mIsMove;
    private void createFloatingWindow() {
        if (mFloatingView != null) return;
        mFloatingView = new FloatingActionButton(this);
        mFloatingView.setImageResource(R.drawable.ic_open_in_new);

        mWM = (WindowManager) getApplicationContext().getSystemService(
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

        mFloatingView.setOnTouchListener(new View.OnTouchListener() {
            int lastX, lastY;
            int paramX, paramY;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) motionEvent.getRawX();
                        lastY = (int) motionEvent.getRawY();
                        paramX = mParams.x;
                        paramY = mParams.y;
                        mIsMove = false;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) motionEvent.getRawX() - lastX;
                        int dy = (int) motionEvent.getRawY() - lastY;
                        mParams.x = paramX + dx;
                        mParams.y = paramY + dy;
                        mIsMove = true;

                        // Update view location
                        mWM.updateViewLayout(view, mParams);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (!mIsMove) {
                            startActivity(new Intent(MainActivity.this, Main2Activity.class));
                            view.setVisibility(View.GONE);
                        }
                        break;
                }
                return true;
            }
        });

        mWM.addView(mFloatingView, mParams);
    }

    public static void showView() {
        if (mFloatingView != null) {
            mFloatingView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        mWM.removeView(mFloatingView);
        mFloatingView = null;
        super.onDestroy();
    }
}
