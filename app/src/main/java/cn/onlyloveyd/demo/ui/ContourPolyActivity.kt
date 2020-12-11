package cn.onlyloveyd.demo.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cn.onlyloveyd.demo.App
import cn.onlyloveyd.demo.R
import cn.onlyloveyd.demo.databinding.ActivityContourPolyBinding
import cn.onlyloveyd.demo.ext.showMat
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.imgproc.Imgproc

/**
 * 轮廓外接多边形
 * author: yidong
 * 2020/10/7
 */
class ContourPolyActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityContourPolyBinding
    private var mSource: Mat = Mat()
    private var mGray: Mat = Mat()
    private var mBinary: Mat = Mat()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_contour_poly)
        mBinding.presenter = this
        val bgr = Utils.loadResource(this, R.drawable.contourpoly)
        Imgproc.cvtColor(bgr, mSource, Imgproc.COLOR_BGR2RGB)
        Imgproc.cvtColor(bgr, mGray, Imgproc.COLOR_BGR2GRAY)
        Imgproc.GaussianBlur(mGray, mGray, Size(5.0, 5.0), 2.0, 2.0)
        Imgproc.threshold(
            mGray,
            mBinary,
            20.0,
            255.0,
            Imgproc.THRESH_BINARY or Imgproc.THRESH_OTSU
        )
        mBinding.ivLena.showMat(mBinary)
    }

    fun findRect(flag: Int) {
        val tmp = mSource.clone()
        val contours = mutableListOf<MatOfPoint>()
        val hierarchy = Mat()
        Imgproc.findContours(
            mBinary,
            contours,
            hierarchy,
            Imgproc.RETR_TREE,
            Imgproc.CHAIN_APPROX_SIMPLE
        )

        for (i in 0 until contours.size) {
            when (flag) {
                0 -> {
                    title = "最大外接矩形"
                    val rect = Imgproc.boundingRect(contours[i])
                    Imgproc.rectangle(tmp, rect, Scalar(255.0, 255.0, 0.0), 4, Imgproc.LINE_8)
                }
                1 -> {
                    title = "最小外接矩形"
                    val source = MatOfPoint2f()
                    source.fromList(contours[i].toList())
                    val rect = Imgproc.minAreaRect(source)
                    val points = arrayOfNulls<Point>(4)
                    val center = rect.center
                    rect.points(points)
                    Log.d(App.TAG, "RotateRect: ${points.toList()}, Center：$center")
                    for (j in 0..3) {
                        Imgproc.line(
                            tmp,
                            points[j % 4],
                            points[(j + 1) % 4],
                            Scalar(255.0, 255.0, 0.0),
                            4,
                            Imgproc.LINE_8
                        )
                    }
                }
                else -> {
                    title = "轮廓多边形"
                    val result = MatOfPoint2f()
                    val source = MatOfPoint2f()
                    source.fromList(contours[i].toList())
                    Imgproc.approxPolyDP(source, result, 4.0, true)
                    Log.d(App.TAG, "Poly: ${result.dump()}")
                    val points = result.toArray()
                    for (j in points.indices) {
                        Imgproc.line(
                            tmp,
                            points[j % points.size],
                            points[(j + 1) % points.size],
                            Scalar(255.0, 255.0, 0.0),
                            4,
                            Imgproc.LINE_8
                        )
                    }
                }
            }
        }
        mBinding.ivResult.showMat(tmp)
        tmp.release()
        hierarchy.release()
    }


    override fun onDestroy() {
        mSource.release()
        mGray.release()
        mBinary.release()
        super.onDestroy()
    }
}