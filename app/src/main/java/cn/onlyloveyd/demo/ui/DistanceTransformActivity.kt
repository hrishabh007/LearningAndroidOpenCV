package cn.onlyloveyd.demo.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cn.onlyloveyd.demo.R
import cn.onlyloveyd.demo.databinding.ActivityDistanceTransformBinding
import org.opencv.android.Utils
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

/**
 * 图像距离变换
 * author: yidong
 * 2020/6/13
 */
class DistanceTransformActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityDistanceTransformBinding
    private lateinit var mBinary: Mat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_distance_transform)

        val bgr = Utils.loadResource(this, R.drawable.number)
        val gray = Mat()
        Imgproc.cvtColor(bgr, gray, Imgproc.COLOR_BGR2GRAY)
        mBinary = Mat()
        //区域内像素用零表示
        Imgproc.threshold(gray, mBinary, 50.0, 255.0, Imgproc.THRESH_BINARY_INV)
        showMat(mBinding.ivLena, mBinary)
        bgr.release()
        gray.release()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_distance_transform, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        title = item.title
        when (item.itemId) {
            R.id.dt_l1 -> {
                doDistanceTransform(Imgproc.DIST_L1)
            }
            R.id.dt_l2 -> {
                doDistanceTransform(Imgproc.DIST_L2)
            }
            R.id.dt_c -> {
                doDistanceTransform(Imgproc.DIST_C)
            }

        }
        return true
    }

    private fun showMat(view: ImageView, source: Mat) {
        val bitmap = Bitmap.createBitmap(source.width(), source.height(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(source, bitmap)
        view.setImageBitmap(bitmap)
    }

    private fun doDistanceTransform(flag: Int) {
        val dst = Mat()
        // 计算每个二值图像像素到最近的零像素的近似或精确距离。对于零图像像素，距离显然是零。
        Imgproc.distanceTransform(mBinary, dst, flag, 5, CvType.CV_32S)
        dst.convertTo(dst, CvType.CV_8U)
        showMat(mBinding.ivResult, dst)
        dst.release()
    }
}


