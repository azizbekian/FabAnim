package com.incentive.fabanim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.ColorStateList;
import android.graphics.Path;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.PathInterpolator;

public class MainActivity extends AppCompatActivity {

    private boolean isActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        @ColorInt final int colorActive = ContextCompat.getColor(this, R.color.fb_color_active);
        @ColorInt final int colorPassive = ContextCompat.getColor(this, R.color.fb_color_passive);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        final float from = 1.0f;
        final float to = 1.3f;

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(fab, View.SCALE_X, from, to);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(fab, View.SCALE_Y,  from, to);
        ObjectAnimator translationZ = ObjectAnimator.ofFloat(fab, View.TRANSLATION_Z, from, to);

        AnimatorSet set1 = new AnimatorSet();
        set1.playTogether(scaleX, scaleY, translationZ);
        set1.setDuration(100);
        set1.setInterpolator(new AccelerateInterpolator());

        set1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                fab.setImageResource(isActive ? R.drawable.heart_active : R.drawable.heart_passive);
                fab.setBackgroundTintList(ColorStateList.valueOf(isActive ? colorActive : colorPassive));
                isActive = !isActive;
            }
        });

        ObjectAnimator scaleXBack = ObjectAnimator.ofFloat(fab, View.SCALE_X, to, from);
        ObjectAnimator scaleYBack = ObjectAnimator.ofFloat(fab, View.SCALE_Y, to, from);
        ObjectAnimator translationZBack = ObjectAnimator.ofFloat(fab, View.TRANSLATION_Z, to, from);

        Path path = new Path();
        path.moveTo(0.0f, 0.0f);
        path.lineTo(0.5f, 1.3f);
        path.lineTo(0.75f, 0.8f);
        path.lineTo(1.0f, 1.0f);
        PathInterpolator pathInterpolator = new PathInterpolator(path);

        AnimatorSet set2 = new AnimatorSet();
        set2.playTogether(scaleXBack, scaleYBack, translationZBack);
        set2.setDuration(300);
        set2.setInterpolator(pathInterpolator);

        final AnimatorSet set = new AnimatorSet();
        set.playSequentially(set1, set2);

        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                fab.setClickable(true);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                fab.setClickable(false);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set.start();
            }
        });
    }
}
