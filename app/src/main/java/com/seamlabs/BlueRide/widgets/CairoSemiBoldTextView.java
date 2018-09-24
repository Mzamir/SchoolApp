package com.seamlabs.BlueRide.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class CairoSemiBoldTextView extends android.support.v7.widget.AppCompatTextView {
    public CairoSemiBoldTextView(Context context) {
        super(context);
        init();
    }

    public CairoSemiBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CairoSemiBoldTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Cairo-SemiBold.ttf");
        setTypeface(tf);
    }
}
