package com.esp8266collection.airquality;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.AsyncTask;

import com.esp8266collection.airquality.Callbacks.AnimationCallback;

public class AnimationTask extends AsyncTask<Boolean, Void, Void> {

    private AnimationCallback animationCallback;

    public AnimationTask(AnimationCallback animationCallback) {
        this.animationCallback = animationCallback;
    }

    @Override
    protected Void doInBackground(Boolean... booleans) {
        if(booleans[0]){
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        animationCallback.onAnimationEnd();
    }
}
