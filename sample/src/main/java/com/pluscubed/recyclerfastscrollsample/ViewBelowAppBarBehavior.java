package com.pluscubed.recyclerfastscrollsample;


import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

public class ViewBelowAppBarBehavior extends CoordinatorLayout.Behavior<View> {

    private static final String TAG = "ViewBelowAppBarBehavior";
    private int mStartHeight = -1;
    private Context mContext;

    public ViewBelowAppBarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean isDirty(CoordinatorLayout parent, View child) {
        return true;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View appBarLayout) {

        if (mStartHeight == -1) {
            mStartHeight = child.getHeight();
        }

        child.setY(appBarLayout.getBottom());

        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        lp.height = mStartHeight - appBarLayout.getBottom() + getStatusBarHeight();
        child.setLayoutParams(lp);

        return true;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");

        if (resourceId > 0) {
            result = mContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
