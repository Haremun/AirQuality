package com.esp8266collection.airquality;

import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.esp8266collection.airquality.Callbacks.AnimationCallback;

import java.util.ArrayList;
import java.util.List;

public class ToastDrawerAnimation extends Thread {

    private Context _context;
    private ImageView _imageView;
    private boolean _animationDone;

    private List<ToastMessage> toastMessages;
    private int counter = 0;

    private AnimationCallback _animationCallback;

    public static int SHOW_AND_HIDE = 0;
    public static int SHOW = 1;
    public static int HIDE = 2;

    public ToastDrawerAnimation(Context context, AnimationCallback animationCallback, ImageView imageView) {
        this._context = context;
        this._imageView = imageView;
        this._animationDone = false;
        this._animationCallback = animationCallback;

        toastMessages = new ArrayList<>();
    }

    @Override
    public void run() {
        while (!_animationDone) {
            try {
                while(counter < toastMessages.size()){
                    Log.i("Animation_test", counter + " " + toastMessages.size());
                    ToastMessage toastMessage = toastMessages.get(counter);
                    switch (toastMessage.getToastType()) {
                        case 0: {
                            show();
                            Thread.sleep(400);
                            _animationCallback.onToastShow(toastMessage.getToastMessage());
                            Thread.sleep(1000);
                            _animationCallback.onToastHide();
                            hide();
                            Thread.sleep(400);
                            _animationCallback.onToastEnd();
                            break;
                        }
                        case 1: {
                            show();
                            Thread.sleep(400);
                            _animationCallback.onToastShow(toastMessage.getToastMessage());
                            _animationCallback.onToastEnd();
                            break;
                        }
                        case 2: {
                            _animationCallback.onToastHide();
                            hide();
                            Thread.sleep(400);
                            _animationCallback.onToastEnd();
                            break;
                        }
                    }
                    counter++;
                    Thread.sleep(50);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public void startToast(int toastType, String text) {
        toastMessages.add(new ToastMessage(toastType, text));
    }

    public void startToast(int toastType) {
        toastMessages.add(new ToastMessage(toastType, ""));
    }

    private void show() {
        Drawable drawable;
        drawable = _context.getDrawable(R.drawable.show_information_frame);
        _imageView.setImageDrawable(drawable);
        AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) _imageView.getDrawable();
        animatedVectorDrawable.start();
    }

    private void hide() {
        Drawable drawable;
        drawable = _context.getDrawable(R.drawable.hide_information_frame);
        _imageView.setImageDrawable(drawable);
        AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) _imageView.getDrawable();
        animatedVectorDrawable.start();
    }
}
