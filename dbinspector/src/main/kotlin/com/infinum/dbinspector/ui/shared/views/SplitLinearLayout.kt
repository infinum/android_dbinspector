@file:Suppress("unused", "LongMethod", "ComplexMethod", "NestedBlockDepth", "TooManyFunctions")

package com.infinum.dbinspector.ui.shared.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.PaintDrawable
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.HapticFeedbackConstants
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.IntDef
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import com.infinum.dbinspector.R

/**
 * A layout that splits the available space between two child views.
 *
 * A movable bar exists between exactly 2s  children which allows the user to redistribute
 * the space allocated to each view.
 */
class SplitLinearLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ViewGroup(context, attrs, defStyle) {

    companion object {

        @IntDef(HORIZONTAL, VERTICAL)
        @Retention(AnnotationRetention.SOURCE)
        annotation class OrientationMode

        const val HORIZONTAL = 0
        const val VERTICAL = 1

        @ColorRes
        private val DEFAULT_SPLITTER = R.color.dbinspector_splitter_background

        @ColorRes
        private val DEFAULT_SPLITTER_DRAGGABLE = R.color.dbinspector_splitter_dragging_background

        private const val DEFAULT_SPLITTER_POSITION = Int.MIN_VALUE
        private const val DEFAULT_SPLITTER_POSITION_PERCENTAGE = 0.5f

        private const val SHIFT_OFFSET = 5
    }

    /**
     * Control whether the splitter is movable by the user.
     */
    var isSplitterMovable = true

    /**
     * Drawable used for the splitter.
     */
    var splitterDrawable: Drawable
        get() = currentSplitterDrawable
        set(splitterDrawable) {
            currentSplitterDrawable = splitterDrawable
            if (childCount == 2) {
                forceMeasure()
            }
        }

    /**
     * Drawable used for the splitter dragging overlay.
     */
    var splitterDraggingDrawable: Drawable
        get() = currentSplitterDraggingDrawable
        set(splitterDraggingDrawable) {
            currentSplitterDraggingDrawable = splitterDraggingDrawable
            if (isDragging) {
                invalidate()
            }
        }

    /**
     * The current orientation of the layout.
     */
    @OrientationMode
    var orientation: Int
        get() = currentOrientation
        set(orientation) {
            if (currentOrientation != orientation) {
                currentOrientation = orientation
                if (childCount == 2) {
                    forceMeasure()
                }
            }
        }

    /**
     * Current size of the splitter in pixels.
     */
    var splitterSize: Int
        get() = currentSplitterSize
        set(splitterSize) {
            currentSplitterSize = splitterSize
            if (childCount == 2) {
                forceMeasure()
            }
        }

    /**
     * Current position of the splitter in pixels.
     */
    var splitterPosition: Int
        get() = currentSplitterPosition
        set(position) {
            currentSplitterPosition = position.coerceIn(0, Int.MAX_VALUE)
            currentSplitterPositionPercent = -1f
            forceMeasure()
            notifySplitterPositionChanged(false)
        }

    /**
     * Current position of the splitter as a percentage of the layout.
     */
    var splitterPositionPercent: Float
        get() = currentSplitterPositionPercent
        set(position) {
            currentSplitterPosition = DEFAULT_SPLITTER_POSITION
            currentSplitterPositionPercent = position.coerceIn(0f, 1f)
            forceMeasure()
            notifySplitterPositionChanged(false)
        }

    /**
     * Current "touch slop" which is used to extends the grab size of the splitter
     * and requires the splitter to be dragged at least this far to be considered a move.
     */
    var splitterTouchSlop: Int
        get() = currentSplitterTouchSlop
        set(splitterTouchSlop) {
            currentSplitterTouchSlop = splitterTouchSlop
            computeSplitterPosition()
        }

    /**
     * Minimum size of panes, in pixels.
     */
    var paneSizeMin: Int
        get() = minSplitterPosition
        set(paneSizeMin) {
            minSplitterPosition = paneSizeMin
            if (isMeasured) {
                val newSplitterPosition = currentSplitterPosition.coerceIn(minSplitterPosition, maxSplitterPosition)
                if (newSplitterPosition != currentSplitterPosition) {
                    splitterPosition = newSplitterPosition
                }
            }
        }

    /**
     * OnSplitterPositionChangedListener to receive callbacks when the splitter position is changed
     */
    var onSplitterPositionChangedListener: OnSplitterPositionChangedListener? = null

