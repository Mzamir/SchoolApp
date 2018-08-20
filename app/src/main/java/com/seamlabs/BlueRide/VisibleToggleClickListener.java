package com.seamlabs.BlueRide;

import android.view.View;

public abstract class VisibleToggleClickListener implements View.OnClickListener {

    private boolean mVisible;

    @Override
    public void onClick(View v) {
        mVisible = !mVisible;
        changeVisibility(mVisible);
    }

    protected abstract void changeVisibility(boolean visible);

}
