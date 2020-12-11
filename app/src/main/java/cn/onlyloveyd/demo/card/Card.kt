package cn.onlyloveyd.demo.card

import android.graphics.Bitmap
import android.os.Bundle

class Card constructor(private val name: String, private val bitmap: Bitmap) {

    fun toBundle(): Bundle {
        val args = Bundle(2)
        args.putParcelable(ARG_BITMAP, bitmap)
        args.putString(ARG_NAME, name)
        return args
    }

    companion object {
        val ARG_NAME = "card_name"
        val ARG_BITMAP = "card_bitmap"
    }

    fun recycle() {
        bitmap.recycle()
    }
}