    private val maxSplitterPosition: Int
        get() {
            when (currentOrientation) {
                HORIZONTAL -> return measuredWidth - minSplitterPosition
                VERTICAL -> return measuredHeight - minSplitterPosition
            }
            return 0
        }

    @OrientationMode
    private var currentOrientation = HORIZONTAL
    private var currentSplitterSize = context.resources.getDimensionPixelSize(R.dimen.dbinspector_default_splitter_size)
    private var currentSplitterPosition = DEFAULT_SPLITTER_POSITION
    private var currentSplitterPositionPercent = DEFAULT_SPLITTER_POSITION_PERCENTAGE
    private var currentSplitterTouchSlop = 0
    private var currentSplitterDrawable: Drawable = PaintDrawable(
        ContextCompat.getColor(context, DEFAULT_SPLITTER)
    )
    private var currentSplitterDraggingDrawable: Drawable = PaintDrawable(
        ContextCompat.getColor(context, DEFAULT_SPLITTER_DRAGGABLE)
    )
    private val currentSplitterBounds = Rect()
    private val currentSplitterTouchBounds = Rect()
    private val currentSplitterDraggingBounds = Rect()

    private var minSplitterPosition = 0
    private var lastTouchX = 0
    private var lastTouchY = 0
    private var isDragging = false
    private var isMovingSplitter = false
    private var isMeasured = false

    init {
        parseAttributes(attrs)
        descendantFocusability = FOCUS_AFTER_DESCENDANTS
        isFocusable = true
        isFocusableInTouchMode = false
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val measuredWidth = measuredWidth
        val measuredHeight = measuredHeight
        checkChildCount()
        if (measuredWidth > 0 && measuredHeight > 0) {
            computeSplitterPosition()
            when (currentOrientation) {
                HORIZONTAL -> {
                    getChildAt(0).measure(
                        MeasureSpec.makeMeasureSpec(
                            currentSplitterPosition - currentSplitterSize / 2,
                            MeasureSpec.EXACTLY
                        ),
                        MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.EXACTLY)
                    )
                    getChildAt(1).measure(
                        MeasureSpec.makeMeasureSpec(
                            measuredWidth - currentSplitterSize / 2 - currentSplitterPosition,
                            MeasureSpec.EXACTLY
                        ),
                        MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.EXACTLY)
                    )
                }
                VERTICAL -> {
                    getChildAt(0).measure(
                        MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(
                            currentSplitterPosition - currentSplitterSize / 2,
                            MeasureSpec.EXACTLY
                        )
                    )
                    getChildAt(1).measure(
                        MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(
                            measuredHeight - currentSplitterSize / 2 - currentSplitterPosition,
                            MeasureSpec.EXACTLY
                        )
                    )
                }
            }
            isMeasured = true
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val w = r - l
        val h = b - t
        when (currentOrientation) {
            HORIZONTAL -> {
                getChildAt(0).layout(
                    0,
                    0,
                    currentSplitterPosition - currentSplitterSize / 2,
                    h
                )
                getChildAt(1).layout(
                    currentSplitterPosition + currentSplitterSize / 2,
                    0,
                    r,
                    h
                )
            }
            VERTICAL -> {
                getChildAt(0).layout(
                    0,
                    0,
                    w,
                    currentSplitterPosition - currentSplitterSize / 2
                )
                getChildAt(1).layout(
                    0,
                    currentSplitterPosition + currentSplitterSize / 2,
                    w,
                    h
                )
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        var forceMeasure = false
        var offset = currentSplitterSize
        if (event.isShiftPressed) {
            offset *= SHIFT_OFFSET
        }
        when (currentOrientation) {
            HORIZONTAL -> if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                currentSplitterPosition = (currentSplitterPosition - offset)
                    .coerceIn(minSplitterPosition, maxSplitterPosition)
                currentSplitterPositionPercent = -1f
                forceMeasure = true
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                currentSplitterPosition = (currentSplitterPosition + offset)
                    .coerceIn(minSplitterPosition, maxSplitterPosition)
                currentSplitterPositionPercent = -1f
                forceMeasure = true
            }
            VERTICAL -> if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                currentSplitterPosition = (currentSplitterPosition - offset)
                    .coerceIn(minSplitterPosition, maxSplitterPosition)
                currentSplitterPositionPercent = -1f
                forceMeasure = true
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                currentSplitterPosition = (currentSplitterPosition + offset)
                    .coerceIn(minSplitterPosition, maxSplitterPosition)
                currentSplitterPositionPercent = -1f
                forceMeasure = true
            }
        }
        if (forceMeasure) {
            forceMeasure()
            notifySplitterPositionChanged(true)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean =
        if (isSplitterMovable) {
            val x = event.x.toInt()
            val y = event.y.toInt()
            when (event.action) {
                MotionEvent.ACTION_DOWN -> handleTouchDown(x, y)
                MotionEvent.ACTION_MOVE -> handleTouchMove(x, y)
                MotionEvent.ACTION_UP -> handleTouchUp(x, y)
            }
            true
        } else {
            false
        }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        when (isDragging) {
            true -> with(currentSplitterDraggingDrawable) {
                state = drawableState
                bounds = currentSplitterDraggingBounds
                draw(canvas)
            }
            false -> with(currentSplitterDrawable) {
                state = drawableState
                bounds = currentSplitterBounds
                draw(canvas)
            }
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        val savedState = SavedState(superState)
        savedState.splitterPositionPercent = currentSplitterPositionPercent
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }
        super.onRestoreInstanceState(state.superState)
        splitterPositionPercent = state.splitterPositionPercent
    }

