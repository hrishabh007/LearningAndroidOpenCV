package cn.onlyloveyd.demo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.onlyloveyd.demo.R
import cn.onlyloveyd.demo.databinding.ActivityGrabcutBinding
import cn.onlyloveyd.demo.ext.showMat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.imgproc.Imgproc

/**
 * 图像分割 Grabcut
 * author: yidong
 * 2020/11/21
 */
class GrabcutActivity : AppCompatActivity() {

    private val mBinding: ActivityGrabcutBinding by lazy {
        ActivityGrabcutBinding.inflate(layoutInflater)
    }

    private lateinit var mRgb: Mat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)

        val bgr = Utils.loadResource(this, R.drawable.lena)
        mRgb = Mat()
        Imgproc.cvtColor(bgr, mRgb, Imgproc.COLOR_BGR2RGB)
        mBinding.ivLena.showMat(mRgb)
        GlobalScope.launch(Dispatchers.IO) {
            doGrabCut()
        }
    }

    private fun doGrabCut() {
        val rectMat = Mat()
        mRgb.copyTo(rectMat)
        val rect = Rect(80, 30, 340, 390)
        Imgproc.rectangle(rectMat, rect, Scalar.all(255.0), 2)
        GlobalScope.launch(Dispatchers.Main) {
            mBinding.ivLena.showMat(rectMat)
        }
        val bgdModel = Mat.zeros(1, 65, CvType.CV_64FC1)
        val fgdModel = Mat.zeros(1, 65, CvType.CV_64FC1)
        val mask = Mat.zeros(mRgb.size(), CvType.CV_8UC1)
        Imgproc.grabCut(mRgb, mask, rect, bgdModel, fgdModel, 5, Imgproc.GC_INIT_WITH_RECT)

        val result = Mat()
        for (i in 0 until mask.rows()) {
            for (j in 0 until mask.cols()) {
                val value = mask.get(i, j)[0].toInt()
                if (value == 1 || value == 3) {
                    mask.put(i, j, 0.0)
                } else {
                    mask.put(i, j, 255.0)
                }
            }
        }
        Core.bitwise_and(mRgb, mRgb, result, mask)
        GlobalScope.launch(Dispatchers.Main) {
            mBinding.ivResult.showMat(result)
        }
    }

    override fun onDestroy() {
        mRgb.release()
        super.onDestroy()
    }
}