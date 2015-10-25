package com.pluscubed.recyclerfastscroll;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.ColorInt;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class RecyclerFastScroller extends FrameLayout {

    protected final View mBar;
    protected final View mHandle;

    private final Runnable mHide;

    private final int mMinScrollHandleHeight;
    private final int mHiddenTranslationX;
    protected OnTouchListener mOnTouchListener;
    private int mHandleColorNormal;
    private int mHandleColorPressed;
    private int mScrollBarColor;
    private int mTouchTargetWidth;
    private RecyclerView mRecyclerView;
    private AnimatorSet mAnimator;
    private boolean mAnimatingIn;
    private int mBarInset;

    public RecyclerFastScroller(Context context) {
        this(context, null, 0);
    }

    public RecyclerFastScroller(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerFastScroller(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public RecyclerFastScroller(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RecyclerFastScroller, defStyleAttr, defStyleRes);

        mScrollBarColor = a.getColor(
                R.styleable.RecyclerFastScroller_scrollBarColor,
                Utils.resolveColor(context, R.attr.colorControlNormal));

        mHandleColorNormal = a.getColor(
                R.styleable.RecyclerFastScroller_handleColorNormal,
                Utils.resolveColor(context, R.attr.colorControlNormal));

        mHandleColorPressed = a.getColor(
                R.styleable.RecyclerFastScroller_handleColorPressed,
                Utils.resolveColor(context, R.attr.colorAccent));

        mTouchTargetWidth = a.getDimensionPixelSize(R.styleable.RecyclerFastScroller_touchTargetWidth,
                Utils.convertDpToPx(context, 24));

        a.recycle();

        int eightDp = Utils.convertDpToPx(getContext(), 8);
        mBarInset = mTouchTargetWidth - eightDp;

        int fortyEightDp = Utils.convertDpToPx(context, 48);
        setLayoutParams(new ViewGroup.LayoutParams(fortyEightDp, ViewGroup.LayoutParams.MATCH_PARENT));

        if (mTouchTargetWidth > fortyEightDp) {
            throw new RuntimeException("Touch target width cannot be larger than 48dp!");
        }

        mBar = new View(context);
        mHandle = new View(context);
        addView(mBar);
        addView(mHandle);

        setTouchTargetWidth(mTouchTargetWidth);

        mMinScrollHandleHeight = fortyEightDp;

        mHiddenTranslationX = (Utils.isRTL(getContext()) ? -1 : 1) * eightDp;
        mHide = new Runnable() {
            @Override
            public void run() {
                if (!mHandle.isPressed()) {
                    if (mAnimator != null && mAnimator.isStarted()) {
                        mAnimator.cancel();
                    }
                    mAnimator = new AnimatorSet();
                    ObjectAnimator animator2 = ObjectAnimator.ofFloat(RecyclerFastScroller.this, View.TRANSLATION_X,
                            mHiddenTranslationX);
                    animator2.setInterpolator(new FastOutLinearInInterpolator());
                    animator2.setDuration(150);
                    mHandle.setEnabled(false);
                    mAnimator.play(animator2);
                    mAnimator.start();
                }
            }
        };

        mHandle.setOnTouchListener(new OnTouchListener() {
            private float mInitialBarHeight;
            private float mLastPressedYAdjustedToInitial;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mOnTouchListener != null) {
                    mOnTouchListener.onTouch(v, event);
                }
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    mHandle.setPressed(true);

                    mInitialBarHeight = mBar.getHeight();
                    mLastPressedYAdjustedToInitial = event.getY() + mHandle.getY() + mBar.getY();
                } else if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
                    float newHandlePressedY = event.getY() + mHandle.getY() + mBar.getY();
                    int barHeight = mBar.getHeight();
                    float newHandlePressedYAdjustedToInitial =
                            newHandlePressedY + (mInitialBarHeight - barHeight);

                    float deltaPressedYFromLastAdjustedToInitial =
                            newHandlePressedYAdjustedToInitial - mLastPressedYAdjustedToInitial;

                    int dY = (int) ((deltaPressedYFromLastAdjustedToInitial / mInitialBarHeight) *
                            (mRecyclerView.computeVerticalScrollRange()));

                    updateRvScroll(dY);

                    mLastPressedYAdjustedToInitial = newHandlePressedYAdjustedToInitial;
                } else if (event.getActionMasked() == MotionEvent.ACTION_UP) {
                    mLastPressedYAdjustedToInitial = -1;

                    mHandle.setPressed(false);
                    postAutoHide();
                }

                return true;
            }
        });

        setTranslationX(mHiddenTranslationX);
    }

    /**
     * Convenience method, resolves default ?colorControlNormal for {@link #setHandleColor(int, int)}.
     */
    public void setPressedHandleColor(@ColorInt int colorPressed) {
        setHandleColor(colorPressed, Utils.resolveColor(getContext(), R.attr.colorControlNormal));
    }

    public void setHandleColor(@ColorInt int colorPressed, @ColorInt int colorNormal) {
        StateListDrawable drawable = new StateListDrawable();

        if (!Utils.isRTL(getContext())) {
            drawable.addState(View.PRESSED_ENABLED_STATE_SET,
                    new InsetDrawable(new ColorDrawable(colorPressed), mBarInset, 0, 0, 0));
            drawable.addState(View.EMPTY_STATE_SET,
                    new InsetDrawable(new ColorDrawable(colorNormal), mBarInset, 0, 0, 0));
        } else {
            drawable.addState(View.PRESSED_ENABLED_STATE_SET,
                    new InsetDrawable(new ColorDrawable(colorPressed), 0, 0, mBarInset, 0));
            drawable.addState(View.EMPTY_STATE_SET,
                    new InsetDrawable(new ColorDrawable(colorNormal), 0, 0, mBarInset, 0));
        }
        Utils.setViewBackground(mHandle, drawable);
    }

    public void setBarColor(@ColorInt int scrollBarColor) {
        Drawable drawable;

        if (!Utils.isRTL(getContext())) {
            drawable = new InsetDrawable(new ColorDrawable(scrollBarColor), mBarInset, 0, 0, 0);
        } else {
            drawable = new InsetDrawable(new ColorDrawable(scrollBarColor), 0, 0, mBarInset, 0);
        }
        drawable.setAlpha(57);
        Utils.setViewBackground(mBar, drawable);
    }

    /**
     * @param touchTargetWidth Largest touch target width is 48dp
     */
    public void setTouchTargetWidth(int touchTargetWidth) {
        mTouchTargetWidth = touchTargetWidth;

        mBar.setLayoutParams(new LayoutParams(touchTargetWidth, ViewGroup.LayoutParams.MATCH_PARENT, GravityCompat.END));
        mHandle.setLayoutParams(new LayoutParams(touchTargetWidth, ViewGroup.LayoutParams.MATCH_PARENT, GravityCompat.END));

        setHandleColor(mHandleColorPressed, mHandleColorNormal);
        setBarColor(mScrollBarColor);
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        initRecyclerViewOnScrollListener();
    }

    public void setOnHandleTouchListener(OnTouchListener listener) {
        mOnTouchListener = listener;
    }

    private void initRecyclerViewOnScrollListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerFastScroller.this.onScrolled();
            }
        });
    }

    private void onScrolled() {
        requestLayout();

        mHandle.setEnabled(true);
        if (!mAnimatingIn && getTranslationX() != 0) {
            if (mAnimator != null && mAnimator.isStarted()) {
                mAnimator.cancel();
            }
            mAnimator = new AnimatorSet();
            ObjectAnimator animator = ObjectAnimator.ofFloat(RecyclerFastScroller.this, View.TRANSLATION_X, 0);
            animator.setInterpolator(new LinearOutSlowInInterpolator());
            animator.setDuration(100);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mAnimatingIn = false;
                }
            });
            mAnimatingIn = true;
            mAnimator.play(animator);
            mAnimator.start();
        }
        postAutoHide();
    }

    private void postAutoHide() {
        if (mRecyclerView != null) {
            mRecyclerView.removeCallbacks(mHide);
            mRecyclerView.postDelayed(mHide, 1500);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        int scrollOffset = mRecyclerView.computeVerticalScrollOffset();
        int verticalScrollRange = mRecyclerView.computeVerticalScrollRange()
                + mRecyclerView.getPaddingBottom();

        int barHeight = mBar.getHeight();
        float ratio = (float) scrollOffset / (verticalScrollRange - barHeight);

        int calculatedHandleHeight = (int) ((float) barHeight / verticalScrollRange * barHeight);
        if (calculatedHandleHeight < mMinScrollHandleHeight) {
            calculatedHandleHeight = mMinScrollHandleHeight;
        }

        if (calculatedHandleHeight >= barHeight) {
            setTranslationX(mHiddenTranslationX);
            return;
        }

        float y = ratio * (barHeight - calculatedHandleHeight);

        mHandle.layout(mHandle.getLeft(), (int) y, mHandle.getRight(), (int) y + calculatedHandleHeight);
    }

    private void updateRvScroll(int dY) {
        if (mRecyclerView != null && mHandle != null) {
            try {
                mRecyclerView.scrollBy(0, dY);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
}