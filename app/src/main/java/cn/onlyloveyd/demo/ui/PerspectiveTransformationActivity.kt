package cn.onlyloveyd.demo.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cn.onlyloveyd.demo.R
import cn.onlyloveyd.demo.databinding.ActivityPerspectiveTransformationBinding
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import org.opencv.utils.Converters

/**
 * 透视变换
 * author: yidong
 * 2020/3/3
 */
class PerspectiveTransformationActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityPerspectiveTransformationBinding
    private lateinit var mRgb: Mat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_perspective_transformation)

        val bgr = Utils.loadResource(this, R.drawable.road)
        mRgb = Mat()
        Imgproc.cvtColor(bgr, mRgb, Imgproc.COLOR_BGR2RGB)
        showMat(mBinding.ivRoad, mRgb)

        doPerspectiveTransform()
    }

    private fun showMat(view: ImageView, source: Mat) {
        val bitmap = Bitmap.createBitmap(source.width(), source.height(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(source, bitmap)
        view.setImageBitmap(bitmap)
    }

    private fun getPerspectiveTransform(): Mat {
        val srcPoints = ArrayList<Point>()
        val p1 = Point(325.0000, 374.0000)
        srcPoints.add(p1)
        val p2 = Point(597.0000, 374.0000)
        srcPoints.add(p2)
        val p3 = Point(960.0000, 505.0000)
        srcPoints.add(p3)
        val p0 = Point(0.0000, 505.0000)
        srcPoints.add(p0)
        val srcMat = Converters.vector_Point2f_to_Mat(srcPoints)

        val resultWidth = 500.0
        val resultHeight = 500.0
        val dstPoints = ArrayList<Point>()
        val p5 = Point(0.0, 0.0)
        dstPoints.add(p5)
        val p6 = Point(resultWidth, 0.0)
        dstPoints.add(p6)
        val p7 = Point(resultWidth, resultHeight)
        dstPoints.add(p7)
        val p4 = Point(0.0, resultHeight)
        dstPoints.add(p4)
        val dstMat = Converters.vector_Point2f_to_Mat(dstPoints)
        return Imgproc.getPerspectiveTransform(srcMat, dstMat)
    }

    private fun doPerspectiveTransform() {
        val transform = getPerspectiveTransform()
        val dst = Mat()
        Imgproc.warpPerspective(
            mRgb,
            dst,
            transform,
            Size(500.0, 500.0),
            Imgproc.INTER_NEAREST
        )
        showMat(mBinding.ivResult, dst)
    }

    override fun onDestroy() {
        mRgb.release()
        super.onDestroy()
    }
}