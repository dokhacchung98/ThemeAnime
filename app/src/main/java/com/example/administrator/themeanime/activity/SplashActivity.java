package com.example.administrator.themeanime.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.administrator.themeanime.R;

public class SplashActivity extends AppCompatActivity {
    private ImageView imageView;
    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_splash);
        imageView = findViewById(R.id.idLoad);
        animation = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.rotarionloading);
        imageView.setAnimation(animation);
        animation.start();
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(SplashActivity.this, MenuActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
