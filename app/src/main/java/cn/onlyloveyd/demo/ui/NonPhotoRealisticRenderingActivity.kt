package cn.onlyloveyd.demo.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import cn.onlyloveyd.demo.R
import cn.onlyloveyd.demo.databinding.ActivityNonPhotorealisticRenderingBinding
import cn.onlyloveyd.demo.ext.showMat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc
import org.opencv.photo.Photo

/**
 * 非真实渲染
 *
 * @author yidong
 * @date 11/30/20
 */
class NonPhotoRealisticRenderingActivity : AppCompatActivity() {

    private lateinit var mRgb: Mat
    private val mBinding: ActivityNonPhotorealisticRenderingBinding by lazy {
        ActivityNonPhotorealisticRenderingBinding.inflate(layoutInflater)
    }

    private var sigmaR = 10f
        set(value) {
            field = when {
                value > 200f -> {
                    200f
                }
                value < 0f -> {
                    200f
                }
                else -> {
                    value
                }
            }
            mBinding.tvSigmaR.text = sigmaR.toInt().toString(10)
        }
    private var sigmaS = 0.1f
        set(value) {
            field = when {
                value > 1.0f -> {
                    1.0f
                }
                value < 0f -> {
                    0f
                }
                else -> {
                    value
                }
            }
            mBinding.tvSigmaS.text = String.format("%.1f", sigmaS)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)

        mRgb = Mat()
        val bgr = Utils.loadResource(this, R.drawable.cow)
        Imgproc.cvtColor(bgr, mRgb, Imgproc.COLOR_BGR2RGB)
        mBinding.ivLena.showMat(mRgb)
    }


    private fun doEdgePreservingFilter(flag: Int) {
        val dst = Mat()
        mBinding.isLoading = true
        GlobalScope.launch(Dispatchers.IO) {
            Photo.edgePreservingFilter(mRgb, dst, flag, sigmaR, sigmaS)
            launch(Dispatchers.Main) {
                mBinding.isLoading = false
                mBinding.ivResult.showMat(dst)
            }
        }
    }

    private fun doDetailEnhance() {
        val dst = Mat()
        mBinding.isLoading = true
        GlobalScope.launch(Dispatchers.IO) {
            Photo.detailEnhance(mRgb, dst, sigmaR, sigmaS)
            launch(Dispatchers.Main) {
                mBinding.isLoading = false
                mBinding.ivResult.showMat(dst)
            }
        }
    }


    private fun doPencilSketch() {
        val dst1 = Mat()
        val dst2 = Mat()
        mBinding.isLoading = true
        GlobalScope.launch(Dispatchers.IO) {
            Photo.pencilSketch(mRgb, dst1, dst2, sigmaR, sigmaS, 0.03f)
            launch(Dispatchers.Main) {
                mBinding.isLoading = false
                mBinding.ivResult.showMat(dst1)
            }
        }
    }

    private fun doStylization() {
        val dst = Mat()
        mBinding.isLoading = true
        GlobalScope.launch(Dispatchers.IO) {
            Photo.stylization(mRgb, dst, sigmaR, sigmaS)
            launch(Dispatchers.Main) {
                mBinding.isLoading = false
                mBinding.ivResult.showMat(dst)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_non_photorealistic_rendering, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        title = item.title
        when (item.itemId) {
            R.id.photo_edge_preserving_normconv_filter
            -> {
                doEdgePreservingFilter(Photo.NORMCONV_FILTER)
            }
            R.id.photo_edge_preserving_recurs_filter
            -> {
                doEdgePreservingFilter(Photo.RECURS_FILTER)
            }
            R.id.photo_detail_enhance
            -> {
                doDetailEnhance()
            }
            R.id.photo_pencil_sketch
            -> {
                doPencilSketch()
            }
            R.id.photo_stylization
            -> {
                doStylization()
            }
        }
        return true
    }

    fun incSigmaR(view: View) {
        this.sigmaR = this.sigmaR.plus(1.0f)
        if (this.sigmaR > 200.0f) {
            this.sigmaR = 200f
        }
    }

    fun decSigmaR(view: View) {
        this.sigmaR = this.sigmaR.minus(1.0f)
        if (this.sigmaR < 0f) {
            this.sigmaR = 0f
        }
    }

    fun incSigmaS(view: View) {
        this.sigmaS = this.sigmaS.plus(.1f)
        if (this.sigmaS > 1.0f) {
            this.sigmaS = 1f
        }
    }

    fun decSigmaS(view: View) {
        this.sigmaS = this.sigmaS.minus(.1f)
        if (this.sigmaS < 0f) {
            this.sigmaS = 0f
        }
    }
}