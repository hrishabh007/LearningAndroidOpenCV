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
import org.opencv.imgproc.Imgproc

/**
 * Scharr算子-边缘检测
 *
 * @author yidong
 * @date 2020-05-17
 */
class ScharrEdgeDetectionActivity : AppCompatActivity() {


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

    private fun showMat(view: ImageView, source: Mat) {
        val bitmap = Bitmap.createBitmap(source.width(), source.height(), Bitmap.Config.ARGB_8888)
        bitmap.density = 360
        Utils.matToBitmap(source, bitmap)
        view.setImageBitmap(bitmap)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_scharr, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.scharr_edge_detection_x -> {
                edgeDetectionX()
            }
            R.id.scharr_edge_detection_y -> {
                edgeDetectionY()
            }
            R.id.scharr_edge_detection_x_y -> {
                edgeDetectionXAndY()
            }
        }
        return true
    }

    private fun edgeDetectionX() {
        title = "X轴方向边缘检测"
        val resultX = Mat()
        Imgproc.Scharr(mRgb, resultX, CvType.CV_16S, 1, 0)
        Core.convertScaleAbs(resultX, resultX)
        showMat(mBinding.ivResult, resultX)
    }


    private fun edgeDetectionY() {
        title = "Y轴方向边缘检测"
        val resultY = Mat()
        Imgproc.Scharr(mRgb, resultY, CvType.CV_16S, 0, 1)
        Core.convertScaleAbs(resultY, resultY)
        showMat(mBinding.ivResult, resultY)
    }

    private fun edgeDetectionXAndY() {
        title = "X和Y轴方向边缘检测"
        val resultX = Mat()
        Imgproc.Scharr(mRgb, resultX, CvType.CV_16S, 1, 0)
        Core.convertScaleAbs(resultX, resultX)
        showMat(mBinding.ivResult, resultX)
        val resultY = Mat()
        Imgproc.Scharr(mRgb, resultY, CvType.CV_16S, 0, 1)
        Core.convertScaleAbs(resultY, resultY)
        showMat(mBinding.ivResult, resultY)

        val resultXY = Mat()
        Core.add(resultX, resultY, resultXY)
        showMat(mBinding.ivResult, resultXY)

        resultX.release()
        resultY.release()
        resultXY.release()
    }
}