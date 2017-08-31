package com.viovie.floatingwindow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View view = findViewById(R.id.hello);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FloatingViewBuilder.getInstance().buildView(MainActivity.this, new FloatingViewBuilder.OnClickListener() {
                    @Override
                    public void onClick() {
                        startActivity(new Intent(MainActivity.this, Main2Activity.class));
                    }
                });
            }
        });
        view.callOnClick();
    }

    @Override
    protected void onDestroy() {
        FloatingViewBuilder.getInstance().destroy();
        super.onDestroy();
    }
}
