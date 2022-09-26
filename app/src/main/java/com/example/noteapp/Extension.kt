package com.example.noteapp

import androidx.fragment.app.Fragment

fun Fragment?.runOnUiTread(action: () -> Unit) {
    this ?: return
    if (!isAdded) return
    activity?.runOnUiThread(action)
}