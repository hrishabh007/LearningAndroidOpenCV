package cn.onlyloveyd.demo.ui

import cn.onlyloveyd.demo.R
import cn.onlyloveyd.demo.ext.CardGalleryActivity
import org.opencv.android.Utils
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc

/**
 * 方框滤波
 * author: yidong
 * 2020/4/18
 */
class BoxFilterActivity : CardGalleryActivity() {
    override fun buildCards() {
        val bgr = Utils.loadResource(this, R.drawable.test)
        val rgb = Mat()
        Imgproc.cvtColor(bgr, rgb, Imgproc.COLOR_BGR2RGB)
        addCardFromMat("原图", rgb)

        val resultNormalize = Mat()
        Imgproc.boxFilter(
            rgb,
            resultNormalize,
            -1,
            Size(9.0, 9.0),
            Point(-1.0, -1.0),
            true
        )
        addCardFromMat("boxFilter归一化", resultNormalize)
        resultNormalize.release()

        val resultUnNormalize = Mat()
        Imgproc.boxFilter(rgb, resultUnNormalize, -1, Size(3.0, 3.0), Point(-1.0, -1.0), false)
        addCardFromMat("boxFilter未归一化", resultUnNormalize)
        resultUnNormalize.release()

        val sqResult = Mat()
        Imgproc.sqrBoxFilter(rgb, sqResult, CvType.CV_8U, Size(3.0, 3.0), Point(-1.0, -1.0), true)
        addCardFromMat("sqrBoxFilter归一化", sqResult)

        val sqResultUnNormalizer = Mat()
        Imgproc.sqrBoxFilter(
            rgb,
            sqResultUnNormalizer,
            CvType.CV_8U,
            Size(3.0, 3.0),
            Point(-1.0, -1.0),
            false
        )
        addCardFromMat("sqrBoxFilter未归一化", sqResultUnNormalizer)

        bgr.release()
        rgb.release()
    }
}