package com.example.mahmoudsamir.schoolappand.widgets;

import android.content.Context;
import android.graphics.Typeface;
import EditText;
import android.util.AttributeSet;

public class CairoEditText extends TextInputEditText{

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