    @Suppress("MagicNumber")
    private fun parseAttributes(attrs: AttributeSet?) =
        context.withStyledAttributes(attrs, R.styleable.DbinspectorSplitLinearLayout) {
            currentOrientation = getInt(
                R.styleable.DbinspectorSplitLinearLayout_dbinspector_orientation,
                HORIZONTAL
            )
            currentSplitterSize = getDimensionPixelSize(
                R.styleable.DbinspectorSplitLinearLayout_dbinspector_splitterSize,
                context.resources.getDimensionPixelSize(R.dimen.dbinspector_default_splitter_size)
            )
            isSplitterMovable = getBoolean(
                R.styleable.DbinspectorSplitLinearLayout_dbinspector_splitterMovable,
                true
            )
            currentSplitterTouchSlop = getDimensionPixelSize(
                R.styleable.DbinspectorSplitLinearLayout_dbinspector_splitterTouchSlop,
                ViewConfiguration.get(context).scaledTouchSlop
            )
            minSplitterPosition = getDimensionPixelSize(
                R.styleable.DbinspectorSplitLinearLayout_dbinspector_paneSizeMin,
                0
            )

            peekValue(R.styleable.DbinspectorSplitLinearLayout_dbinspector_splitterPosition)?.let {
                when (it.type) {
                    TypedValue.TYPE_DIMENSION -> currentSplitterPosition = getDimensionPixelSize(
                        R.styleable.DbinspectorSplitLinearLayout_dbinspector_splitterPosition,
                        Int.MIN_VALUE
                    )
                    TypedValue.TYPE_FRACTION -> currentSplitterPositionPercent = getFraction(
                        R.styleable.DbinspectorSplitLinearLayout_dbinspector_splitterPosition,
                        100,
                        100,
                        DEFAULT_SPLITTER_POSITION_PERCENTAGE * 100.0f
                    ) * 0.01f
                    else -> currentSplitterPositionPercent = DEFAULT_SPLITTER_POSITION_PERCENTAGE
                }
            }

            peekValue(R.styleable.DbinspectorSplitLinearLayout_dbinspector_splitterBackground)?.let {
                when (it.type) {
                    TypedValue.TYPE_REFERENCE,
                    TypedValue.TYPE_STRING ->
                        getDrawable(R.styleable.DbinspectorSplitLinearLayout_dbinspector_splitterBackground)
                            ?.let { drawable -> currentSplitterDrawable = drawable }
                    TypedValue.TYPE_INT_COLOR_ARGB8,
                    TypedValue.TYPE_INT_COLOR_ARGB4,
                    TypedValue.TYPE_INT_COLOR_RGB8,
                    TypedValue.TYPE_INT_COLOR_RGB4 ->
                        currentSplitterDrawable = PaintDrawable(
                            getColor(R.styleable.DbinspectorSplitLinearLayout_dbinspector_splitterBackground,
                                ContextCompat.getColor(context, DEFAULT_SPLITTER))
                        )
                    else ->
                        currentSplitterDrawable = PaintDrawable(
                            ContextCompat.getColor(context, DEFAULT_SPLITTER)
                        )
                }
            }

            peekValue(R.styleable.DbinspectorSplitLinearLayout_dbinspector_splitterDraggingBackground)?.let {
                when (it.type) {
                    TypedValue.TYPE_REFERENCE,
                    TypedValue.TYPE_STRING ->
                        getDrawable(R.styleable.DbinspectorSplitLinearLayout_dbinspector_splitterDraggingBackground)
                            ?.let { drawable -> currentSplitterDraggingDrawable = drawable }
                    TypedValue.TYPE_INT_COLOR_ARGB8,
                    TypedValue.TYPE_INT_COLOR_ARGB4,
                    TypedValue.TYPE_INT_COLOR_RGB8,
                    TypedValue.TYPE_INT_COLOR_RGB4 ->
                        currentSplitterDraggingDrawable = PaintDrawable(
                            getColor(R.styleable.DbinspectorSplitLinearLayout_dbinspector_splitterDraggingBackground,
                                ContextCompat.getColor(context, DEFAULT_SPLITTER_DRAGGABLE))
                        )
                    else -> currentSplitterDraggingDrawable =
                        PaintDrawable(ContextCompat.getColor(context, DEFAULT_SPLITTER_DRAGGABLE))
                }
            }
        }

