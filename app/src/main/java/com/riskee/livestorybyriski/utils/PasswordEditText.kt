package com.riskee.livestorybyriski.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.riskee.livestorybyriski.R

class PasswordEditText : AppCompatEditText, View.OnTouchListener {

    private lateinit var hideButtonImage: Drawable
    private var isPasswordVisible: Boolean = false

    fun isValidPassword() = error == null && text.toString().isNotBlank()

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = "Enter Password"
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun init() {
        hideButtonImage =
            ContextCompat.getDrawable(context, R.drawable.baseline_visibility_off_24) as Drawable
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                error = if (s.length < 8) {
                    "Password must be at least 8 characters long"
                } else {
                    null
                }
                if (s.toString().isNotEmpty()) {
                    showHideButton()
                } else {
                    hideHideButton()
                }
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
    }

    private fun showHideButton() {
        setButtonDrawables(endOfTheText = hideButtonImage)
    }

    private fun hideHideButton() {
        setButtonDrawables()
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesRelativeWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            null,
            bottomOfTheText
        )
        setCompoundDrawablesRelativeWithIntrinsicBounds(
            null,
            null,
            endOfTheText,
            null
        )
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val hideButtonStart: Float
            val hideButtonEnd: Float
            var isHideButtonClicked = false
            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                hideButtonEnd = (hideButtonImage.intrinsicWidth + paddingStart).toFloat()
                when {
                    event.x < hideButtonEnd -> isHideButtonClicked = true
                }
            } else {
                hideButtonStart = (width - paddingEnd - hideButtonImage.intrinsicWidth).toFloat()
                when {
                    event.x > hideButtonStart -> isHideButtonClicked = true
                }
            }
            if (isHideButtonClicked) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        hideButtonImage = ContextCompat.getDrawable(
                            context,
                            R.drawable.baseline_visibility_off_24
                        ) as Drawable
                        showHideButton()
                        showPassword(false)
                        return true
                    }

                    MotionEvent.ACTION_UP -> {
                        isPasswordVisible = !isPasswordVisible
                        hideButtonImage = if (isPasswordVisible) {
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.baseline_visibility_24
                            ) as Drawable
                        } else {
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.baseline_visibility_off_24
                            ) as Drawable
                        }
                        showPassword(isPasswordVisible)
                        return true
                    }

                    else -> return false
                }
            } else return false
        }
        return false
    }

    private fun showPassword(show: Boolean) {
        transformationMethod = if (show) null else PasswordTransformationMethod.getInstance()
    }
}
