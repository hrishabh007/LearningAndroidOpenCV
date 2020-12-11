package cn.onlyloveyd.demo.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cn.onlyloveyd.demo.R
import cn.onlyloveyd.demo.databinding.ActivityPolarBinding
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.imgproc.Imgproc

/**
 * 极坐标变换
 * author: yidong
 * 2020/3/12
 */
class PolarActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityPolarBinding
    private lateinit var mRgb: Mat
    private lateinit var mPolar: Mat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_polar)

        val bgr = Utils.loadResource(this, R.drawable.circle)
        mRgb = Mat()
        Imgproc.cvtColor(bgr, mRgb, Imgproc.COLOR_BGR2RGB)
        showMat(mBinding.ivCircle, mRgb)
        bgr.release()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_polar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.polar -> {
                polarTransform(mRgb)
            }

            R.id.reverse_polar -> {
                if (!this::mPolar.isInitialized) {
                    Toast.makeText(this, "请先进行极坐标变换", Toast.LENGTH_SHORT).show()
                } else {
                    reversePolarTransform(mPolar)
                }
            }
        }
        return true
    }

    private fun polarTransform(source: Mat) {
        val center = Point(source.width() / 2.0, source.height() / 2.0)
        mPolar = Mat()
        val size = mRgb.size()
        Imgproc.warpPolar(
            source,
            mPolar,
            size,
            center,
            center.x,
            Imgproc.INTER_LINEAR + Imgproc.WARP_POLAR_LINEAR
        )
        showMat(mBinding.ivResult, mPolar)
    }

    private fun reversePolarTransform(source: Mat) {
        val center = Point(source.width() / 2.0, source.height() / 2.0)
        val dst = Mat()
        val size = mRgb.size()
        Imgproc.warpPolar(
            source,
            dst,
            size,
            center,
            center.x,
            Imgproc.INTER_LINEAR + Imgproc.WARP_INVERSE_MAP
        )
        showMat(mBinding.ivResult, dst)
    }


    private fun showMat(view: ImageView, source: Mat) {
        val bitmap = Bitmap.createBitmap(source.width(), source.height(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(source, bitmap)
        view.setImageBitmap(bitmap)
    }

    override fun onDestroy() {
        mRgb.release()
        mPolar.release()
        super.onDestroy()
    }
}