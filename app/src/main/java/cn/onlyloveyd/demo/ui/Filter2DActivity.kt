package cn.onlyloveyd.demo.ui

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cn.onlyloveyd.demo.R
import cn.onlyloveyd.demo.adapter.FilterAdapter
import cn.onlyloveyd.demo.databinding.ActivityFilter2dBinding
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.imgproc.Imgproc

/**
 * 图像卷积
 * author: yidong
 * 2020/3/28
 */
class Filter2DActivity : AppCompatActivity() {
    private lateinit var mGray: Mat
    private lateinit var mBinding: ActivityFilter2dBinding
    private lateinit var mAdapter: FilterAdapter
    private var values = ArrayList<Float>()

    companion object {
        val FILTER_DEFAULT = arrayOf(-1F, -1F, -1F, -1F, 8F, -1F, -1F, 1F, -1F)
        val FILTER_BLUR =
            arrayOf(0.0625F, 0.125F, 0.0625F, 0.125F, 0.25F, 0.125F, 0.0625F, 0.125F, 0.0625F)
        val FILTER_EMBOSS = arrayOf(-2F, -1F, 0F, -1F, 1F, 1F, 0F, 1F, 2F)
        val FILTER_IDENTITY = arrayOf(0F, 0F, 0F, 0F, 1F, 0F, 0F, 0F, 0F)
        val FILTER_OUTLINE = arrayOf(-1F, -1F, -1F, -1F, 8F, -1F, -1F, -1F, -1F)
        val FILTER_SHARPEN =
            arrayOf(0F, -1F, 0F, -1F, 5F, -1F, 0F, -1F, 0F)
        val FILTER_LEFT_SOBEL =
            arrayOf(1F, 0F, -1F, 2F, 0F, -2F, 1F, 0F, -1F)
        val FILTER_RIGHT_SOBEL =
            arrayOf(-1F, 0F, 1F, -2F, 0F, 2F, -1F, 0F, 1F)
        val FILTER_TOP_SOBEL =
            arrayOf(1F, 2F, 1F, 0F, 0F, 0F, -1F, -2F, -1F)
        val FILTER_BOTTOM_SOBEL =
            arrayOf(-1F, -2F, -1F, 0F, 0F, 0F, 1F, 2F, 1F)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_filter2d)
        mBinding.presenter = this
        values.clear()
        values.addAll(FILTER_DEFAULT)
        mAdapter = FilterAdapter(this, values)
        mBinding.kernel.adapter = mAdapter

        val bgr = Utils.loadResource(this, R.drawable.lena)
        mGray = Mat()
        Imgproc.cvtColor(bgr, mGray, Imgproc.COLOR_BGR2GRAY)
        showMat(mBinding.ivLena, mGray)
    }

    override fun onDestroy() {
        mGray.release()
        super.onDestroy()
    }

    fun doFilter() {
        hideKeyboard()
        val kernelArray = FloatArray(9) {
            values[it]
        }
        val kernel = Mat(3, 3, CvType.CV_32FC1)
        kernel.put(0, 0, kernelArray)

        val result = Mat()
        Imgproc.filter2D(
            mGray,
            result,
            -1,
            kernel,
            Point(-1.0, -1.0),
            2.0,
            Core.BORDER_CONSTANT
        )
        showMat(mBinding.ivResult, result)
    }

    private fun showMat(view: ImageView, source: Mat) {
        val bitmap = Bitmap.createBitmap(source.width(), source.height(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(source, bitmap)
        view.setImageBitmap(bitmap)
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            mBinding.ivLena.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_filter2d, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.filter_blur -> {
                mAdapter.setData(FILTER_BLUR)
                doFilter()
            }
            R.id.filter_emboss -> {
                mAdapter.setData(FILTER_EMBOSS)
                doFilter()
            }
            R.id.filter_identity -> {
                mAdapter.setData(FILTER_IDENTITY)
                doFilter()
            }
            R.id.filter_sharpen -> {
                mAdapter.setData(FILTER_SHARPEN)
                doFilter()
            }
            R.id.filter_outline -> {
                mAdapter.setData(FILTER_OUTLINE)
                doFilter()
            }
            R.id.filter_left_sobel -> {
                mAdapter.setData(FILTER_LEFT_SOBEL)
                doFilter()
            }
            R.id.filter_right_sobel -> {
                mAdapter.setData(FILTER_RIGHT_SOBEL)
                doFilter()
            }

            R.id.filter_top_sobel -> {
                mAdapter.setData(FILTER_TOP_SOBEL)
                doFilter()
            }
            R.id.filter_bottom_sobel -> {
                mAdapter.setData(FILTER_BOTTOM_SOBEL)
                doFilter()
            }

        }
        return true
    }
}