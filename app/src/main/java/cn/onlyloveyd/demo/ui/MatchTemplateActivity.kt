package cn.onlyloveyd.demo.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cn.onlyloveyd.demo.App
import cn.onlyloveyd.demo.R
import cn.onlyloveyd.demo.databinding.ActivityMatchTemplateBinding
import cn.onlyloveyd.demo.ext.showMat
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.imgproc.Imgproc

/**
 * 模板匹配
 * author: yidong
 * 2020/10/23
 */
class MatchTemplateActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMatchTemplateBinding
    private lateinit var mRgb: Mat
    private lateinit var mTemplate: Mat
    private var method = Imgproc.TM_SQDIFF
        set(value) {
            field = value
            doMatch(field)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_match_template)
        title = "TM_SQDIFF"
        val bgr = Utils.loadResource(this, R.drawable.kobe)
        mRgb = Mat()
        mTemplate = Mat()
        Imgproc.cvtColor(bgr, mRgb, Imgproc.COLOR_BGR2RGB)
        val templateBgr = Utils.loadResource(this, R.drawable.kobe_template)
        Imgproc.cvtColor(templateBgr, mTemplate, Imgproc.COLOR_BGR2RGB)
        mBinding.ivLena.showMat(mTemplate)
        doMatch(method)
    }

    private fun doMatch(method: Int) {
        val tmp = mRgb.clone()
        val result = Mat()
        Imgproc.matchTemplate(mRgb, mTemplate, result, method)
        val minMaxLoc = Core.minMaxLoc(result)
        Log.d(
            App.TAG,
            "maxVal = ${minMaxLoc.maxVal}, maxLocation = ${minMaxLoc.maxLoc}, minVal = ${minMaxLoc.minVal}, minLocation = ${minMaxLoc.minLoc}"
        )
        val topLeft = if (method == Imgproc.TM_SQDIFF || method == Imgproc.TM_SQDIFF_NORMED) {
            minMaxLoc.minLoc
        } else {
            minMaxLoc.maxLoc
        }
        val rect = Rect(topLeft, Size(mTemplate.cols().toDouble(), mTemplate.rows().toDouble()))
        Imgproc.rectangle(tmp, rect, Scalar(255.0, 0.0, 0.0), 4, Imgproc.LINE_8)
        mBinding.ivResult.showMat(tmp)
        tmp.release()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_match_template, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.match_tm_sqdiff -> {
                method = Imgproc.TM_SQDIFF
                title = "TM_SQDIFF"
            }
            R.id.match_tm_sqdiff_normed -> {
                method = Imgproc.TM_SQDIFF_NORMED
                title = "TM_SQDIFF_NORMED"
            }
            R.id.match_tm_ccoeff -> {
                method = Imgproc.TM_CCOEFF
                title = "TM_CCOEFF"
            }

            R.id.match_tm_ccoeff_normed -> {
                method = Imgproc.TM_CCOEFF_NORMED
                title = "TM_CCOEFF_NORMED"
            }
            R.id.match_tm_ccorr -> {
                method = Imgproc.TM_CCORR
                title = "TM_CCORR"
            }
            R.id.match_tm_ccorr_normed -> {
                method = Imgproc.TM_CCORR_NORMED
                title = "TM_CCORR_NORMED"
            }
        }
        return true
    }

    override fun onDestroy() {
        mTemplate.release()
        mRgb.release()
        super.onDestroy()
    }
}