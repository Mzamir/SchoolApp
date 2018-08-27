package com.seamlabs.BlueRide;

import android.support.v4.app.Fragment;

public class MyFragment extends Fragment {

    public interface onNavigationIconClickListener {
        void onNavigationIconClick();
    }

    onNavigationIconClickListener navigationIconClickListener;

    public onNavigationIconClickListener getNavigationIconClickListener() {
        return navigationIconClickListener;
    }

    public void setNavigationIconClickListener(onNavigationIconClickListener navigationIconClickListener) {
        this.navigationIconClickListener = navigationIconClickListener;
    }
}
