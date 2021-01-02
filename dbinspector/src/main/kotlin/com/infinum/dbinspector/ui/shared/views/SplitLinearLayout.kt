package com.infinum.dbinspector.ui.shared.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import com.infinum.dbinspector.R;

/**
 * A layout that splits the available space between two child views.
 *
 * An optionally movable bar exists between the children which allows the user
 * to redistribute the space allocated to each view.
 */
public class SplitPaneLayout extends ViewGroup {

    public interface OnSplitterPositionChangedListener {
        void onSplitterPositionChanged(SplitPaneLayout splitPaneLayout, boolean fromUser);
    }

    public static final int ORIENTATION_HORIZONTAL = 0;
    public static final int ORIENTATION_VERTICAL = 1;

    private int mOrientation = 0;
    private int mSplitterSize = 8;
    private boolean mSplitterMovable = true;
    private int mSplitterPosition = Integer.MIN_VALUE;
    private float mSplitterPositionPercent = 0.5f;
    private int mSplitterTouchSlop = 0;

    private int mPaneSizeMin = 0;

    private Drawable mSplitterDrawable;
    private Drawable mSplitterDraggingDrawable;

    private final Rect mSplitterBounds = new Rect();
    private final Rect mSplitterTouchBounds = new Rect();
    private final Rect mSplitterDraggingBounds = new Rect();

    private OnSplitterPositionChangedListener mOnSplitterPositionChangedListener;

    private int lastTouchX;
    private int lastTouchY;

    private boolean isDragging = false;
    private boolean isMovingSplitter = false;

    private boolean isMeasured = false;

    public SplitPaneLayout(Context context) {
        super(context);
        mSplitterPositionPercent = 0.5f;
        mSplitterDrawable = new PaintDrawable(0x88FFFFFF);
        mSplitterDraggingDrawable = new PaintDrawable(0x88FFFFFF);
    }

