package com.example.mahmoudsamir.schoolappand.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class CairoEditText extends EditText{

    public CairoEditText(Context context) {
        super(context);
        init();
    }

    public CairoEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CairoEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Cairo.ttf");
        setTypeface(tf);
    }

}