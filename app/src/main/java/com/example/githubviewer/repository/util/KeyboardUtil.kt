package com.example.githubviewer.repository.util

import android.content.Context
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