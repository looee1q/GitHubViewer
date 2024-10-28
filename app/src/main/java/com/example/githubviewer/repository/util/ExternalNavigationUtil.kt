package com.example.githubviewer.repository.util

import android.content.Context
import android.content.Intent
import android.net.Uri

fun openWebPage(
    context: Context,
    url: String
) {
    val intent = Intent(Intent.ACTION_VIEW).also {
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        it.data = Uri.parse(url)
    }
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    }
}