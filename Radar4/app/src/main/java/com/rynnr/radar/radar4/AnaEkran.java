package com.rynnr.radar.radar4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

public class AnaEkran extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        // Thread hazırlanıyor
        Thread thread = new Thread() {

            @Override
            public void run() {

                try {
                    synchronized (this) {
                        StartAnimations();
                        wait(1400);
                    }
                } catch (InterruptedException e) {

                    // Hata yönetimi

                } finally {

                    finish();

                    // Yeni açılmak istenen Intent
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), MainActivity.class);
                    startActivity(intent);

                }

            }
        };

        // Thread başlatılıyor
        thread.start();

    }

    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l = (LinearLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);
    }
}