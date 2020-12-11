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
 * Canny边缘检测
 *
 * @author yidong
 * @date 2020-05-18
 */
class CannyEdgeDetectionActivity : AppCompatActivity() {

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
        menuInflater.inflate(R.menu.menu_canny, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.low_canny_edge_detection -> {
                lowCannyEdgeDetection()
            }
            R.id.high_canny_edge_detection -> {
                highCannyEdgeDetection()
            }
            R.id.blur_canny_edge_detection -> {
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

    private fun lowCannyEdgeDetection() {
        title = "低阈值Canny边缘检测"
        val result = Mat()
        Imgproc.Canny(mRgb, result, 20.0, 40.0, 3)
        showMat(mBinding.ivResult, result)
    }


    private fun highCannyEdgeDetection() {
        title = "高阈值Canny边缘检测"
        val result = Mat()
        Imgproc.Canny(mRgb, result, 100.0, 200.0, 3)
        showMat(mBinding.ivResult, result)
    }

    private fun edgeDetectionAfterBlur() {
        title = "滤波后Canny边缘检测"
        val resultG = Mat()
        val result = Mat()
        Imgproc.GaussianBlur(mRgb, resultG, Size(3.0, 3.0), 5.0)
        Imgproc.Canny(resultG, result, 100.0, 200.0, 3)
        showMat(mBinding.ivResult, result)
    }

}