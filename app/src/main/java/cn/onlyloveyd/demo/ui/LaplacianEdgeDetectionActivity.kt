package cn.onlyloveyd.demo.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cn.onlyloveyd.demo.R
import cn.onlyloveyd.demo.databinding.ActivityEdgeDetectionBinding
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc

/**
 * Laplacian算子-边缘检测
 *
 * @author yidong
 * @date 2020-05-18
 */
class LaplacianEdgeDetectionActivity : AppCompatActivity() {


    private lateinit var mBinding: ActivityEdgeDetectionBinding
    private lateinit var mRgb: Mat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_edge_detection)
        val bgr = Utils.loadResource(this, R.drawable.lena)
        mRgb = Mat()
        Imgproc.cvtColor(bgr, mRgb, Imgproc.COLOR_BGR2RGB)
        showMat(mBinding.ivLena, mRgb)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_laplacian, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.without_blur_edge_detection -> {
                edgeDetection()
            }
            R.id.with_blur_edge_detection -> {
                edgeDetectionAfterBlur()
            }
        }
        return true
    }

    private fun showMat(view: ImageView, source: Mat) {
        val bitmap = Bitmap.createBitmap(source.width(), source.height(), Bitmap.Config.ARGB_8888)
        bitmap.density = 360
        Utils.matToBitmap(source, bitmap)
        view.setImageBitmap(bitmap)
    }

    private fun edgeDetection() {
        title = "Laplacian边缘检测"
        val result = Mat()
        Imgproc.Laplacian(mRgb, result, CvType.CV_16S, 3, 1.0, 0.0)
        Core.convertScaleAbs(result, result)
        showMat(mBinding.ivResult, result)
    }

    private fun edgeDetectionAfterBlur() {
        title = "高斯滤波后Laplacian边缘检测"
        val resultG = Mat()
        val result = Mat()
        Imgproc.GaussianBlur(mRgb, resultG, Size(3.0, 3.0), 5.0, 0.0)
        Imgproc.Laplacian(resultG, result, CvType.CV_16S, 3, 1.0, 0.0)
        Core.convertScaleAbs(result, result)
        showMat(mBinding.ivResult, result)
    }
}