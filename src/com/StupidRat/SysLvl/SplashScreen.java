package com.StupidRat.SysLvl;

import com.StupidRat.SysLvl.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

public class SplashScreen extends SysLvlActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        startAnimating();
    }
    
    /**
     * Helper method to start the animation on the splash screen
     */
    private void startAnimating() {
        // Fade in top title
        ImageView logo1 = (ImageView) findViewById(R.id.ImageViewSplash);
        Animation fade1 = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        logo1.startAnimation(fade1);
     // Transition to Main Menu when bottom title finishes animating
        fade1.setAnimationListener(new AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                // The animation has ended, transition to the Main Menu screen
                startActivity(new Intent(SplashScreen.this, MenuScreen.class));
                SplashScreen.this.finish();
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop the animation
        ImageView logo1 = (ImageView) findViewById(R.id.ImageViewSplash);
        logo1.clearAnimation();
    }
    
    @Override
    protected void onResume() {
        super.onResume();

        // Start animating at the beginning so we get the full splash screen experience
        startAnimating();
    }
    
    
    
}