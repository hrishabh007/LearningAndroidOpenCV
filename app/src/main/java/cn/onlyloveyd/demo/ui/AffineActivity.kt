package cn.onlyloveyd.demo.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cn.onlyloveyd.demo.R
import cn.onlyloveyd.demo.databinding.ActivityAffineBinding
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint2f
import org.opencv.core.Point
import org.opencv.imgproc.Imgproc

/**
 * 仿射变换
 * author: yidong
 * 2020/3/6
 */
class AffineActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityAffineBinding
    private lateinit var mRgb: Mat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_affine)
        mRgb = Mat()
        val bgr = Utils.loadResource(this, R.drawable.lena)
        Imgproc.cvtColor(bgr, mRgb, Imgproc.COLOR_BGR2RGB)
        bgr.release()
        showMat(mBinding.ivLena, mRgb)
    }


    private fun showMat(view: ImageView, source: Mat) {
        val bitmap = Bitmap.createBitmap(source.width(), source.height(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(source, bitmap)
        view.setImageBitmap(bitmap)
    }

    private fun rotateMatrix(degree: Double, scale: Double) {
        val center = Point(mRgb.width() / 2.0, mRgb.height() / 2.0)
        val matrix = Imgproc.getRotationMatrix2D(center, degree, scale)
        val size = mRgb.size()
        val dst = Mat()
        Imgproc.warpAffine(mRgb, dst, matrix, size)
        showMat(mBinding.ivResult, dst)
        dst.release()
    }

    private fun threePointsMatrix() {
        val srcPoints = arrayOfNulls<Point>(3)
        val dstPoints = arrayOfNulls<Point>(3)

        srcPoints[0] = Point(0.0, 0.0)
        srcPoints[1] = Point(0.0, mRgb.width() - 1.0)
        srcPoints[2] = Point(mRgb.height() - 1.0, mRgb.width() - 1.0)

        dstPoints[0] = Point(mRgb.width() * 0.11, mRgb.width() * 0.2)
        dstPoints[1] = Point(mRgb.width() * 0.15, mRgb.width() * 0.7)
        dstPoints[2] = Point(mRgb.width() * 0.81, mRgb.width() * 0.85)

        val transform = Imgproc.getAffineTransform(
            MatOfPoint2f(srcPoints[0], srcPoints[1], srcPoints[2]),
            MatOfPoint2f(dstPoints[0], dstPoints[1], dstPoints[2])
        )
        val dst = Mat()
        val size = mRgb.size()
        Imgproc.warpAffine(mRgb, dst, transform, size)
        showMat(mBinding.ivResult, dst)
        dst.release()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_affine, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        title = item.title
        when (item.itemId) {
            R.id.affine_rotate_scale -> rotateMatrix(120.0, 1.2)
            R.id.affine_three_points -> threePointsMatrix()
        }
        return true
    }
}