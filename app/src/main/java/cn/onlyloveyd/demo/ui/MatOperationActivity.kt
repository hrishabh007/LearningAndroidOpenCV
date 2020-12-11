package cn.onlyloveyd.demo.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cn.onlyloveyd.demo.R
import cn.onlyloveyd.demo.databinding.ActivityMatOperationBinding
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

/**
 * Mat 操作
 * author: yidong
 * 2020/1/8
 */
class MatOperationActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMatOperationBinding
    private lateinit var bgr: Mat
    private lateinit var source: Mat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_mat_operation)

        bgr = Utils.loadResource(this, R.drawable.lena)
        source = Mat()
        Imgproc.cvtColor(bgr, source, Imgproc.COLOR_BGR2RGB)
        mBinding.ivSource.setImageResource(R.drawable.lena)
        val bitmap = Bitmap.createBitmap(source.width(), source.height(), Bitmap.Config.ARGB_8888)
        bitmap.density = DisplayMetrics.DENSITY_XXHIGH
        Utils.matToBitmap(bgr, bitmap)
        mBinding.ivBgr.setImageBitmap(bitmap)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_mat_operation, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.bitwise_not
            -> bitwiseNot(source)
            R.id.bitwise_and
            -> bitwiseAnd(source, bgr)
            R.id.bitwise_xor
            -> bitwiseXor(source, bgr)
            R.id.bitwise_or
            -> bitwiseOr(source, bgr)
            R.id.add
            -> add(source, bgr)
            R.id.subtract
            -> subtract(source, bgr)
            R.id.multiply
            -> multiply(source, bgr)
            R.id.divide
            -> divide(source, bgr)
            R.id.addWeight
            -> addWeight(source, bgr)
        }
        return true
    }

    private fun showResult(dst: Mat) {
        val bitmap = Bitmap.createBitmap(dst.width(), dst.height(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(dst, bitmap)
        mBinding.ivResult.setImageBitmap(bitmap)
    }

    private fun bitwiseNot(source: Mat) {
        val dst = Mat()
        Core.bitwise_not(source, dst)
        showResult(dst)
        dst.release()
    }

    private fun bitwiseAnd(source: Mat, attach: Mat) {
        val dst = Mat()
        Core.bitwise_and(source, attach, dst)
        showResult(dst)
        dst.release()
    }

    private fun bitwiseOr(source: Mat, attach: Mat) {
        val dst = Mat()
        Core.bitwise_or(source, attach, dst)
        showResult(dst)
        dst.release()
    }

    private fun bitwiseXor(source: Mat, attach: Mat) {
        val dst = Mat()
        Core.bitwise_xor(source, attach, dst)
        showResult(dst)
        dst.release()
    }

    private fun add(source: Mat, attach: Mat) {
        val dst = Mat()
        Core.add(source, attach, dst)
        showResult(dst)
        dst.release()
    }

    private fun subtract(source: Mat, attach: Mat) {
        val dst = Mat()
        Core.subtract(source, attach, dst)
        showResult(dst)
        dst.release()
    }

    private fun multiply(source: Mat, attach: Mat) {
        val dst = Mat()
        Core.multiply(source, attach, dst)
        showResult(dst)
        dst.release()
    }

    private fun divide(source: Mat, attach: Mat) {
        val dst = Mat()
        Core.divide(source, attach, dst, 50.0, -1)
        Core.convertScaleAbs(dst, dst)
        showResult(dst)
        dst.release()
    }

    private fun addWeight(source: Mat, attach: Mat) {
        val dst = Mat()
        Core.addWeighted(source, 0.2, attach, 0.8, 100.0, dst)
        showResult(dst)
        dst.release()
    }

    override fun onDestroy() {
        source.release()
        bgr.release()
        super.onDestroy()
    }
}