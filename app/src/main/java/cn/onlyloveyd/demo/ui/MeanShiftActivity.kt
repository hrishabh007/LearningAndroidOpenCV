package cn.onlyloveyd.demo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.onlyloveyd.demo.R
import cn.onlyloveyd.demo.databinding.ActivityMeanShiftBinding
import cn.onlyloveyd.demo.ext.showMat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.imgproc.Imgproc

/**
 * Mean-Shift 均值漂移
 *
 * @author yidong
 * @date 11/25/20
 */
class MeanShiftActivity : AppCompatActivity() {

    private val mBinding: ActivityMeanShiftBinding by lazy {
        ActivityMeanShiftBinding.inflate(layoutInflater)
    }
    private lateinit var mRgb: Mat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        mRgb = Mat()
        val bgr = Utils.loadResource(this, R.drawable.number)
        Imgproc.cvtColor(bgr, mRgb, Imgproc.COLOR_BGR2RGB)
        mBinding.ivLena.showMat(mRgb)
        mBinding.isLoading = true
        GlobalScope.launch(Dispatchers.IO) {
            doMeanShift()
        }
    }

    private fun doMeanShift() {
        val dst = Mat()
        Imgproc.pyrMeanShiftFiltering(mRgb, dst, 40.0, 40.0)

        val maskers = Mat(dst.rows() + 2, dst.cols() + 2, CvType.CV_8UC1, Scalar.all(0.0))
        Imgproc.floodFill(
            dst,
            maskers,
            Point(7.0, 7.0),
            Scalar(65.0, 105.0, 225.0),
            Rect(),
            Scalar.all(10.0),
            Scalar.all(10.0),
            Imgproc.LINE_4 or Imgproc.FLOODFILL_FIXED_RANGE or (250 shl 8)
        )

        GlobalScope.launch(Dispatchers.Main) {
            mBinding.isLoading = false
            mBinding.ivResult.showMat(dst)
        }
    }
}