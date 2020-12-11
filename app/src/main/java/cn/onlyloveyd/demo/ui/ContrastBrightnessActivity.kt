package cn.onlyloveyd.demo.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cn.onlyloveyd.demo.R
import cn.onlyloveyd.demo.databinding.ActivityContrastBrightnessBinding
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.Scalar
import org.opencv.imgproc.Imgproc

/**
 * 调整对比度和亮度
 * author: yidong
 * 2020/1/29
 */
class ContrastBrightnessActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityContrastBrightnessBinding
    private lateinit var source: Mat

    private var originBrightness: Double = 0.0

    private var brightness: Double = 0.0
        set(value) {
            field = value
            adjustBrightnessContrast()
        }
    private var contrast: Double = 100.0
        set(value) {
            field = value
            adjustBrightnessContrast()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_contrast_brightness)
        val bgr = Utils.loadResource(this, R.drawable.lena)

        source = Mat()
        Imgproc.cvtColor(bgr, source, Imgproc.COLOR_BGR2RGB)
        val bitmap = Bitmap.createBitmap(source.width(), source.height(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(source, bitmap)
        mBinding.ivSource.setImageBitmap(bitmap)
        originBrightness = Core.mean(source).`val`[0]
        mBinding.sbBrightness.progress = originBrightness.toInt()
        mBinding.sbBrightness.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                brightness = progress.toDouble()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        mBinding.sbContrast.progress = 100
        mBinding.sbContrast.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                contrast = progress.toDouble()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    private fun adjustBrightnessContrast() {
        val pre = Mat()
        Core.add(
            source,
            Scalar(
                brightness - originBrightness,
                brightness - originBrightness,
                brightness - originBrightness
            ),
            pre
        )
        val dst = Mat()
        Core.multiply(
            pre,
            Scalar(
                contrast / 100,
                contrast / 100,
                contrast / 100,
                contrast / 100
            ),
            dst
        )
        val bitmap = Bitmap.createBitmap(dst.cols(), dst.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(dst, bitmap)
        mBinding.ivSource.setImageBitmap(bitmap)
        pre.release()
        dst.release()
    }

    override fun onDestroy() {
        source.release()
        super.onDestroy()
    }
}