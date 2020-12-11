package cn.onlyloveyd.demo.ui

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cn.onlyloveyd.demo.App
import cn.onlyloveyd.demo.R
import cn.onlyloveyd.demo.databinding.ActivityQrDetectBinding
import cn.onlyloveyd.demo.databinding.LayoutPhotoOpBinding
import cn.onlyloveyd.demo.databinding.LayoutQrDetectOpBinding
import cn.onlyloveyd.demo.ext.MediaStoreUtils
import cn.onlyloveyd.demo.ext.showMat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Scalar
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import org.opencv.objdetect.QRCodeDetector
import java.io.File

/**
 * QR二维码检测
 * author: yidong
 * 2020/10/27
 */
class QRDetectActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityQrDetectBinding
    private lateinit var mQRCodeDetector: QRCodeDetector

    private var mPhotoSavePath = ""
    private lateinit var mUri: Uri
    private lateinit var mSource: Mat
    private lateinit var mGray: Mat
    private lateinit var mOperationSheet: BottomSheetDialog
    private lateinit var mSheetBinding: LayoutQrDetectOpBinding

    private lateinit var mPhotoSheet: BottomSheetDialog
    private lateinit var mPhotoOpBinding: LayoutPhotoOpBinding


    // 请求相机权限
    private val requestCameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                mPhotoSavePath =
                    cacheDir.path + File.separator + "${System.currentTimeMillis()}.png"
                mUri = MediaStoreUtils.getIntentUri(this, File(mPhotoSavePath))
                requestCamera.launch(mUri)
            } else {
                Toast.makeText(applicationContext, "无相机权限", Toast.LENGTH_SHORT).show()
            }
        }

    // 请求外部存储权限
    private val requestStoragePermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                pickImage.launch("image/*")
            } else {
                Toast.makeText(applicationContext, "无存储权限", Toast.LENGTH_SHORT).show()
            }
        }


    private val requestCamera = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            val bgr = Imgcodecs.imread(mPhotoSavePath, Imgcodecs.IMREAD_COLOR)
            if (bgr.empty()) {
                Toast.makeText(applicationContext, "读取拍照结果失败", Toast.LENGTH_SHORT).show()
                return@registerForActivityResult
            } else {
                Imgproc.cvtColor(bgr, mSource, Imgproc.COLOR_BGR2RGB)
                Imgproc.cvtColor(bgr, mGray, Imgproc.COLOR_BGR2GRAY)
                mBinding.ivLena.showMat(mSource)
            }
        } else {
            Toast.makeText(applicationContext, "拍照失败", Toast.LENGTH_SHORT).show()
        }
    }

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) {
            val filePath = MediaStoreUtils.getMediaPath(this, it)
            if (filePath.isNullOrEmpty()) {
                Toast.makeText(applicationContext, "读取图片失败", Toast.LENGTH_SHORT).show()
                return@registerForActivityResult
            }
            val bgr = Imgcodecs.imread(filePath, Imgcodecs.IMREAD_COLOR)
            if (bgr.empty()) {
                Toast.makeText(applicationContext, "读取图片失败", Toast.LENGTH_SHORT).show()
                return@registerForActivityResult
            } else {
                Imgproc.cvtColor(bgr, mSource, Imgproc.COLOR_BGR2RGB)
                Imgproc.cvtColor(bgr, mGray, Imgproc.COLOR_BGR2GRAY)
                mBinding.ivLena.showMat(mSource)
            }
        } else {
            Toast.makeText(applicationContext, "选图失败", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_qr_detect)
        mQRCodeDetector = QRCodeDetector()
        mSource = Mat()
        mGray = Mat()
        val bgr = Utils.loadResource(this, R.drawable.qrcode)
        Imgproc.cvtColor(bgr, mSource, Imgproc.COLOR_BGR2RGB)
        Imgproc.cvtColor(bgr, mGray, Imgproc.COLOR_BGR2GRAY)
        mBinding.ivLena.showMat(mSource)
        createDialog()
    }

    private fun createDialog() {
        mOperationSheet = BottomSheetDialog(this)
        mSheetBinding = LayoutQrDetectOpBinding.inflate(layoutInflater, null, false)
        mOperationSheet.setContentView(mSheetBinding.root)
        mSheetBinding.tvDetect.setOnClickListener {
            mOperationSheet.dismiss()
            doDetect()
        }
        mSheetBinding.tvDecode.setOnClickListener {
            mOperationSheet.dismiss()
            doDecode()
        }

        mPhotoSheet = BottomSheetDialog(this)
        mPhotoOpBinding = LayoutPhotoOpBinding.inflate(layoutInflater, null, false)
        mPhotoSheet.setContentView(mPhotoOpBinding.root)
        mPhotoOpBinding.tvCamera.setOnClickListener {
            mPhotoSheet.dismiss()
            requestCameraPermission.launch(
                Manifest.permission.CAMERA
            )
        }
        mPhotoOpBinding.tvPhoto.setOnClickListener {
            mPhotoSheet.dismiss()
            requestStoragePermission.launch(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )

        }
    }

    private fun doDetect() {
        val points = Mat()
        val isHasQr = mQRCodeDetector.detect(mSource, points)
        if (isHasQr) {
            val pointArr = FloatArray(8)
            points.get(0, 0, pointArr)
            Log.d(App.TAG, pointArr.toList().toString())
            val tmp = mSource.clone()
            for (i in pointArr.indices step 2) {
                val start = Point(pointArr[i % 8].toDouble(), pointArr[(i + 1) % 8].toDouble())
                val end = Point(pointArr[(i + 2) % 8].toDouble(), pointArr[(i + 3) % 8].toDouble())
                Imgproc.line(tmp, start, end, Scalar(255.0, 0.0, 0.0), 8, Imgproc.LINE_8)
            }
            mBinding.ivResult.showMat(tmp)
            tmp.release()
        }
    }

    private fun doDecode() {
        val points = Mat()
        val isHasQr = mQRCodeDetector.detect(mGray, points)
        if (isHasQr) {
            val result = mQRCodeDetector.decode(mGray, points)
            if (result.isEmpty()) {
                Toast.makeText(applicationContext, "无法解码", Toast.LENGTH_SHORT).show()
            } else {
                Snackbar.make(mBinding.root, "解码结果：$result", 3000).show()
            }
            Log.d(App.TAG, result)
        } else {
            Toast.makeText(applicationContext, "未检测到QRCode", Toast.LENGTH_SHORT).show()
        }
    }

    private fun selectMedia() {
        if (this::mPhotoSheet.isInitialized) {
            mPhotoSheet.show()
        }
    }

    private fun selectOps() {
        if (this::mOperationSheet.isInitialized) {
            mOperationSheet.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_qr_detect, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_pick_photo -> selectMedia()
            R.id.menu_qr_ops -> selectOps()
        }
        return true
    }

    override fun onDestroy() {
        mSource.release()
        mGray.release()
        super.onDestroy()
    }
}