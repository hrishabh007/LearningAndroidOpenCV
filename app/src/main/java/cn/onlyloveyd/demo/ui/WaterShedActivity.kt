package cn.onlyloveyd.demo.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import cn.onlyloveyd.demo.App
import cn.onlyloveyd.demo.R
import cn.onlyloveyd.demo.databinding.ActivityWaterShedBinding
import cn.onlyloveyd.demo.ext.setInvisible
import cn.onlyloveyd.demo.ext.setVisible
import cn.onlyloveyd.demo.ext.showMat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.opencv.android.Utils
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.core.Scalar
import org.opencv.imgproc.Imgproc
import kotlin.random.Random

/**
 * 图像分割--分水岭法
 * author: yidong
 * 2020/11/9
 */
class WaterShedActivity : AppCompatActivity() {

    private val mBinding: ActivityWaterShedBinding by lazy {
        ActivityWaterShedBinding.inflate(
            layoutInflater
        )
    }
    private lateinit var mRgb: Mat
    private lateinit var mGray: Mat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)

        mRgb = Mat()
        mGray = Mat()
        val bgr = Utils.loadResource(this, R.drawable.contourpoly)
        Imgproc.cvtColor(bgr, mRgb, Imgproc.COLOR_BGR2RGB)
        Imgproc.cvtColor(bgr, mGray, Imgproc.COLOR_BGR2GRAY)

        mBinding.ivLena.showMat(mGray)

        GlobalScope.launch(Dispatchers.IO) {
            doWaterShed()
        }
    }

    private fun doWaterShed() {
        mBinding.progressBar.setVisible()
        val markers = Mat(
            mRgb.size(),
            CvType.CV_32S,
            Scalar.all(0.0)
        )
//        Imgproc.GaussianBlur(mGray, mGray, Size(13.0, 13.0), 4.0, 4.0
        val binary = Mat()
        Imgproc.threshold(
            mGray,
            binary,
            20.0,
            255.0,
            Imgproc.THRESH_BINARY and Imgproc.THRESH_OTSU
        )
        mBinding.ivResult.showMat(binary)
        val contours = mutableListOf<MatOfPoint>()
        val hierarchy = Mat()
        Imgproc.findContours(
            binary,
            contours,
            hierarchy,
            Imgproc.RETR_TREE,
            Imgproc.CHAIN_APPROX_SIMPLE
        )

        for (i in 0 until contours.size) {
            Imgproc.drawContours(
                markers,
                contours,
                i,
                Scalar.all(i + 1.toDouble()),
                -1,
                Imgproc.LINE_8,
                hierarchy,
                Int.MAX_VALUE
            )
        }

        Imgproc.watershed(mRgb, markers)

        val colors = mutableListOf<DoubleArray>()
        for (k in 0 until contours.size) {
            val r = Random.nextInt(0, 255)
            val g = Random.nextInt(0, 255)
            val b = Random.nextInt(0, 255)
            val scalar = doubleArrayOf(r.toDouble(), g.toDouble(), b.toDouble())
            colors.add(scalar)
        }

        val resultImg = Mat(mGray.size(), CvType.CV_8UC3)
        for (i in 0 until markers.rows()) {
            for (j in 0 until markers.cols()) {
                val index = markers.get(i, j)[0].toInt()

                if (index == -1) {                                    // -1:区域之间的分割线用-1表示
                    resultImg.put(i, j, 255.0, 255.0, 255.0)
                    Log.d(App.TAG, " i= $i, j=$j")
                } else if (index <= 0 || (index > contours.size)) {   //  <0 or >size:未标记区域
                    resultImg.put(i, j, 0.0, 0.0, 0.0)
                } else {                                              // 0,1,2,3... size-1: 标记区域
                    resultImg.put(
                        i,
                        j,
                        colors[index - 1][0],
                        colors[index - 1][1],
                        colors[index - 1][2]
                    )
                }
            }
        }
        mBinding.progressBar.setInvisible()
        mBinding.ivResult.showMat(resultImg)

        markers.release()
        resultImg.release()
    }

    override fun onDestroy() {
        mGray.release()
        mRgb.release()
        super.onDestroy()
    }
}