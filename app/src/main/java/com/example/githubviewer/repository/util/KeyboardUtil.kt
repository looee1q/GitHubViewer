package com.example.githubviewer.repository.util

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

fun View.hideKeyboard() {
    val inputMethodManager = context.getSystemService(
        Context.INPUT_METHOD_SERVICE
    ) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

fun EditText.setOnIMEActionDoneListener(
    onIMEActionDoneListener: () -> Unit
) {
    setOnEditorActionListener { v, actionId, event ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            onIMEActionDoneListener()
            return@setOnEditorActionListener true
        }
        return@setOnEditorActionListener false
    }
}

fun setKeyboardVisibilityListener(
    parentView: View,
    onKeyboardShown: () -> Unit,
    onKeyboardHidden: () -> Unit
) {
    var alreadyOpen = false
    val rect = Rect()
    val estimatedKeyboardDP = 148f

    parentView.viewTreeObserver.addOnGlobalLayoutListener {
        val estimatedKeyboardHeight = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            estimatedKeyboardDP,
            parentView.resources.displayMetrics
        )
        parentView.getWindowVisibleDisplayFrame(rect)
        val heightDiff = parentView.rootView.height - rect.height()
        val isShown = heightDiff >= estimatedKeyboardHeight

        if (isShown == alreadyOpen) {
            return@addOnGlobalLayoutListener
        }
        alreadyOpen = isShown

        if (isShown) {
            onKeyboardShown()
        } else {
            onKeyboardHidden()
        }
    }
}