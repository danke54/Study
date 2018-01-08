package com.zhangke.servicedemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.zhangke.servicedemo.service.DownloadService;

public class IntentServcieActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_servcie);

        Button button = findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            private int urlid;
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntentServcieActivity.this, DownloadService.class);
                intent.putExtra(DownloadService.DOWN_URL, "url" + ++urlid);
                startService(intent);
            }
        });
    }
}


