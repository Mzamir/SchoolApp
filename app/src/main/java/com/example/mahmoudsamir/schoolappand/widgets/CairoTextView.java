package com.example.mahmoudsamir.schoolappand.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class CairoTextView extends android.support.v7.widget.AppCompatTextView {
    public CairoTextView(Context context) {
        super(context);
        init();
    }

    public CairoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CairoTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Cairo.ttf");
        setTypeface(tf);
    }
}
