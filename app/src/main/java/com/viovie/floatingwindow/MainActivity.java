package com.viovie.floatingwindow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingViewBuilder.getInstance().buildView(this, new FloatingViewBuilder.OnClickListener() {
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
