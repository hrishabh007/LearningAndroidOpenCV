package cn.onlyloveyd.demo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cn.onlyloveyd.demo.R
import cn.onlyloveyd.demo.databinding.ActivityHoughCircleBinding
import cn.onlyloveyd.demo.ext.showMat
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Scalar
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import kotlin.math.roundToInt

/**
 * 霍夫圆检测
 * author: yidong
 * 2020/9/2
 */
class HoughCircleDetectActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityHoughCircleBinding
    private lateinit var mGray: Mat
    private lateinit var mRgb: Mat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_hough_circle)
        mBinding.presenter = this

        mGray = Mat()
        mRgb = Mat()
        val source = Utils.loadResource(this, R.drawable.lotsofcoins)
        Imgproc.cvtColor(source, mGray, Imgproc.COLOR_BGR2GRAY)
        Imgproc.cvtColor(source, mRgb, Imgproc.COLOR_BGR2RGB)
        mBinding.ivLena.showMat(mRgb)
        source.release()
    }

    fun doHoughCircleDetect() {
        val circle = Mat()
        Imgproc.GaussianBlur(mGray, mGray, Size(9.0, 9.0), 2.0, 2.0)

        Imgproc.HoughCircles(
            mGray,
            circle,
            Imgproc.HOUGH_GRADIENT,
            2.0,
            240.0,
            100.0,
            100.0,
            100,
            200
        )

        for (index in 0 until circle.cols()) {
            val content = FloatArray(3)
            circle.get(0, index, content)

            val center =
                Point(content[0].roundToInt().toDouble(), content[1].roundToInt().toDouble())
            val radius = content[2].roundToInt()

            Imgproc.circle(mRgb, center, 3, Scalar(0.0, 255.0, 0.0), -1, 8, 0)
            Imgproc.circle(mRgb, center, radius, Scalar(0.0, 0.0, 255.0), 3, 8, 0)

            mBinding.ivResult.showMat(mRgb)
        }
    }

    override fun onDestroy() {
        mGray.release()
        mRgb.release()
        super.onDestroy()
    }
}