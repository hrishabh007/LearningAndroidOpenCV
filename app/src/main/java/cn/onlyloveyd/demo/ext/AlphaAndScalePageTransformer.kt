package cn.onlyloveyd.demo.ext

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs


class AlphaAndScalePageTransformer : ViewPager2.PageTransformer {
    private val SCALE_MAX = 0.8f
    private val ALPHA_MAX = 0.5f

    override fun transformPage(page: View, position: Float) {
        val scale =
            if (position < 0) (1 - SCALE_MAX) * position + 1 else (SCALE_MAX - 1) * position + 1
        val alpha =
            if (position < 0) (1 - ALPHA_MAX) * position + 1 else (ALPHA_MAX - 1) * position + 1
        if (position < 0) {
            page.pivotX = page.width.toFloat()
            page.pivotY = (page.height / 2).toFloat()
        } else {
            page.pivotX = 0F
            page.pivotY = (page.height / 2).toFloat()
        }
        page.scaleX = scale
        page.scaleY = scale
        page.alpha = abs(alpha)
    }
}