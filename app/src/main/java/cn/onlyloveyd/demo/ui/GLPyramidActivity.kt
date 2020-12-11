package cn.onlyloveyd.demo.ui

import android.graphics.Bitmap
import cn.onlyloveyd.demo.R
import cn.onlyloveyd.demo.card.Card
import cn.onlyloveyd.demo.ext.CardGalleryActivity
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

/**
 * 高斯金字塔，拉普拉斯金字塔
 * author: yidong
 * 2020/3/14
 */
class GLPyramidActivity : CardGalleryActivity() {

    override fun buildCards() {
        val bgr = Utils.loadResource(this, R.drawable.lena)
        val rgb = Mat()
        Imgproc.cvtColor(bgr, rgb, Imgproc.COLOR_BGR2RGB)
        bgr.release()
        buildGauss(rgb)
        rgb.release()
    }

    private fun buildGauss(source: Mat) {
        val gaussList = arrayListOf<Mat>()
        gaussList.add(source)
        for (i in 0..2) {
            val gauss = Mat()
            Imgproc.pyrDown(gaussList[i], gauss)
            gaussList.add(gauss)
        }

        for (i in gaussList.indices) {
            val bitmap = Bitmap.createBitmap(
                gaussList[i].width(),
                gaussList[i].height(),
                Bitmap.Config.ARGB_8888
            )
            Utils.matToBitmap(gaussList[i], bitmap)
            cards.add(Card("Gauss${i}", bitmap))
        }
        buildLaplace(gaussList)
    }

    private fun buildLaplace(gaussList: List<Mat>) {
        val laplaceList = arrayListOf<Mat>()
        for (i in gaussList.size - 1 downTo 1) {
            val lap = Mat()
            val upGauss = Mat()
            if (i == gaussList.size - 1) {
                val down = Mat()
                Imgproc.pyrDown(gaussList[i], down)
                Imgproc.pyrUp(down, upGauss)
                Core.subtract(gaussList[i], upGauss, lap)
                laplaceList.add(lap.clone())
            }
            Imgproc.pyrUp(gaussList[i], upGauss)
            Core.subtract(gaussList[i - 1], upGauss, lap)
            laplaceList.add(lap.clone())
        }

        for (i in laplaceList.indices) {
            val bitmap = Bitmap.createBitmap(
                laplaceList[i].width(),
                laplaceList[i].height(),
                Bitmap.Config.ARGB_8888
            )
            Utils.matToBitmap(laplaceList[i], bitmap)
            cards.add(Card("Laplace${i}", bitmap))
        }

        for (gauss in gaussList) {
            gauss.release()
        }
        for (lap in laplaceList) {
            lap.release()
        }
    }
}