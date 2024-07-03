package com.kuma.viewcomponents.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class KumaImageView:View {
    companion object {
        const val ratio = 16 / 9f
    }

    lateinit var mBitmap: Bitmap
    var resultHeight = 100f
    var dstRect = RectF(0f, 500 / 2f, 200.toFloat(), 500 / 2 + resultHeight)
    val paint = Paint()

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    fun setBitmap(bitmap: Bitmap) {
        this.mBitmap = bitmap
        postInvalidate()
    }

    override fun onDraw(canvas: Canvas) {
        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, null, dstRect, paint)
        }
        super.onDraw(canvas)
    }

    var firstDownY = 0
    var clickedOnce = false
    var incrementScale = 1f
    var isCountingDown = false
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!clickedOnce) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    Log.d("PVTest", "first down")
                    firstDownY = event.y.toInt()
                }
                MotionEvent.ACTION_MOVE -> {
                    Log.d("PVTest", "first move ,Y: ${event.y.toInt()}")

                }
                MotionEvent.ACTION_UP -> {
                    clickedOnce = true
                    Log.d("PVTest", "first up")
                    Thread {
                        isCountingDown = true
                        Thread.sleep(1000)
                        if (isCountingDown) {
                            clickedOnce = false
                        }
                    }.start()
                    return true
                }
            }
        }

        if (clickedOnce) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    isCountingDown = false
                    Log.d("PVTest", "second down")
                }
                MotionEvent.ACTION_MOVE -> {
//                    if (!clickerOnce) clickerOnce = true
                    incrementScale = (event.y - firstDownY) / 100 + 1
                    Log.d("PVTest", "scale: $incrementScale")
                    zoom(incrementScale)
                }
                MotionEvent.ACTION_UP -> {
                    Log.d("PVTest", "second up")
                    clickedOnce = false
                    if (incrementScale > 0) {
                        resultHeight *= incrementScale
                    }
                    incrementScale = 1f
                }
            }
        }
        return true
    }

    private fun zoom(scale: Float) {
        if (scale < 0) return

        dstRect =
            RectF(
                0f, 500 / 2f,
                resultHeight * scale * ratio,
                500 / 2f + resultHeight * scale
            )
        Log.d("PVTest", dstRect.toString())
        invalidate()
    }
}