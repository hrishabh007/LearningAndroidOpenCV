package cn.onlyloveyd.demo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cn.onlyloveyd.demo.R
import cn.onlyloveyd.demo.databinding.ActivityHoughLineBinding
import cn.onlyloveyd.demo.ext.showMat
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Scalar
import org.opencv.imgproc.Imgproc
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

/**
 * 霍夫直线检测
 * author: yidong
 * 2020/7/18
 */
class HoughLineDetectActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityHoughLineBinding
    private lateinit var mGray: Mat
    private lateinit var mEdge: Mat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_hough_line)
        mBinding.presenter = this
        mGray = Mat()
        mEdge = Mat()
        val bgr = Utils.loadResource(this, R.drawable.book)
        Imgproc.cvtColor(bgr, mGray, Imgproc.COLOR_BGR2GRAY)
        mBinding.ivLena.showMat(mGray)
        Imgproc.Canny(mGray, mEdge, 80.0, 150.0, 3, false)
    }

    override fun onDestroy() {
        mGray.release()
        mEdge.release()
        super.onDestroy()
    }

    fun doHoughLineDetect() {
        title = "HoughLine"
        val lines = Mat()
        Imgproc.HoughLines(mEdge, lines, 1.0, Math.PI / 180.0, 150)
        val out = Mat.zeros(mGray.size(), mGray.type())
        val data = FloatArray(2)
        for (i in 0 until lines.rows()) {
            lines.get(i, 0, data)
            val rho = data[0] // 直线距离坐标原点的距离
            val theta = data[1] // 直线过坐标原点垂线与x轴夹角
            val a = cos(theta.toDouble())  //夹角的余弦值
            val b = sin(theta.toDouble())  //夹角的正弦值
            val x0 = a * rho  //直线与过坐标原点的垂线的交点
            val y0 = b * rho
            val pt1 = Point()
            val pt2 = Point()
            pt1.x = (x0 + 1000 * (-b)).roundToInt().toDouble()
            pt1.y = (y0 + 1000 * (a)).roundToInt().toDouble()
            pt2.x = (x0 - 1000 * (-b)).roundToInt().toDouble()
            pt2.y = (y0 - 1000 * (a)).roundToInt().toDouble()
            Imgproc.line(out, pt1, pt2, Scalar(255.0, 255.0, 255.0), 2, Imgproc.LINE_AA, 0)
        }
        mBinding.ivResult.showMat(out)
        out.release()
        lines.release()
    }

    fun doHoughLinePDetect() {
        title = "HoughLineP"
        val lines = Mat()
        Imgproc.HoughLinesP(mEdge, lines, 1.0, Math.PI / 180.0, 100, 50.0, 10.0)
        val out = Mat.zeros(mGray.size(), mGray.type())
        for (i in 0 until lines.rows()) {
            val data = IntArray(4)
            lines.get(i, 0, data)
            val pt1 = Point(data[0].toDouble(), data[1].toDouble())
            val pt2 = Point(data[2].toDouble(), data[3].toDouble())
            Imgproc.line(out, pt1, pt2, Scalar(255.0, 255.0, 255.0), 2, Imgproc.LINE_AA, 0)
        }
        mBinding.ivResult.showMat(out)
        out.release()
        lines.release()
    }
}