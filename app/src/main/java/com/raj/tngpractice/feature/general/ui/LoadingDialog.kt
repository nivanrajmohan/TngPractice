package com.raj.tngpractice.feature.general.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.raj.tngpractice.R

class LoadingDialog : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_loading, container, false);
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            it.setCanceledOnTouchOutside(false)
            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onResume() {
        super.onResume()
        dialog?.setOnKeyListener { _, keyCode, _ ->
            keyCode == KeyEvent.KEYCODE_BACK // block back press
        }
    }

}