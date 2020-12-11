package cn.onlyloveyd.demo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.onlyloveyd.demo.R
import cn.onlyloveyd.demo.databinding.ActivityInPaintBinding
import cn.onlyloveyd.demo.ext.showMat
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import org.opencv.photo.Photo

/**
 * 图像修复
 * author: yidong
 * 2020/11/28
 */
class InPaintActivity : AppCompatActivity() {
    private val mBinding: ActivityInPaintBinding by lazy {
        ActivityInPaintBinding.inflate(layoutInflater)
    }

    private lateinit var mRgb: Mat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        mRgb = Mat()
        val bgr = Utils.loadResource(this, R.drawable.whiteprint)
        Imgproc.cvtColor(bgr, mRgb, Imgproc.COLOR_BGR2RGB)
        mBinding.ivLena.showMat(mRgb)
        doInPaint()
    }

    private fun doInPaint() {
        val gray = Mat()
        val mask = Mat()
        Imgproc.cvtColor(mRgb, gray, Imgproc.COLOR_RGB2GRAY)
        Imgproc.threshold(gray, mask, 254.0, 255.0, Imgproc.THRESH_BINARY)

        val kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, Size(20.0, 20.0))
        Imgproc.dilate(mask, mask, kernel)

        val dst = Mat()
        Photo.inpaint(mRgb, mask, dst, 5.0, Photo.INPAINT_TELEA)
        mBinding.ivResult.showMat(dst)
    }
}
