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
 * 边缘检测
 * author: yidong
 * 2020/5/2
 */
class EdgeDetectionActivity : AppCompatActivity() {

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
        menuInflater.inflate(R.menu.menu_edge_detection, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edge_detection_x -> {
                edgeDetectionX()
            }
            R.id.edge_detection_y -> {
                edgeDetectionY()
            }
            R.id.edge_detection_x_y -> {
                edgeDetectionXAndY()
            }
            R.id.edge_detection_xy -> {
                edgeDetectionXY()
            }
            R.id.edge_detection_yx -> {
                edgeDetectionYX()
            }
        }
        return true
    }

    private fun edgeDetectionX() {
        title = "X轴方向边缘检测"
        // X轴方向边缘检测
        val kernelX = Mat(1, 3, CvType.CV_16S)
        val arrayX = shortArrayOf(-1, 0, 1)
        kernelX.put(0, 0, arrayX)
        val resultKernelX = Mat()
        Imgproc.filter2D(mRgb, resultKernelX, CvType.CV_16S, kernelX)
        Core.convertScaleAbs(resultKernelX, resultKernelX)
        showMat(mBinding.ivResult, resultKernelX)
        kernelX.release()
        resultKernelX.release()
    }

    private fun edgeDetectionY() {
        title = "Y轴方向边缘检测"
        // Y轴方向边缘检测
        val kernelY = Mat(3, 1, CvType.CV_16S)
        val arrayY = shortArrayOf(-1, 0, 1)
        kernelY.put(0, 0, arrayY)
        val resultKernelY = Mat()
        Imgproc.filter2D(mRgb, resultKernelY, CvType.CV_16S, kernelY)
        Core.convertScaleAbs(resultKernelY, resultKernelY)
        showMat(mBinding.ivResult, resultKernelY)
        kernelY.release()
        resultKernelY.release()
    }

    private fun edgeDetectionXAndY() {
        title = "X和Y轴方向边缘检测"
        // X轴方向边缘检测
        val kernelX = Mat(1, 3, CvType.CV_16S)
        val arrayX = shortArrayOf(-1, 0, 1)
        kernelX.put(0, 0, arrayX)
        val resultKernelX = Mat()
        Imgproc.filter2D(mRgb, resultKernelX, CvType.CV_16S, kernelX)
        Core.convertScaleAbs(resultKernelX, resultKernelX)
        // Y轴方向边缘检测
        val kernelY = Mat(3, 1, CvType.CV_16S)
        val arrayY = shortArrayOf(-1, 0, 1)
        kernelY.put(0, 0, arrayY)
        val resultKernelY = Mat()
        Imgproc.filter2D(mRgb, resultKernelY, CvType.CV_16S, kernelY)
        Core.convertScaleAbs(resultKernelY, resultKernelY)

        // X,Y轴方向合并
        val resultXY = Mat()
        Core.add(resultKernelX, resultKernelY, resultXY)
        showMat(mBinding.ivResult, resultXY)

        kernelX.release()
        resultKernelX.release()
        kernelY.release()
        resultKernelY.release()
        resultXY.release()
    }

    private fun edgeDetectionXY() {
        title = "由左上到右下方向边缘检测"
        //由左上到右下方向边缘检
        val kernelXY = Mat(2, 2, CvType.CV_16S)
        val arrayXY = shortArrayOf(1, 0, 0, -1)
        kernelXY.put(0, 0, arrayXY)
        val resultKernelXY = Mat()
        Imgproc.filter2D(mRgb, resultKernelXY, CvType.CV_16S, kernelXY)
        Core.convertScaleAbs(resultKernelXY, resultKernelXY)
        showMat(mBinding.ivResult, resultKernelXY)
        kernelXY.release()
        resultKernelXY.release()
    }

    private fun edgeDetectionYX() {
        title = "由右上到左下方向边缘检测"
        //由右上到左下方向边缘检
        val kernelYX = Mat(2, 2, CvType.CV_16S)
        val arrayYX = shortArrayOf(0, -1, 1, 0)
        kernelYX.put(0, 0, arrayYX)
        val resultKernelYX = Mat()
        Imgproc.filter2D(mRgb, resultKernelYX, CvType.CV_16S, kernelYX)
        Core.convertScaleAbs(resultKernelYX, resultKernelYX)
        showMat(mBinding.ivResult, resultKernelYX)
        kernelYX.release()
        resultKernelYX.release()
    }
}