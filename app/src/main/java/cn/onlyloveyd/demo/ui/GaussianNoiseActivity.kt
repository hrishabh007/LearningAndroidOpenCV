package cn.onlyloveyd.demo.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cn.onlyloveyd.demo.R
import cn.onlyloveyd.demo.databinding.ActivityGaussianNoiseBinding
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.MatOfDouble
import org.opencv.imgproc.Imgproc


/**
 * 高斯噪声
 * author: yidong
 * 2020/4/4
 */
class GaussianNoiseActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityGaussianNoiseBinding
    private lateinit var mRgb: Mat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_gaussian_noise)

        val bgr = Utils.loadResource(this, R.drawable.lena)
        mRgb = Mat()
        Imgproc.cvtColor(bgr, mRgb, Imgproc.COLOR_BGR2RGB)
        showMat(mBinding.ivLena, mRgb)
        buildGaussian()

    }


    private fun buildGaussian() {
        val noise = Mat(mRgb.size(), mRgb.type())
        val result = Mat()
        val mean = MatOfDouble()
        val dev = MatOfDouble()

        Core.meanStdDev(mRgb, mean, dev)

        Core.randn(noise, mean[0, 0][0], dev[0, 0][0])
        showMat(mBinding.ivGaussian, noise)
        Core.add(mRgb, noise, result)
        showMat(mBinding.ivResult, result)

        noise.release()
        result.release()
    }

    override fun onDestroy() {
        mRgb.release()
        super.onDestroy()
    }

    private fun showMat(view: ImageView, source: Mat) {
        val bitmap = Bitmap.createBitmap(source.width(), source.height(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(source, bitmap)
        view.setImageBitmap(bitmap)
    }
}