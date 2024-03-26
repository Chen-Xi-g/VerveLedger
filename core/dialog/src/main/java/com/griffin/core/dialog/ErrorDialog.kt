package com.griffin.core.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.StyleRes
import androidx.appcompat.widget.AppCompatTextView
import com.griffin.core.utils.runMain

class ErrorDialog(
    context: Context,
    private var title: String = context.getString(R.string.dialog_error_title),
    private var message: String? = null,
    @StyleRes themeResId: Int = R.style.CommonDialog
) : Dialog(context, themeResId) {
    private var tvLoadingText: AppCompatTextView? = null
    private var tvLoadingDesc: AppCompatTextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_error_layout)
        tvLoadingText = findViewById(R.id.dialog_success_text)
        tvLoadingDesc = findViewById(R.id.dialog_success_desc)
        findViewById<AppCompatTextView>(R.id.dialog_success_btn).setOnClickListener {
            dismiss()
        }
        tvLoadingText?.text = title
        if (message.isNullOrBlank()){
            tvLoadingDesc?.visibility = View.GONE
        }else{
            tvLoadingDesc?.visibility = View.VISIBLE
            tvLoadingDesc?.text = message
        }
    }

    override fun show() {
        runMain{
            super.show()
        }
    }

    fun updateText(text: String) {
        runMain {
            tvLoadingText?.text = text
        }
        title = text
    }

    fun updateDesc(desc: String) {
        runMain {
            tvLoadingDesc?.text = desc
        }
        message = desc
    }
}