    private fun computeSplitterPosition() {
        val measuredWidth = measuredWidth
        val measuredHeight = measuredHeight
        if (measuredWidth > 0 && measuredHeight > 0) {
            when (currentOrientation) {
                HORIZONTAL -> {
                    if (currentSplitterPosition == Int.MIN_VALUE && currentSplitterPositionPercent < 0) {
                        currentSplitterPosition = measuredWidth / 2
                    } else if (currentSplitterPosition == Int.MIN_VALUE && currentSplitterPositionPercent >= 0) {
                        currentSplitterPosition = (measuredWidth * currentSplitterPositionPercent).toInt()
                        if (currentSplitterPosition !in minSplitterPosition..maxSplitterPosition) {
                            currentSplitterPosition = currentSplitterPosition
                                .coerceIn(minSplitterPosition, maxSplitterPosition)
                            currentSplitterPositionPercent = currentSplitterPosition.toFloat() / measuredWidth.toFloat()
                        }
                    } else if (currentSplitterPosition != Int.MIN_VALUE && currentSplitterPositionPercent < 0) {
                        if (currentSplitterPosition !in minSplitterPosition..maxSplitterPosition) {
                            currentSplitterPosition = currentSplitterPosition
                                .coerceIn(minSplitterPosition, maxSplitterPosition)
                        }
                        currentSplitterPositionPercent = currentSplitterPosition.toFloat() / measuredWidth.toFloat()
                    }
                    currentSplitterBounds[
                        currentSplitterPosition - currentSplitterSize / 2,
                        0,
                        currentSplitterPosition + currentSplitterSize / 2
                    ] = measuredHeight
                    currentSplitterTouchBounds[
                        currentSplitterBounds.left - currentSplitterTouchSlop,
                        currentSplitterBounds.top,
                        currentSplitterBounds.right + currentSplitterTouchSlop
                    ] = currentSplitterBounds.bottom
                }
                VERTICAL -> {
                    if (currentSplitterPosition == Int.MIN_VALUE && currentSplitterPositionPercent < 0) {
                        currentSplitterPosition = measuredHeight / 2
                    } else if (currentSplitterPosition == Int.MIN_VALUE && currentSplitterPositionPercent >= 0) {
                        currentSplitterPosition = (measuredHeight * currentSplitterPositionPercent).toInt()
                        if (currentSplitterPosition !in minSplitterPosition..maxSplitterPosition) {
                            currentSplitterPosition = currentSplitterPosition
                                .coerceIn(minSplitterPosition, maxSplitterPosition)
                            currentSplitterPositionPercent =
                                currentSplitterPosition.toFloat() / measuredHeight.toFloat()
                        }
                    } else if (currentSplitterPosition != Int.MIN_VALUE && currentSplitterPositionPercent < 0) {
                        if (currentSplitterPosition !in minSplitterPosition..maxSplitterPosition) {
                            currentSplitterPosition = currentSplitterPosition
                                .coerceIn(minSplitterPosition, maxSplitterPosition)
                        }
                        currentSplitterPositionPercent = currentSplitterPosition.toFloat() / measuredHeight.toFloat()
                    }
                    currentSplitterBounds[
                        0,
                        currentSplitterPosition - currentSplitterSize / 2,
                        measuredWidth
                    ] = currentSplitterPosition + currentSplitterSize / 2
                    currentSplitterTouchBounds[
                        currentSplitterBounds.left,
                        currentSplitterBounds.top - currentSplitterTouchSlop / 2,
                        currentSplitterBounds.right
                    ] = currentSplitterBounds.bottom + currentSplitterTouchSlop / 2
                }
            }
        }
    }