    public SplitPaneLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        extractAttributes(context, attrs);
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
        setFocusable(true);
        setFocusableInTouchMode(false);
    }

    public SplitPaneLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        extractAttributes(context, attrs);
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
        setFocusable(true);
        setFocusableInTouchMode(false);
    }

    private void extractAttributes(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DbinspectorSplitPaneLayout);
            mOrientation = a.getInt(R.styleable.DbinspectorSplitPaneLayout_dbinspector_orientation, 0);
            mSplitterSize = a.getDimensionPixelSize(R.styleable.DbinspectorSplitPaneLayout_dbinspector_splitterSize, context.getResources().getDimensionPixelSize(R.dimen.dbinspector_default_splitter_size));
            mSplitterMovable = a.getBoolean(R.styleable.DbinspectorSplitPaneLayout_dbinspector_splitterMovable, true);
            TypedValue value = a.peekValue(R.styleable.DbinspectorSplitPaneLayout_dbinspector_splitterPosition);
            if (value != null) {
                if (value.type == TypedValue.TYPE_DIMENSION) {
                    mSplitterPosition = a.getDimensionPixelSize(R.styleable.DbinspectorSplitPaneLayout_dbinspector_splitterPosition, Integer.MIN_VALUE);
                } else if (value.type == TypedValue.TYPE_FRACTION) {
                    mSplitterPositionPercent = a.getFraction(R.styleable.DbinspectorSplitPaneLayout_dbinspector_splitterPosition, 100, 100, 50) * 0.01f;
                }
            } else {
                mSplitterPosition = Integer.MIN_VALUE;
                mSplitterPositionPercent = 0.5f;
            }

            value = a.peekValue(R.styleable.DbinspectorSplitPaneLayout_dbinspector_splitterBackground);
            if (value != null) {
                if (value.type == TypedValue.TYPE_REFERENCE ||
                        value.type == TypedValue.TYPE_STRING) {
                    mSplitterDrawable = a.getDrawable(R.styleable.DbinspectorSplitPaneLayout_dbinspector_splitterBackground);
                } else if (value.type == TypedValue.TYPE_INT_COLOR_ARGB8 ||
                        value.type == TypedValue.TYPE_INT_COLOR_ARGB4 ||
                        value.type == TypedValue.TYPE_INT_COLOR_RGB8 ||
                        value.type == TypedValue.TYPE_INT_COLOR_RGB4) {
                    mSplitterDrawable = new PaintDrawable(a.getColor(R.styleable.DbinspectorSplitPaneLayout_dbinspector_splitterBackground, 0xFF000000));
                }
            }
            value = a.peekValue(R.styleable.DbinspectorSplitPaneLayout_dbinspector_splitterDraggingBackground);
            if (value != null) {
                if (value.type == TypedValue.TYPE_REFERENCE ||
                        value.type == TypedValue.TYPE_STRING) {
                    mSplitterDraggingDrawable = a.getDrawable(R.styleable.DbinspectorSplitPaneLayout_dbinspector_splitterDraggingBackground);
                } else if (value.type == TypedValue.TYPE_INT_COLOR_ARGB8 ||
                        value.type == TypedValue.TYPE_INT_COLOR_ARGB4 ||
                        value.type == TypedValue.TYPE_INT_COLOR_RGB8 ||
                        value.type == TypedValue.TYPE_INT_COLOR_RGB4) {
                    mSplitterDraggingDrawable = new PaintDrawable(a.getColor(R.styleable.DbinspectorSplitPaneLayout_dbinspector_splitterDraggingBackground, 0x88FFFFFF));
                }
            } else {
                mSplitterDraggingDrawable = new PaintDrawable(0x88FFFFFF);
            }
            mSplitterTouchSlop = a.getDimensionPixelSize(R.styleable.DbinspectorSplitPaneLayout_dbinspector_splitterTouchSlop, ViewConfiguration.get(context).getScaledTouchSlop());
            mPaneSizeMin = a.getDimensionPixelSize(R.styleable.DbinspectorSplitPaneLayout_dbinspector_paneSizeMin, 0);
            a.recycle();
        }
    }

    private void computeSplitterPosition() {

        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();

        if (measuredWidth > 0 && measuredHeight > 0) {
            switch (mOrientation) {
                case ORIENTATION_HORIZONTAL: {
                    if (mSplitterPosition == Integer.MIN_VALUE && mSplitterPositionPercent < 0) {
                        mSplitterPosition = measuredWidth / 2;
                    } else if (mSplitterPosition == Integer.MIN_VALUE && mSplitterPositionPercent >= 0) {
                        mSplitterPosition = (int) (measuredWidth * mSplitterPositionPercent);
                        if (!between(mSplitterPosition, getMinSplitterPosition(), getMaxSplitterPosition())) {
                            mSplitterPosition = clamp(mSplitterPosition, getMinSplitterPosition(), getMaxSplitterPosition());
                            mSplitterPositionPercent = (float) mSplitterPosition / (float) measuredWidth;
                        }
                    } else if (mSplitterPosition != Integer.MIN_VALUE && mSplitterPositionPercent < 0) {
                        if (!between(mSplitterPosition, getMinSplitterPosition(), getMaxSplitterPosition())) {
                            mSplitterPosition = clamp(mSplitterPosition, getMinSplitterPosition(), getMaxSplitterPosition());
                        }
                        mSplitterPositionPercent = (float) mSplitterPosition / (float) measuredWidth;
                    }
                    mSplitterBounds.set(mSplitterPosition - (mSplitterSize / 2), 0, mSplitterPosition + (mSplitterSize / 2), measuredHeight);
                    mSplitterTouchBounds.set(mSplitterBounds.left - mSplitterTouchSlop, mSplitterBounds.top, mSplitterBounds.right + mSplitterTouchSlop, mSplitterBounds.bottom);
                    break;
                }
                case ORIENTATION_VERTICAL: {
                    if (mSplitterPosition == Integer.MIN_VALUE && mSplitterPositionPercent < 0) {
                        mSplitterPosition = measuredHeight / 2;
                    } else if (mSplitterPosition == Integer.MIN_VALUE && mSplitterPositionPercent >= 0) {
                        mSplitterPosition = (int) (measuredHeight * mSplitterPositionPercent);
                        if (!between(mSplitterPosition, getMinSplitterPosition(), getMaxSplitterPosition())) {
                            mSplitterPosition = clamp(mSplitterPosition, getMinSplitterPosition(), getMaxSplitterPosition());
                            mSplitterPositionPercent = (float) mSplitterPosition / (float) measuredHeight;
                        }
                    } else if (mSplitterPosition != Integer.MIN_VALUE && mSplitterPositionPercent < 0) {
                        if (!between(mSplitterPosition, getMinSplitterPosition(), getMaxSplitterPosition())) {
                            mSplitterPosition = clamp(mSplitterPosition, getMinSplitterPosition(), getMaxSplitterPosition());
                        }
                        mSplitterPositionPercent = (float) mSplitterPosition / (float) measuredHeight;
                    }
                    mSplitterBounds.set(0, mSplitterPosition - (mSplitterSize / 2), measuredWidth, mSplitterPosition + (mSplitterSize / 2));
                    mSplitterTouchBounds.set(mSplitterBounds.left, mSplitterBounds.top - mSplitterTouchSlop / 2, mSplitterBounds.right, mSplitterBounds.bottom + mSplitterTouchSlop / 2);
                    break;
                }
            }
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();

        check();

        if (measuredWidth > 0 && measuredHeight > 0) {

            computeSplitterPosition();

            switch (mOrientation) {
                case ORIENTATION_HORIZONTAL: {
                    getChildAt(0).measure(MeasureSpec.makeMeasureSpec(mSplitterPosition - (mSplitterSize / 2), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.EXACTLY));
                    getChildAt(1).measure(MeasureSpec.makeMeasureSpec(measuredWidth - (mSplitterSize / 2) - mSplitterPosition, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.EXACTLY));
                    break;
                }
                case ORIENTATION_VERTICAL: {
                    getChildAt(0).measure(MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(mSplitterPosition - (mSplitterSize / 2), MeasureSpec.EXACTLY));
                    getChildAt(1).measure(MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(measuredHeight - (mSplitterSize / 2) - mSplitterPosition, MeasureSpec.EXACTLY));
                    break;
                }
            }

            isMeasured = true;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int w = r - l;
        int h = b - t;
        switch (mOrientation) {
            case ORIENTATION_HORIZONTAL: {
                getChildAt(0).layout(0, 0, mSplitterPosition - (mSplitterSize / 2), h);
                getChildAt(1).layout(mSplitterPosition + (mSplitterSize / 2), 0, r, h);
                break;
            }
            case ORIENTATION_VERTICAL: {
                getChildAt(0).layout(0, 0, w, mSplitterPosition - (mSplitterSize / 2));
                getChildAt(1).layout(0, mSplitterPosition + (mSplitterSize / 2), w, h);
                break;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean remeasure = false;
        int offset = mSplitterSize;
        if (event.isShiftPressed()) {
            offset *= 5;
        }
        switch (mOrientation) {
            case ORIENTATION_HORIZONTAL:
                if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                    mSplitterPosition = clamp(mSplitterPosition - offset, getMinSplitterPosition(), getMaxSplitterPosition());
                    mSplitterPositionPercent = -1;
                    remeasure = true;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    mSplitterPosition = clamp(mSplitterPosition + offset, getMinSplitterPosition(), getMaxSplitterPosition());
                    mSplitterPositionPercent = -1;
                    remeasure = true;
                }
                break;
            case ORIENTATION_VERTICAL:
                if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                    mSplitterPosition = clamp(mSplitterPosition - offset, getMinSplitterPosition(), getMaxSplitterPosition());
                    mSplitterPositionPercent = -1;
                    remeasure = true;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    mSplitterPosition = clamp(mSplitterPosition + offset, getMinSplitterPosition(), getMaxSplitterPosition());
                    mSplitterPositionPercent = -1;
                    remeasure = true;
                }
                break;
        }
        if (remeasure) {
            remeasure();
            notifySplitterPositionChanged(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mSplitterMovable) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    handleTouchDown(x, y);
                    break;
                case MotionEvent.ACTION_MOVE:
                    handleTouchMove(x, y);
                    break;
                case MotionEvent.ACTION_UP:
                    handleTouchUp(x, y);
                    break;
            }
            return true;
        }
        return false;
    }

    private void handleTouchDown(int x, int y) {
        if (mSplitterTouchBounds.contains(x, y)) {
            performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            isDragging = true;
            mSplitterDraggingBounds.set(mSplitterBounds);
            invalidate(mSplitterDraggingBounds);
            lastTouchX = x;
            lastTouchY = y;
        }
    }

    private void handleTouchMove(int x, int y) {
        if (isDragging) {
            if (!isMovingSplitter) {
                // Verify we've moved far enough to leave the touch bounds before moving the splitter
                if (mSplitterTouchBounds.contains(x, y)) {
                    return;
                } else {
                    isMovingSplitter = true;
                }
            }
            boolean take = true;
            switch (mOrientation) {
                case ORIENTATION_HORIZONTAL: {
                    mSplitterDraggingBounds.offset(x - lastTouchX, 0);
                    if (mSplitterDraggingBounds.centerX() < getMinSplitterPosition()) {
                        take = false;
                        mSplitterDraggingBounds.offset(getMinSplitterPosition() - mSplitterDraggingBounds.centerX(), 0);
                    }
                    if (mSplitterDraggingBounds.centerX() > getMaxSplitterPosition()) {
                        take = false;
                        mSplitterDraggingBounds.offset(getMaxSplitterPosition() - mSplitterDraggingBounds.centerX(), 0);
                    }
                    break;
                }
                case ORIENTATION_VERTICAL: {
                    mSplitterDraggingBounds.offset(0, y - lastTouchY);
                    if (mSplitterDraggingBounds.centerY() < getMinSplitterPosition()) {
                        take = false;
                        mSplitterDraggingBounds.offset(0, getMinSplitterPosition() - mSplitterDraggingBounds.centerY());
                    }
                    if (mSplitterDraggingBounds.centerY() > getMaxSplitterPosition()) {
                        take = false;
                        mSplitterDraggingBounds.offset(0, getMaxSplitterPosition() - mSplitterDraggingBounds.centerY());
                    }
                    break;
                }
            }
            if (take) {
                lastTouchX = x;
                lastTouchY = y;
            }
            invalidate();
        }
    }

    private void handleTouchUp(int x, int y) {
        if (isDragging) {
            isDragging = false;
            isMovingSplitter = false;
            switch (mOrientation) {
                case ORIENTATION_HORIZONTAL: {
                    mSplitterPosition = clamp(x, getMinSplitterPosition(), getMaxSplitterPosition());
                    mSplitterPositionPercent = -1;
                    break;
                }
                case ORIENTATION_VERTICAL: {
                    mSplitterPosition = clamp(y, getMinSplitterPosition(), getMaxSplitterPosition());
                    mSplitterPositionPercent = -1;
                    break;
                }
            }
            remeasure();
            notifySplitterPositionChanged(true);
        }
    }

    private int getMinSplitterPosition() {
        return mPaneSizeMin;
    }

    private int getMaxSplitterPosition() {
        switch (mOrientation) {
            case ORIENTATION_HORIZONTAL:
                return getMeasuredWidth() - mPaneSizeMin;
            case ORIENTATION_VERTICAL:
                return getMeasuredHeight() - mPaneSizeMin;
        }
        return 0;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.mSplitterPositionPercent = mSplitterPositionPercent;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setSplitterPositionPercent(ss.mSplitterPositionPercent);
    }

    /**
     * Convenience for calling own measure method.
     */
    private void remeasure() {
        // TODO: Performance: Guard against calling too often, can it be done without requestLayout?
        forceLayout();
        measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY)
        );
        requestLayout();
    }

    /**
     * Checks that we have exactly two children.
     */
    private void check() {
        if (getChildCount() != 2) {
            throw new RuntimeException("SplitPaneLayout must have exactly two child views.");
        }
    }

    private void enforcePaneSizeMin() {

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (isDragging) {
            mSplitterDraggingDrawable.setState(getDrawableState());
            mSplitterDraggingDrawable.setBounds(mSplitterDraggingBounds);
            mSplitterDraggingDrawable.draw(canvas);
        } else {
            if (mSplitterDrawable != null) {
                mSplitterDrawable.setState(getDrawableState());
                mSplitterDrawable.setBounds(mSplitterBounds);
                mSplitterDrawable.draw(canvas);
            }
        }
    }

    /**
     * Gets the current drawable used for the splitter.
     *
     * @return the drawable used for the splitter
     */
    public Drawable getSplitterDrawable() {
        return mSplitterDrawable;
    }

    /**
     * Sets the drawable used for the splitter.
     *
     * @param splitterDrawable the desired orientation of the layout
     */
    public void setSplitterDrawable(Drawable splitterDrawable) {
        mSplitterDrawable = splitterDrawable;
        if (getChildCount() == 2) {
            remeasure();
        }
    }

    /**
     * Gets the current drawable used for the splitter dragging overlay.
     *
     * @return the drawable used for the splitter
     */
    public Drawable getSplitterDraggingDrawable() {
        return mSplitterDraggingDrawable;
    }

    /**
     * Sets the drawable used for the splitter dragging overlay.
     *
     * @param splitterDraggingDrawable the drawable to use while dragging the splitter
     */
    public void setSplitterDraggingDrawable(Drawable splitterDraggingDrawable) {
        mSplitterDraggingDrawable = splitterDraggingDrawable;
        if (isDragging) {
            invalidate();
        }
    }

    /**
     * Gets the current orientation of the layout.
     *
     * @return the orientation of the layout
     */
    public int getOrientation() {
        return mOrientation;
    }

    /**
     * Sets the orientation of the layout.
     *
     * @param orientation the desired orientation of the layout
     */
    public void setOrientation(int orientation) {
        if (mOrientation != orientation) {
            mOrientation = orientation;
            if (getChildCount() == 2) {
                remeasure();
            }
        }
    }

    /**
     * Gets the current size of the splitter in pixels.
     *
     * @return the size of the splitter
     */
    public int getSplitterSize() {
        return mSplitterSize;
    }

    /**
     * Sets the current size of the splitter in pixels.
     *
     * @param splitterSize the desired size of the splitter
     */
    public void setSplitterSize(int splitterSize) {
        mSplitterSize = splitterSize;
        if (getChildCount() == 2) {
            remeasure();
        }
    }

    /**
     * Gets whether the splitter is movable by the user.
     *
     * @return whether the splitter is movable
     */
    public boolean isSplitterMovable() {
        return mSplitterMovable;
    }

    /**
     * Sets whether the splitter is movable by the user.
     *
     * @param splitterMovable whether the splitter is movable
     */
    public void setSplitterMovable(boolean splitterMovable) {
        mSplitterMovable = splitterMovable;
    }

    /**
     * Gets the current position of the splitter in pixels.
     *
     * @return the position of the splitter
     */
    public int getSplitterPosition() {
        return mSplitterPosition;
    }

    /**
     * Sets the current position of the splitter in pixels.
     *
     * @param position the desired position of the splitter
     */
    public void setSplitterPosition(int position) {
        mSplitterPosition = clamp(position, 0, Integer.MAX_VALUE);
        mSplitterPositionPercent = -1;
        remeasure();
        notifySplitterPositionChanged(false);
    }

    /**
     * Gets the current position of the splitter as a percent.
     *
     * @return the position of the splitter
     */
    public float getSplitterPositionPercent() {
        return mSplitterPositionPercent;
    }

    /**
     * Sets the current position of the splitter as a percentage of the layout.
     *
     * @param position the desired position of the splitter
     */
    public void setSplitterPositionPercent(float position) {
        mSplitterPosition = Integer.MIN_VALUE;
        mSplitterPositionPercent = clamp(position, 0, 1);
        remeasure();
        notifySplitterPositionChanged(false);
    }

    /**
     * Gets the current "touch slop" which is used to extends the grab size of the splitter
     * and requires the splitter to be dragged at least this far to be considered a move.
     *
     * @return the current "touch slop" of the splitter
     */
    public int getSplitterTouchSlop() {
        return mSplitterTouchSlop;
    }

    /**
     * Sets the current "touch slop" which is used to extends the grab size of the splitter
     * and requires the splitter to be dragged at least this far to be considered a move.
     *
     * @param splitterTouchSlop the desired "touch slop" of the splitter
     */
    public void setSplitterTouchSlop(int splitterTouchSlop) {
        this.mSplitterTouchSlop = splitterTouchSlop;
        computeSplitterPosition();
    }

    /**
     * Gets the minimum size of panes, in pixels.
     *
     * @return the minimum size of panes, in pixels.
     */
    public int getPaneSizeMin() {
        return mPaneSizeMin;
    }

    /**
     * Sets the minimum size of panes, in pixels.
     *
     * @param paneSizeMin the minimum size of panes, in pixels
     */
    public void setPaneSizeMin(int paneSizeMin) {
        mPaneSizeMin = paneSizeMin;
        if (isMeasured) {
            int newSplitterPosition = clamp(mSplitterPosition, getMinSplitterPosition(), getMaxSplitterPosition());
            if (newSplitterPosition != mSplitterPosition) {
                setSplitterPosition(newSplitterPosition);
            }
        }
    }

    /**
     * Gets the OnSplitterPositionChangedListener to receive callbacks when the splitter position is changed
     *
     * @return the OnSplitterPositionChangedListener to receive callbacks when the splitter position is changed
     */
    public OnSplitterPositionChangedListener getOnSplitterPositionChangedListener() {
        return mOnSplitterPositionChangedListener;
    }

    /**
     * Sets the OnSplitterPositionChangedListener to receive callbacks when the splitter position is changed
     *
     * @param l the OnSplitterPositionChangedListener to receive callbacks when the splitter position is changed
     */
    public void setOnSplitterPositionChangedListener(OnSplitterPositionChangedListener l) {
        this.mOnSplitterPositionChangedListener = l;
    }

    private void notifySplitterPositionChanged(boolean fromUser) {
        if (mOnSplitterPositionChangedListener != null) {
            Log.d("SPL", "Splitter Position Changed");
            mOnSplitterPositionChangedListener.onSplitterPositionChanged(this, fromUser);
        }
    }

    private static float clamp(float value, float min, float max) {
        if (value < min) {
            return min;
        } else if (value > max) {
            return max;
        }
        return value;
    }

    private static int clamp(int value, int min, int max) {
        if (value < min) {
            return min;
        } else if (value > max) {
            return max;
        }
        return value;
    }

    private static boolean between(int value, int min, int max) {
        return min <= value && value <= max;
    }

    /**
     * Holds important values when we need to save instance state.
     */
    public static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }

        };

        float mSplitterPositionPercent;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            mSplitterPositionPercent = in.readFloat();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeFloat(mSplitterPositionPercent);
        }
    }

}
