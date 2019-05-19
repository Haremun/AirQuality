package com.esp8266collection.airquality;

import android.widget.ImageView;
import android.widget.TextView;

public class ToastDrawer {

    private ImageView _imgView;
    private TextView _textView;

    public ToastDrawer(ImageView imgView, TextView textView) {
        this._textView = textView;
        this._imgView = imgView;
    }

    public ImageView getImgView() {
        return _imgView;
    }

    public void setText(String text) {
        _textView.setText(text);
    }
}
