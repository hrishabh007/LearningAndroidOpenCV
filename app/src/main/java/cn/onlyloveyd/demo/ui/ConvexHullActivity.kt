package cn.onlyloveyd.demo.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cn.onlyloveyd.demo.App
import cn.onlyloveyd.demo.R
import cn.onlyloveyd.demo.databinding.ActivityConvexHullBinding
import cn.onlyloveyd.demo.ext.showMat
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.imgproc.Imgproc

/**
 * 凸包检测，凸包缺陷
 *
 * @author yidong
 * @date 2020/10/13
 */
class ConvexHullActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityConvexHullBinding
    private lateinit var mSource: Mat
    private lateinit var mGray: Mat


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_convex_hull)
        val bgr = Utils.loadResource(this, R.drawable.star)
        mSource = Mat()
        mGray = Mat()

        Imgproc.cvtColor(bgr, mGray, Imgproc.COLOR_BGR2GRAY)
        Imgproc.cvtColor(bgr, mSource, Imgproc.COLOR_BGR2RGB)
        mBinding.ivLena.showMat(mSource)
        doConvexHull()
    }

    private fun doConvexHull() {
        val binary = Mat()
        Imgproc.threshold(mGray, binary, 25.0, 255.0, Imgproc.THRESH_BINARY)

        val tmp = mSource.clone()
        val contours = mutableListOf<MatOfPoint>()
        val hierarchy = Mat()
        Imgproc.findContours(
            binary,
            contours,
            hierarchy,
            Imgproc.RETR_TREE,
            Imgproc.CHAIN_APPROX_SIMPLE
        )

        for (contour in contours) {
            val hull = MatOfInt()
            val defects = MatOfInt4()
            Imgproc.convexHull(contour, hull)
            val indexList = hull.toList()
            val contourList = contour.toList()
            for (i in 0 until indexList.size) {
                val index = indexList[i % indexList.size]
                val nextIndex = indexList[(i + 1) % indexList.size]
                val point = contourList[index]
                Imgproc.circle(
                    tmp,
                    point,
                    10,
                    Scalar(255.0, 255.0, 0.0),
                    2,
                    Imgproc.LINE_8,
                    0
                )
                Log.d(App.TAG, contourList[i].toString())
                Imgproc.line(
                    tmp,
                    point,
                    contourList[nextIndex],
                    Scalar(255.0, 255.0, 0.0),
                    10,
                    Imgproc.LINE_8,
                    0
                )
            }

            Imgproc.convexityDefects(contour, hull, defects)
            val defectsList = defects.toList()
            for (i in 0 until defectsList.size step 4) {
                val start = contourList[defectsList[i]]
                val end = contourList[defectsList[i + 1]]
                val far = contourList[defectsList[i + 2]]
                Imgproc.line(
                    tmp,
                    start,
                    far,
                    Scalar(0.0, 0.0, 205.0),
                    8,
                    Imgproc.LINE_8,
                    0
                )
                Imgproc.line(
                    tmp,
                    far,
                    end,
                    Scalar(0.0, 0.0, 205.0),
                    8,
                    Imgproc.LINE_8,
                    0
                )

                Imgproc.line(
                    tmp,
                    end,
                    start,
                    Scalar(0.0, 0.0, 205.0),
                    2,
                    Imgproc.LINE_8,
                    0
                )
            }
            mBinding.ivResult.showMat(tmp)
        }

        binary.release()
        hierarchy.release()
        tmp.release()
    }

    override fun onDestroy() {
        mSource.release()
        mGray.release()
        super.onDestroy()
    }
}