package cn.onlyloveyd.demo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cn.onlyloveyd.demo.R
import cn.onlyloveyd.demo.databinding.ActivityPixelStatisticsBinding
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.MatOfDouble
import org.opencv.core.Point
import org.opencv.imgproc.Imgproc

/**
 * 像素相关信息统计
 * author: yidong
 * 2020/1/23
 */
class PixelStatisticsActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityPixelStatisticsBinding

    private var message: String = ""
        set(value) {
            field = value
            onMessageChange(value)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_pixel_statistics)
        supportActionBar?.title = intent.getStringExtra("title")
        doStatistics()
    }

    private fun doStatistics() {
        val bgr = Utils.loadResource(this, R.drawable.lena)
        val source = Mat()
        Imgproc.cvtColor(bgr, source, Imgproc.COLOR_BGR2RGB)
        message +=
            "列数 = ${source.cols()} 行数 = ${source.rows()} 通道数 = ${source.channels()}  深度 = ${source.depth()}\n"

        minMaxLoc(source)
        meanStdDev(source)
        bgr.release()
        source.release()
    }

    private fun minMaxLoc(source: Mat) {
        val bgrList = ArrayList<Mat>()
        Core.split(source, bgrList)

        var minLoc = Point()
        var maxLoc = Point()
        var minVal = 255.0
        var maxVal = 0.0
        var minCha = 0
        var maxCha = 0
        for (index in 0 until bgrList.size) {
            val tmp = Core.minMaxLoc(bgrList[index])
            if (tmp.minVal < minVal) {
                minVal = tmp.minVal
                minLoc = tmp.minLoc
                minCha = index
            }
            if (tmp.maxVal > maxVal) {
                maxVal = tmp.maxVal
                maxLoc = tmp.maxLoc
                maxCha = index
            }
        }
        val tmp =
            "最小值 = $minVal, 位于${minCha}通道${minLoc}\n最大值 = $maxVal, 位于${maxCha}通道${maxLoc}\n"
        message += tmp

        for (current in bgrList) {
            current.release()
        }
    }

    private fun meanStdDev(source: Mat) {
        val mean = MatOfDouble()
        val stdDev = MatOfDouble()
        Core.meanStdDev(source, mean, stdDev)
        val tmp = "平均值：${mean.toList()}\n标准差：${stdDev.toList()}\n"
        message += tmp
        mean.release()
        stdDev.release()
    }

    private fun onMessageChange(message: String) {
        mBinding.tvPixelStatistics.text = message
    }
}