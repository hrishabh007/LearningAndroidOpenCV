package cn.onlyloveyd.demo.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cn.onlyloveyd.demo.R
import cn.onlyloveyd.demo.databinding.ActivityColorTransferBinding
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

/**
 * 颜色模型及转换
 * author: yidong
 * 2020/2/8
 */
class ColorTransferActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityColorTransferBinding
    private lateinit var mBgr: Mat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_color_transfer)
        mBgr = Utils.loadResource(this, R.drawable.lena)
        showBgr()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_color_transfer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        title = item.title
        when (item.itemId) {
            R.id.bgr -> showBgr()
            R.id.rgb -> showRgb()
            R.id.yuv -> showYuv()
            R.id.hsv -> showHsv()
            R.id.lab -> showLab()
            R.id.gray -> showGray()
        }
        return true
    }

    private fun showBgr() {
        showMat(mBgr)
    }

    private fun showRgb() {
        val rgb = Mat()
        Imgproc.cvtColor(mBgr, rgb, Imgproc.COLOR_BGR2RGB)
        showMat(rgb)
        rgb.release()
    }

    private fun showYuv() {
        val yuv = Mat()
        Imgproc.cvtColor(mBgr, yuv, Imgproc.COLOR_BGR2YUV)
        showMat(yuv)
        yuv.release()
    }

    private fun showHsv() {
        val hsv = Mat()
        Imgproc.cvtColor(mBgr, hsv, Imgproc.COLOR_BGR2HSV)
        showMat(hsv)
        hsv.release()
    }

    private fun showLab() {
        val lab = Mat()
        Imgproc.cvtColor(mBgr, lab, Imgproc.COLOR_BGR2Lab)
        showMat(lab)
        lab.release()
    }

    private fun showGray() {
        val gray = Mat()
        Imgproc.cvtColor(mBgr, gray, Imgproc.COLOR_BGR2GRAY)
        showMat(gray)
        gray.release()
    }

    private fun showMat(source: Mat) {
        val bitmap = Bitmap.createBitmap(source.width(), source.height(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(source, bitmap)
        mBinding.ivLena.setImageBitmap(bitmap)
    }

    override fun onDestroy() {
        super.onDestroy()
        mBgr.release()
    }
}