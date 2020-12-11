package cn.onlyloveyd.demo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cn.onlyloveyd.demo.R
import cn.onlyloveyd.demo.databinding.ActivityFitLineBinding
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.core.Point
import org.opencv.imgproc.Imgproc

/**
 * 直线拟合
 * author: yidong
 * 2020/9/8
 */
class FitLineActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityFitLineBinding
    private val mPoints = MatOfPoint(
        Point(1.0, 21.0),
        Point(2.0, 34.0),
        Point(3.0, 43.0),
        Point(4.0, 67.0),
        Point(5.0, 79.0),
        Point(6.0, 66.0),
        Point(7.0, 67.0),
        Point(8.0, 88.0),
        Point(9.0, 90.0),
        Point(10.0, 100.0)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_fit_line)
        mBinding.presenter = this

        val points = StringBuilder()
        for (point in mPoints.toList()) {
            points.append(point.toString() + "\n")
        }
        mBinding.tvPoints.text = points
    }


    fun doFitLine() {
        val result = Mat()
        Imgproc.fitLine(mPoints, result, Imgproc.DIST_L1, 0.0, 0.00, 0.00)
        val lines = FloatArray(4)
        val tmp = FloatArray(1)
        for (i in 0 until result.rows()) {
            result.get(i, 0, tmp)
            lines[i] = tmp[0]
        }
        val k = lines[1] / lines[0]
        mBinding.tvResult.text = "y=$k(x-${lines[2]})+${lines[3]}"
        result.release()
    }
}