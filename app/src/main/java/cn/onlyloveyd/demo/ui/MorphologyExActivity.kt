package cn.onlyloveyd.demo.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cn.onlyloveyd.demo.R
import cn.onlyloveyd.demo.databinding.ActivityMorphologyExBinding
import cn.onlyloveyd.demo.ext.showMat
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc

/**
 * 形态学
 * author: yidong
 * 2020/7/5
 */
class MorphologyExActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMorphologyExBinding
    private lateinit var mBinary: Mat

    private var mFlag = Imgproc.MORPH_OPEN
        set(value) {
            field = value
            doMorphology(value, mSize.toDouble())
        }
    private var mSize = 3
        set(value) {
            field = value
            doMorphology(mFlag, value.toDouble())
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_morphology_ex)
        mBinding.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mSize = progress
                mBinding.tvSize.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
        mBinary = Mat()
        val bgr = Utils.loadResource(this, R.drawable.number)
        val gray = Mat()
        Imgproc.cvtColor(bgr, gray, Imgproc.COLOR_BGR2GRAY)
        Imgproc.threshold(gray, mBinary, 125.0, 255.0, Imgproc.THRESH_BINARY_INV)
        mBinding.ivLena.showMat(mBinary)

        bgr.release()
        gray.release()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_morphology_ex, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        title = item.title
        mFlag = when (item.itemId) {
            R.id.morphology_close -> {
                Imgproc.MORPH_CLOSE
            }
            R.id.morphology_gradient -> {
                Imgproc.MORPH_GRADIENT
            }
            R.id.morphology_top_hat -> {
                Imgproc.MORPH_TOPHAT
            }
            R.id.morphology_black_hat -> {
                Imgproc.MORPH_BLACKHAT
            }
            R.id.morphology_hit_miss -> {
                Imgproc.MORPH_HITMISS
            }
            else -> Imgproc.MORPH_OPEN
        }
        return true
    }

    private fun doMorphology(flag: Int, width: Double) {
        val kernel = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT, Size(width, width))
        val dst = Mat()
        Imgproc.morphologyEx(mBinary, dst, flag, kernel)
        mBinding.ivResult.showMat(dst)
        dst.release()
    }
}