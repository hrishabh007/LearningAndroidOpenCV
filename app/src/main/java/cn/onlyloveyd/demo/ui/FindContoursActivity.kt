package cn.onlyloveyd.demo.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cn.onlyloveyd.demo.App
import cn.onlyloveyd.demo.R
import cn.onlyloveyd.demo.databinding.ActivityFindContoursBinding
import cn.onlyloveyd.demo.ext.showMat
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.imgproc.Imgproc

/**
 * 轮廓发现、绘制、面积、周长
 * author: yidong
 * 2020/9/19
 */
class FindContoursActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityFindContoursBinding
    private lateinit var mSource: Mat
    private var level = 1
        set(value) {
            field = value
            find()
            mBinding.level.text = level.toString()
        }

    private var mFlag = Imgproc.RETR_TREE
        set(value) {
            field = value
            find()
        }

    private var ignoreLevel = true
        set(value) {
            field = value
            find()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_find_contours)
        mBinding.ignoreLevel.setOnCheckedChangeListener { _, isChecked ->
            ignoreLevel = isChecked
        }
        val bgr = Utils.loadResource(this, R.drawable.opencv)
        mSource = Mat()
        Imgproc.cvtColor(bgr, mSource, Imgproc.COLOR_BGR2RGB)
        mBinding.ivLena.showMat(mSource)
        title = "RETR_TREE"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_contours, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        title = item.title
        mFlag = when (item.itemId) {
            R.id.retr_external -> Imgproc.RETR_EXTERNAL
            R.id.retr_list -> Imgproc.RETR_LIST
            R.id.retr_ccomp -> Imgproc.RETR_CCOMP
            else -> Imgproc.RETR_TREE
        }
        return true
    }

    private fun find() {
        val tmp = mSource.clone()
        val gray = Mat()
        Imgproc.cvtColor(mSource, gray, Imgproc.COLOR_BGR2GRAY)
        Imgproc.GaussianBlur(gray, gray, Size(13.0, 13.0), 4.0, 4.0)
        val binary = Mat()
        Imgproc.threshold(gray, binary, 50.0, 255.0, Imgproc.THRESH_BINARY and Imgproc.THRESH_OTSU)

        val contours = mutableListOf<MatOfPoint>()
        val hierarchy = Mat()
        Imgproc.findContours(
            binary,
            contours,
            hierarchy,
            mFlag,
            Imgproc.CHAIN_APPROX_SIMPLE
        )

        for (i in 0 until contours.size) {
            val area = Imgproc.contourArea(contours[i])
            val source = MatOfPoint2f()
            source.fromList(contours[i].toList())
            val length = Imgproc.arcLength(source, true)
            Log.d(App.TAG, "轮廓${i}面积：${area}; 周长：${length}")
        }

        if (ignoreLevel) {
            Imgproc.drawContours(
                tmp,
                contours,
                -1,
                Scalar(255.0, 255.0, 0.0),
                4,
                Imgproc.LINE_AA
            )
        } else {
            Imgproc.drawContours(
                tmp,
                contours,
                -1,
                Scalar(255.0, 255.0, 0.0),
                4,
                Imgproc.LINE_AA,
                hierarchy,
                level
            )
        }
        mBinding.ivResult.showMat(tmp)
        Log.d(App.TAG, "hierarchy: ${hierarchy.dump()}")
        gray.release()
        binary.release()
        hierarchy.release()
        tmp.release()
    }


    fun increase(v: View) {
        level += 1
    }

    fun decrease(v: View) {
        level -= 1
    }

    override fun onDestroy() {
        mSource.release()
        super.onDestroy()
    }
}