package cn.onlyloveyd.demo.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cn.onlyloveyd.demo.R
import cn.onlyloveyd.demo.databinding.ActivityGaussianFilterBinding
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc

/**
 * 高斯滤波
 * author: yidong
 * 2020/4/25
 */
class GaussianFilterActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityGaussianFilterBinding
    private lateinit var mRgb: Mat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_gaussian_filter)
        val bgr = Utils.loadResource(this, R.drawable.lena)
        mRgb = Mat()
        Imgproc.cvtColor(bgr, mRgb, Imgproc.COLOR_BGR2RGB)
        bgr.release()
        showMat(mBinding.ivLena, mRgb)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_gaussian_filter, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_5_5 -> {
                showMat(mBinding.ivLena, mRgb)

                val result = Mat()
                Imgproc.GaussianBlur(mRgb, result, Size(5.0, 5.0), 10.0, 20.0)
                showMat(mBinding.ivResult, result)
                result.release()
            }
            R.id.menu_9_9 -> {
                showMat(mBinding.ivLena, mRgb)

                val result = Mat()
                Imgproc.GaussianBlur(mRgb, result, Size(9.0, 9.0), 10.0, 20.0)
                showMat(mBinding.ivResult, result)
                result.release()
            }
            R.id.menu_salt_pepper_noise_9_9 -> {
                saltPepperNoiseAndMeanFilter(9.0)
            }
            R.id.menu_salt_pepper_noise_5_5 -> {
                saltPepperNoiseAndMeanFilter(5.0)
            }
            R.id.menu_gaussian_noise_9_9 -> {
                gaussianNoiseAndMeanFilter(9.0)
            }
            R.id.menu_gaussian_noise_5_5 -> {
                gaussianNoiseAndMeanFilter(5.0)
            }
        }
        return true
    }

    private fun saltPepperNoiseAndMeanFilter(size: Double) {
        val source = mRgb.clone()
        val number = 10000
        for (k in 0..number) {
            val i = (0..1000).random() % source.cols()
            val j = (0..1000).random() % source.rows()
            when ((0..100).random() % 2) {
                0 -> {
                    when (source.channels()) {
                        1 -> {
                            source.put(j, i, 255.0)
                        }
                        2 -> {
                            source.put(j, i, 255.0, 255.0)
                        }
                        3 -> {
                            source.put(j, i, 255.0, 255.0, 255.0)
                        }
                        else -> {
                            source.put(j, i, 255.0, 255.0, 255.0, 255.0)
                        }
                    }
                }
                1 -> {
                    when (source.channels()) {
                        1 -> {
                            source.put(j, i, 0.0)
                        }
                        2 -> {
                            source.put(j, i, 0.0, 0.0)
                        }
                        3 -> {
                            source.put(j, i, 0.0, 0.0, 0.0)
                        }
                        else -> {
                            source.put(j, i, 0.0, 0.0, 0.0, 0.0)
                        }
                    }
                }
            }
        }
        showMat(mBinding.ivLena, source)

        val result = Mat()
        Imgproc.GaussianBlur(mRgb, result, Size(size, size), 10.0, 20.0)
        showMat(mBinding.ivResult, result)

        result.release()
        source.release()
    }

    private fun gaussianNoiseAndMeanFilter(size: Double) {
        val source = mRgb.clone()
        val noise = Mat(source.size(), source.type())
        val gaussian = Mat()
        Core.randn(noise, 20.0, 50.0)
        Core.add(source, noise, gaussian)
        showMat(mBinding.ivLena, gaussian)

        val result = Mat()
        Imgproc.GaussianBlur(mRgb, result, Size(size, size), 10.0, 20.0)
        showMat(mBinding.ivResult, result)

        source.release()
        noise.release()
        gaussian.release()
        result.release()
    }

    private fun showMat(view: ImageView, source: Mat) {
        val bitmap = Bitmap.createBitmap(source.width(), source.height(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(source, bitmap)
        view.setImageBitmap(bitmap)
    }

    override fun onDestroy() {
        mRgb.release()
        super.onDestroy()
    }
}