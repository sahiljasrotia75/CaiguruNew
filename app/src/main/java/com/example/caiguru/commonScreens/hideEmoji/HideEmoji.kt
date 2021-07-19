package com.example.caiguru.commonScreens.hideEmoji

import android.content.Context
import android.text.InputFilter
import android.text.Spanned


class HideEmoji(context: Context) : InputFilter {
    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        for (i in start until end) {
            val type = Character.getType(source[i])
            if (type == Character.SURROGATE.toInt() || type == Character.OTHER_SYMBOL.toInt()) {
                return ""
            }
        }
        return null
    }
}


//set these text simple in activity
//edtName.filters = arrayOf<InputFilter>(HideEmoji(this))