package com.viovie.floatingwindow;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_OVERLAY = 1001;

    private boolean isOverlayGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, REQUEST_OVERLAY);
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View view = findViewById(R.id.hello);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildFloatingView();
            }
        });
        view.callOnClick();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_OVERLAY) {
            if (Build.VERSION.SDK_INT >= 23 && Settings.canDrawOverlays(this)) {
                buildFloatingView();
            }
        }
    }

    private void buildFloatingView() {
        FloatingViewBuilder.getInstance().buildView(MainActivity.this, new FloatingViewBuilder.OnClickListener() {
            @Override
            public void onClick() {
                startActivity(new Intent(MainActivity.this, Main2Activity.class));
            }
        });
    }

    @Override
    protected void onDestroy() {
        FloatingViewBuilder.getInstance().destroy();
        super.onDestroy();
    }
}
