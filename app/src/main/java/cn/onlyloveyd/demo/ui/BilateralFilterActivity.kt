package cn.onlyloveyd.demo.ui

import cn.onlyloveyd.demo.R
import cn.onlyloveyd.demo.ext.CardGalleryActivity
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

/**
 * 双边滤波
 * author: yidong
 * 2020/5/1
 */
class BilateralFilterActivity : CardGalleryActivity() {

    override fun buildCards() {
        val bgr = Utils.loadResource(this, R.drawable.timg)
        val source = Mat()
        Imgproc.cvtColor(bgr, source, Imgproc.COLOR_BGR2RGB)
        bgr.release()
        addCardFromMat("原图", source, 240)
        bilateralFilter(source)
        source.release()
    }

    private fun bilateralFilter(source: Mat) {
        val dst9 = Mat()
        Imgproc.bilateralFilter(source, dst9, 9, 10.0, 10.0)
        addCardFromMat("双边滤波 直径9X9 标准差10", dst9, 240)
        dst9.release()

        val dst25 = Mat()
        Imgproc.bilateralFilter(source, dst25, 25, 10.0, 10.0)
        addCardFromMat("双边滤波 直径25X25 标准差10", dst25, 240)
        dst25.release()

        val shift9 = Mat()
        Imgproc.bilateralFilter(source, shift9, 25, 100.0, 100.0)
        addCardFromMat("双边滤波 直径25X25 标准差值100", shift9, 240)
        shift9.release()

        val shift25 = Mat()
        Imgproc.bilateralFilter(source, shift25, 25, 200.0, 200.0)
        addCardFromMat("双边滤波 直径25X25 标准差值200", shift25, 240)
        shift25.release()
    }
}