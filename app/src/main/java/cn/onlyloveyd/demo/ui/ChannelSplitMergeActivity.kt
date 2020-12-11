package cn.onlyloveyd.demo.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cn.onlyloveyd.demo.R
import cn.onlyloveyd.demo.databinding.ActivityChannelSplitMergeBinding
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

/**
 * 通道分离合并
 * author: yidong
 * 2020/2/10
 */
class ChannelSplitMergeActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityChannelSplitMergeBinding
    private var mBgr = Mat()
    private var mChannelB = Mat()
    private var mChannelG = Mat()
    private var mChannelR = Mat()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_channel_split_merge)
        mBgr = Utils.loadResource(this, R.drawable.test)
        val imgList = mutableListOf<Mat>()
        Core.split(mBgr, imgList)
        mChannelB = imgList[0]
        mChannelG = imgList[1]
        mChannelR = imgList[2]

        showRgb()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_channel_split_merge, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        title = item.title
        when (item.itemId) {
            R.id.bgr -> showBgr()
            R.id.rgb -> showRgb()
            R.id.channel_b -> showB()
            R.id.channel_g -> showG()
            R.id.channel_r -> showR()
            R.id.B_G_zero -> showBGZero()
            R.id.B_R_zero -> showBRZero()
            R.id.G_R_zero -> showGRZero()
            R.id.switch_b_r -> showSwitchBR()
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

    private fun showB() {
        showMat(mChannelB)
    }

    private fun showG() {
        showMat(mChannelG)
    }

    private fun showR() {
        showMat(mChannelR)
    }

    private fun showBGZero() {
        val zero = Mat.zeros(mBgr.rows(), mBgr.cols(), CvType.CV_8UC1)
        val list = mutableListOf<Mat>()
        list.add(zero)
        list.add(zero)
        list.add(mChannelR)
        val result = Mat()
        Core.merge(list, result)
        showMat(result)
        result.release()
    }

    private fun showBRZero() {
        val zero = Mat.zeros(mBgr.rows(), mBgr.cols(), CvType.CV_8UC1)
        val list = mutableListOf<Mat>()
        list.add(zero)
        list.add(mChannelG)
        list.add(zero)
        val result = Mat()
        Core.merge(list, result)
        showMat(result)
        result.release()
    }

    private fun showGRZero() {
        val zero = Mat.zeros(mBgr.rows(), mBgr.cols(), CvType.CV_8UC1)
        val list = mutableListOf<Mat>()
        list.add(mChannelB)
        list.add(zero)
        list.add(zero)
        val result = Mat()
        Core.merge(list, result)
        showMat(result)
        result.release()
    }

    private fun showSwitchBR() {
        val zero = Mat.zeros(mBgr.rows(), mBgr.cols(), CvType.CV_8UC1)
        val list = mutableListOf<Mat>()
        list.add(mChannelR)
        list.add(mChannelG)
        list.add(mChannelB)
        val result = Mat()
        Core.merge(list, result)
        showMat(result)
        result.release()
    }

    private fun showMat(source: Mat) {
        val bitmap = Bitmap.createBitmap(source.width(), source.height(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(source, bitmap)
        mBinding.ivLena.setImageBitmap(bitmap)
    }

    override fun onDestroy() {
        mBgr.release()
        mChannelB.release()
        mChannelG.release()
        mChannelR.release()
        super.onDestroy()
    }
}