    private fun handleTouchDown(x: Int, y: Int) {
        if (currentSplitterTouchBounds.contains(x, y)) {
            performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            isDragging = true
            currentSplitterDraggingBounds.set(currentSplitterBounds)
            invalidate()
            lastTouchX = x
            lastTouchY = y
        }
    }

    private fun handleTouchMove(x: Int, y: Int) {
        if (isDragging) {
            if (isMovingSplitter.not()) {
                isMovingSplitter = when {
                    currentSplitterTouchBounds.contains(x, y) -> return
                    else -> true
                }
            }
            var take = true
            when (currentOrientation) {
                HORIZONTAL -> {
                    currentSplitterDraggingBounds.offset(x - lastTouchX, 0)
                    if (currentSplitterDraggingBounds.centerX() < minSplitterPosition) {
                        take = false
                        currentSplitterDraggingBounds.offset(
                            minSplitterPosition - currentSplitterDraggingBounds.centerX(),
                            0
                        )
                    }
                    if (currentSplitterDraggingBounds.centerX() > maxSplitterPosition) {
                        take = false
                        currentSplitterDraggingBounds.offset(
                            maxSplitterPosition - currentSplitterDraggingBounds.centerX(),
                            0
                        )
                    }
                }
                VERTICAL -> {
                    currentSplitterDraggingBounds.offset(0, y - lastTouchY)
                    if (currentSplitterDraggingBounds.centerY() < minSplitterPosition) {
                        take = false
                        currentSplitterDraggingBounds.offset(
                            0,
                            minSplitterPosition - currentSplitterDraggingBounds.centerY()
                        )
                    }
                    if (currentSplitterDraggingBounds.centerY() > maxSplitterPosition) {
                        take = false
                        currentSplitterDraggingBounds.offset(
                            0,
                            maxSplitterPosition - currentSplitterDraggingBounds.centerY()
                        )
                    }
                }
            }
            if (take) {
                lastTouchX = x
                lastTouchY = y
            }
            invalidate()
        }
    }

    private fun handleTouchUp(x: Int, y: Int) {
        if (isDragging) {
            isDragging = false
            isMovingSplitter = false
            when (currentOrientation) {
                HORIZONTAL -> {
                    currentSplitterPosition = x.coerceIn(minSplitterPosition, maxSplitterPosition)
                    currentSplitterPositionPercent = -1f
                }
                VERTICAL -> {
                    currentSplitterPosition = y.coerceIn(minSplitterPosition, maxSplitterPosition)
                    currentSplitterPositionPercent = -1f
                }
            }
            forceMeasure()
            notifySplitterPositionChanged(true)
        }
    }

    private fun forceMeasure() {
        forceLayout()
        measure(
            MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.EXACTLY)
        )
        requestLayout()
    }

    private fun checkChildCount() {
        if (childCount != 2) {
            throw IllegalStateException("SplitLinearLayout must have exactly 2 child views.")
        }
    }

    private fun notifySplitterPositionChanged(fromUser: Boolean) {
        onSplitterPositionChangedListener?.onSplitterPositionChanged(this, fromUser)
    }

    interface OnSplitterPositionChangedListener {
        fun onSplitterPositionChanged(splitLinearLayout: SplitLinearLayout, fromUser: Boolean)
    }

    /**
     * Holds important values when we need to save instance state.
     */
    class SavedState : BaseSavedState {

        companion object {

            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {

                override fun createFromParcel(parcel: Parcel): SavedState = SavedState(parcel)

                override fun newArray(size: Int): Array<SavedState?> = arrayOfNulls(size)
            }
        }

        var splitterPositionPercent = 0.0f

        internal constructor(superState: Parcelable?) : super(superState)

        private constructor(parcel: Parcel) : super(parcel) {
            splitterPositionPercent = parcel.readFloat()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeFloat(splitterPositionPercent)
        }
    }
}
