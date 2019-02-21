package com.example.a123.lookhot;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

/**
 * Created by 123 on 2018/5/30.
 */

public class WelcomeActivity extends Activity implements Runnable{
    AnimationDrawable welAnimation;
    final Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //播放帧动画
        ImageView animationImage = this.findViewById(R.id.wel_animation);
        animationImage.setBackgroundResource(R.drawable.welcome_animation);
        this.welAnimation = (AnimationDrawable) animationImage.getBackground();
        welAnimation.start();

        //设置线程延迟时间为3秒
        handler.postDelayed(this,4000);
    }

    @Override
    public void run() {
        Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
        startActivity(intent);
    }
}
