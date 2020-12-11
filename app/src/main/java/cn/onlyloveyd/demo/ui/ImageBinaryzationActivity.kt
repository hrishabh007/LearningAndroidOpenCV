package cn.onlyloveyd.demo.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cn.onlyloveyd.demo.R
import cn.onlyloveyd.demo.databinding.ActivityImageBinaryzationBinding
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

/**
 * 图像二值化
 * author: yidong
 * 2020/2/12
 */
class ImageBinaryzationActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityImageBinaryzationBinding
    private lateinit var mGray: Mat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_image_binaryzation)
        val bgr = Utils.loadResource(this, R.drawable.lena)
        mGray = Mat()
        Imgproc.cvtColor(bgr, mGray, Imgproc.COLOR_BGR2GRAY)
        showMat(mGray)
        title = "Gray"
        bgr.release()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_image_binaryzation, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        title = item.title
        when (item.itemId) {
            R.id.binary -> binary()
            R.id.binary_inv -> binaryInv()
            R.id.trunc -> trunc()
            R.id.tozero -> toZero()
            R.id.tozero_inv -> toZeroInv()
            R.id.otsu -> otsu()
            R.id.triangle -> triangle()
            R.id.otsu_binary_inv -> otsuBinaryInv()
            R.id.triangle_binary_inv -> triangleBinaryInv()
            R.id.adaptive_mean -> adaptiveMean()
            R.id.adaptive_gaussian -> adaptiveGaussian()
        }
        return true
    }

    private fun showMat(source: Mat) {
        val bitmap = Bitmap.createBitmap(source.width(), source.height(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(source, bitmap)
        mBinding.ivLena.setImageBitmap(bitmap)
    }

    private fun threshold(type: Int) {
        val ret = Mat()
        Imgproc.threshold(mGray, ret, 127.toDouble(), 255.toDouble(), type)
        showMat(ret)
        title = getTypeName(type)
    }

    private fun binary() {
        threshold(Imgproc.THRESH_BINARY)
    }

    private fun binaryInv() {
        threshold(Imgproc.THRESH_BINARY_INV)
    }

    private fun trunc() {
        threshold(Imgproc.THRESH_TRUNC)
    }

    private fun toZero() {
        threshold(Imgproc.THRESH_TOZERO)
    }

    private fun toZeroInv() {
        threshold(Imgproc.THRESH_TOZERO_INV)
    }

    private fun otsu() {
        threshold(Imgproc.THRESH_OTSU)
    }

    private fun triangle() {
        threshold(Imgproc.THRESH_TRIANGLE)
    }

    private fun otsuBinaryInv() {
        threshold(Imgproc.THRESH_OTSU or Imgproc.THRESH_BINARY_INV)
    }

    private fun triangleBinaryInv() {
        threshold(Imgproc.THRESH_TRIANGLE or Imgproc.THRESH_BINARY_INV)
    }

    private fun adaptiveMean() {
        val ret = Mat()
        Imgproc.adaptiveThreshold(
            mGray,
            ret,
            255.toDouble(),
            Imgproc.ADAPTIVE_THRESH_MEAN_C,
            Imgproc.THRESH_BINARY,
            55,
            0.0
        )
        showMat(ret)
        title = "ADAPTIVE_THRESH_MEAN_C"
    }

    private fun adaptiveGaussian() {
        val ret = Mat()
        Imgproc.adaptiveThreshold(
            mGray,
            ret,
            255.toDouble(),
            Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,
            Imgproc.THRESH_BINARY,
            55,
            0.0
        )
        showMat(ret)
        title = "ADAPTIVE_THRESH_GAUSSIAN_C"
    }

    private fun getTypeName(type: Int): String {
        return when (type) {
            0 -> "THRESH_BINARY"
            1 -> "THRESH_BINARY_INV"
            2 -> "THRESH_TRUNC"
            3 -> "THRESH_TOZERO"
            4 -> "THRESH_TOZERO_INV"
            7 -> "THRESH_MASK"
            8 -> "THRESH_OTSU"
            16 -> "THRESH_TRIANGLE"
            Imgproc.THRESH_OTSU or Imgproc.THRESH_BINARY_INV -> "THRESH_OTSU | THRESH_BINARY_INV"
            Imgproc.THRESH_TRIANGLE or Imgproc.THRESH_BINARY_INV -> "THRESH_TRIANGLE | THRESH_BINARY_INV"
            else -> ""
        }
    }
}