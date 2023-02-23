package com.udacity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var buttonProgress = 0f
    private var arcProgress = 0f
    private val animationDuration =1000L
    private var loadingText = ""
    private var defaultText = ""
    private var mainColor = 0
    private var progressButtonColor = 0
    private var progressArcColor = 0
    private var textColor = 0
    private var valueAnimator = ValueAnimator()
    private var buttonTitle: String

    var buttonState: ButtonState
    by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when(new){
            ButtonState.Clicked -> {}
            ButtonState.Loading ->{
                loadingProgress()
            }
            ButtonState.Completed ->{
                completedProgress()
            }
        }
    }



    private fun loadingProgress() {
        buttonTitle = loadingText
        valueAnimator = ValueAnimator.ofFloat(0f,widthSize.toFloat()).apply {
            duration = animationDuration
            interpolator = LinearInterpolator()
            addUpdateListener {value ->
                buttonProgress = value.animatedValue as Float
                arcProgress = (buttonProgress/1000) * 360f
                invalidate()
            }
            repeatCount = ValueAnimator.INFINITE
        }
        valueAnimator.addListener(object: AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation: Animator) {
                buttonProgress = 0f
                arcProgress = 0f
            }
        })
        valueAnimator.start()
        invalidate()
    }


    private fun completedProgress() {
        valueAnimator.cancel()
        buttonTitle = defaultText
        invalidate()
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.LoadingButton){
            mainColor = getColor(R.styleable.LoadingButton_mainColor,0)
            progressButtonColor = getColor(R.styleable.LoadingButton_progressButtonColor,0)
            progressArcColor = getColor(R.styleable.LoadingButton_progressArcColor,0)
            textColor = getColor(R.styleable.LoadingButton_textColor,0)
            defaultText = getString(R.styleable.LoadingButton_defaultText).toString()
            loadingText = getString(R.styleable.LoadingButton_loadingText).toString()
        }
        buttonTitle = defaultText
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawButton()
        canvas?.drawProgressButton()
        canvas?.drawButtonText()
        canvas?.drawProgressArc()
    }


    private val buttonPaint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = mainColor
        }

    private val progressButtonPaint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = progressButtonColor
        }

    private val buttonTextPaint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = textColor
            textAlign = Paint.Align.CENTER
            textSize = resources.getDimension(R.dimen.default_text_size)
        }

    private val progressArcPaint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = progressArcColor
        }


    private fun Canvas.drawButton() {
        drawRect(0f,0f,widthSize.toFloat(),heightSize.toFloat(),buttonPaint)
    }

    private fun Canvas.drawProgressButton() {
        drawRect(0f,0f,buttonProgress,heightSize.toFloat(),progressButtonPaint)
    }

    private fun Canvas.drawButtonText() {
        drawText(buttonTitle,
            widthSize/2f,
            heightSize/2 - (buttonTextPaint.ascent()+buttonTextPaint.descent())/2,
        buttonTextPaint)
    }

    private fun Canvas.drawProgressArc() {
        val arcRect = RectF(
            widthSize * 0.75f, heightSize * 0.2f,
            widthSize * 0.85f, heightSize * 0.8f
        )
        drawArc(arcRect, 0f, arcProgress, true, progressArcPaint)
    }



    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val min: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(min, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}