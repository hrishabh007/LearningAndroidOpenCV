package cn.onlyloveyd.demo.ui

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cn.onlyloveyd.demo.R
import cn.onlyloveyd.demo.databinding.ActivityHistBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import java.util.*
import kotlin.math.round

/**
 * 直方图
 * author: yidong
 * 2020/3/21
 */
class HistActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityHistBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_hist)

        initChart()

        mBinding.ivLena.setImageResource(R.drawable.lena)
        val bgr = Utils.loadResource(this, R.drawable.lena)
        val gray = Mat()
        Imgproc.cvtColor(bgr, gray, Imgproc.COLOR_BGR2GRAY)
        showMat(mBinding.ivGray, gray)
        calHist(gray)
    }

    private fun initChart() {
        mBinding.chartHist.axisLeft.axisMinimum = 0.0F
        mBinding.chartHist.axisRight.isEnabled = false
        mBinding.chartHist.setDrawGridBackground(false)
        mBinding.chartHist.description.isEnabled = false
        mBinding.chartHist.xAxis.position = XAxis.XAxisPosition.BOTTOM
    }

    private fun showMat(view: ImageView, source: Mat) {
        val bitmap = Bitmap.createBitmap(source.width(), source.height(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(source, bitmap)
        view.setImageBitmap(bitmap)
    }

    private fun calHist(gray: Mat) {
        val hist = Mat()
        val source = listOf(gray)
        val mask = Mat()

        Imgproc.calcHist(
            source,
            MatOfInt(0),
            mask,
            hist,
            MatOfInt(256),
            MatOfFloat(0F, 255F),
            false
        )

        val hist_w = 512
        val hist_h = 400
        val width = 2
        val histImage = Mat.zeros(hist_h, hist_w, CvType.CV_8UC3)
        for (i in 0 until hist.rows()) {
            Imgproc.rectangle(
                histImage,
                Point(width * (i - 1).toDouble(), (hist_h - 1).toDouble()),
                Point((width * i - 1).toDouble(), hist_h - round(hist.get(i, 0)[0] / 10)),
                Scalar(255.0, 255.0, 255.0),
                -1
            )
        }
        showMat(mBinding.ivHist, histImage)
        updateChart(hist)

    }

    private fun updateChart(hist: Mat) {
        val source = FloatArray(256)
        for (i in 0 until hist.rows()) {
            val result = FloatArray(1)
            hist.get(i, 0, result)
            source[i] = result[0]
        }

        val values =
            ArrayList<Entry>()
        for (i in source.indices) {
            values.add(
                Entry(
                    i.toFloat(),
                    source[i]
                )
            )
        }

        val set1 = LineDataSet(values, "直方图")
        set1.fillColor = Color.BLACK
        set1.axisDependency = AxisDependency.LEFT
        set1.color = ColorTemplate.getHoloBlue()
        set1.lineWidth = 2f
        set1.fillAlpha = 65
        set1.fillColor = ColorTemplate.getHoloBlue()
        set1.highLightColor = Color.rgb(244, 117, 117)
        set1.setDrawCircleHole(false)
        set1.setDrawCircles(false)
        set1.setDrawFilled(true)

        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(set1)

        val data = LineData(dataSets)
        mBinding.chartHist.data = data
        mBinding.chartHist.invalidate()
    }
}