package com.seamlabs.BlueRide.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class CairoButton extends android.support.v7.widget.AppCompatButton{

    public CairoButton(Context context) {
        super(context);
        init();
    }

    public CairoButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CairoButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Cairo-Regular.ttf");
        setTypeface(tf);
    }

}