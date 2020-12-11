package cn.onlyloveyd.demo.ui

import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import cn.onlyloveyd.demo.R
import cn.onlyloveyd.demo.databinding.ActivityFloodFillBinding
import cn.onlyloveyd.demo.databinding.LayoutFloodFillMenuBinding
import cn.onlyloveyd.demo.ext.showMat
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.opencv.android.Utils
import org.opencv.core.CvType.CV_8UC1
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Rect
import org.opencv.core.Scalar
import org.opencv.imgproc.Imgproc


/**
 * 图像分割--漫水填充法
 * author: yidong
 * 2020/11/7
 */
class FloodFillActivity : AppCompatActivity() {
    private val mBinding by lazy { ActivityFloodFillBinding.inflate(layoutInflater) }
    private lateinit var mMenuDialog: BottomSheetDialog
    private lateinit var mMenuDialogBinding: LayoutFloodFillMenuBinding

    private var mConnectionType = 4
    private var mFloodFillFlag = 0
    private var mScalarNumber = 250 shl 8

    private lateinit var mRgb: Mat
    private var loDiff = 0.0
        set(value) {
            field = value
            doFloodFill()
        }
    private var upDiff = 0.0
        set(value) {
            field = value
            doFloodFill()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        val bgr = Utils.loadResource(this, R.drawable.wedding)
        mRgb = Mat()
        Imgproc.cvtColor(bgr, mRgb, Imgproc.COLOR_BGR2RGB)
        mBinding.ivLena.showMat(mRgb)
        mBinding.sbLow.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                mBinding.tvLoDiff.text = p1.toString()
                loDiff = p1.toDouble()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })
        mBinding.sbUp.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                mBinding.tvUpDiff.text = p1.toString()
                upDiff = p1.toDouble()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })
        mBinding.btFlag.setOnClickListener {
            showMenuDialog()
        }
        doFloodFill()
    }

    private fun doFloodFill() {
        val tmp = mRgb.clone()
        val maskers = Mat(mRgb.rows() + 2, mRgb.cols() + 2, CV_8UC1, Scalar.all(0.0))
        Imgproc.floodFill(
            tmp,
            maskers,
            Point(7.0, 7.0),
            Scalar(65.0, 105.0, 225.0),
            Rect(),
            Scalar.all(loDiff),
            Scalar.all(upDiff),
            mConnectionType or mFloodFillFlag or mScalarNumber
        )
        if (mFloodFillFlag and Imgproc.FLOODFILL_MASK_ONLY == Imgproc.FLOODFILL_MASK_ONLY) {
            mBinding.ivResult.showMat(maskers)
        } else {
            mBinding.ivResult.showMat(tmp)
        }

        tmp.release()
        maskers.release()
    }

    private fun showMenuDialog() {
        if (!this::mMenuDialog.isInitialized) {
            mMenuDialog = BottomSheetDialog(this)
            mMenuDialogBinding = LayoutFloodFillMenuBinding.inflate(layoutInflater)
            mMenuDialog.setContentView(mMenuDialogBinding.root)
            mMenuDialog.setOnDismissListener {
                mConnectionType =
                    if (mMenuDialogBinding.rgFirst.checkedRadioButtonId == R.id.rb_8) {
                        8
                    } else {
                        4
                    }
                mFloodFillFlag = if (mMenuDialogBinding.cbFixed.isChecked) {
                    mFloodFillFlag or Imgproc.FLOODFILL_FIXED_RANGE
                } else {
                    mFloodFillFlag and Imgproc.FLOODFILL_FIXED_RANGE.inv()
                }
                mFloodFillFlag = if (mMenuDialogBinding.cbMaskOnly.isChecked) {
                    mFloodFillFlag or Imgproc.FLOODFILL_MASK_ONLY
                } else {
                    mFloodFillFlag and Imgproc.FLOODFILL_MASK_ONLY.inv()
                }
                try {
                    mScalarNumber = mMenuDialogBinding.etScalar.text.toString().toInt(10) shl 8
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                }
                doFloodFill()
            }
        }
        mMenuDialog.show()
    }

    override fun onDestroy() {
        mRgb.release()
        super.onDestroy()
    }
}