package cn.onlyloveyd.demo.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cn.onlyloveyd.demo.R
import cn.onlyloveyd.demo.databinding.ActivityDilateBinding
import cn.onlyloveyd.demo.ext.showMat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc

/**
 * 图像膨胀
 *
 * @author yidong
 * @date 2020/6/24
 */
class DilateActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityDilateBinding
    private lateinit var mBinary: Mat
    private var mFlag = Imgproc.CV_SHAPE_RECT
        set(value) {
            field = value
            doDilate(value, mSize.toDouble())
        }
    private var mSize = 3
        set(value) {
            field = value
            doDilate(mFlag, value.toDouble())
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_dilate)
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

        val bgr = Utils.loadResource(this, R.drawable.opencv)
        mBinary = Mat()
        val gray = Mat()
        Imgproc.cvtColor(bgr, gray, Imgproc.COLOR_BGR2GRAY)
        Imgproc.threshold(gray, mBinary, 125.0, 255.0, Imgproc.THRESH_BINARY_INV)
        mBinding.ivLena.showMat(mBinary)
        bgr.release()
        gray.release()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_dilate, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        mFlag = when (item.itemId) {
            R.id.dilate_cross -> {
                Imgproc.CV_SHAPE_CROSS
            }
            R.id.dilate_ellipse -> {
                Imgproc.CV_SHAPE_ELLIPSE
            }
            else -> Imgproc.CV_SHAPE_RECT
        }
        return true
    }

    private fun doDilate(flag: Int, width: Double) {
        title = "Flag = $flag，Size = ${width}X${width}"
        val kernel = Imgproc.getStructuringElement(flag, Size(width, width))
        val result = Mat()
        Imgproc.dilate(mBinary, result, kernel)
        GlobalScope.launch(Dispatchers.Main) {
            mBinding.ivResult.showMat(result)
            result.release()
        }
    }

    override fun onDestroy() {
        mBinary.release()
        super.onDestroy()
    }
}