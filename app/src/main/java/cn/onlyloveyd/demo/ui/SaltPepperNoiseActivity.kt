package cn.onlyloveyd.demo.ui

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cn.onlyloveyd.demo.R
import cn.onlyloveyd.demo.databinding.ActivitySaltPepperNoiseBinding
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

/**
 * 椒盐噪声
 * author: yidong
 * 2020/3/31
 */
class SaltPepperNoiseActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, SaltPepperNoiseActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var mBinding: ActivitySaltPepperNoiseBinding
    private lateinit var mRgb: Mat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_salt_pepper_noise)
        mBinding.presenter = this
        val bgr = Utils.loadResource(this, R.drawable.lena)
        mRgb = Mat()
        Imgproc.cvtColor(bgr, mRgb, Imgproc.COLOR_BGR2RGB)
        showMat(mBinding.ivLena, mRgb)
    }

    fun addNoise() {
        hideKeyboard()
        val source = mRgb.clone()
        var number = 10000
        try {
            number = mBinding.etNoiseNumber.text.toString().toInt()
        } catch (e: NumberFormatException) {

        }
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

        showMat(mBinding.ivResult, source)
        source.release()
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

    override fun onDestroy() {
        mRgb.release()
        super.onDestroy()
    }
}