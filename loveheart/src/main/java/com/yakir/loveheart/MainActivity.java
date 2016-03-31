package com.yakir.loveheart;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private LoveLayout loveLayout;
    private static final int ADD_LOVE = 0x123;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == ADD_LOVE){
                loveLayout.addLove();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        loveLayout = (LoveLayout) findViewById(R.id.love_layout);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true){
                        Thread.sleep(400);
                        mHandler.sendEmptyMessage(ADD_LOVE);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
