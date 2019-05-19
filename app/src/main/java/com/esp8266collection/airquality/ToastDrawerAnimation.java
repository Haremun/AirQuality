package com.esp8266collection.airquality;

import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.esp8266collection.airquality.Callbacks.AnimationCallback;

public class ToastDrawerAnimation extends Thread {

    private Context _context;
    private ImageView _imageView;
    private boolean _animationDone;

    private int _toastType = -1;
    private String _toastText;

    private AnimationCallback _animationCallback;

    public static int SHOW_AND_HIDE = 0;
    public static int SHOW = 1;
    public static int HIDE = 2;

    public ToastDrawerAnimation(Context context, AnimationCallback animationCallback, ImageView imageView) {
        this._context = context;
        this._imageView = imageView;
        this._animationDone = false;
        this._animationCallback = animationCallback;
    }

    @Override
    public void run() {
        while (!_animationDone) {
            switch (_toastType) {
                case 0: {
                    try {
                        show();
                        Thread.sleep(400);
                        _animationCallback.onToastShow(_toastText);
                        Thread.sleep(1000);
                        _animationCallback.onToastHide();
                        hide();
                        _toastType = -1;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case 1: {
                    try {
                        show();
                        Thread.sleep(400);
                        _animationCallback.onToastShow(_toastText);
                        _toastType = -1;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case 2: {
                    hide();
                    _animationCallback.onToastHide();
                    _toastType = -1;
                    break;
                }
            }
        }
    }

    public void startToast(int toastType, String text) {
        this._toastType = toastType;
        this._toastText = text;
    }
    public void startToast(int toastType) {
        this._toastType = toastType;
        this._toastText = "";
    }

    private void show() {
        Drawable drawable;
        drawable = _context.getResources().getDrawable(R.drawable.show_information_frame);
        _imageView.setImageDrawable(drawable);
        AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) _imageView.getDrawable();
        animatedVectorDrawable.start();
    }
    private void hide() {
        Drawable drawable;
        drawable = _context.getResources().getDrawable(R.drawable.hide_information_frame);
        _imageView.setImageDrawable(drawable);
        AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) _imageView.getDrawable();
        animatedVectorDrawable.start();
    }
}
