package cn.onlyloveyd.demo.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cn.onlyloveyd.demo.R
import cn.onlyloveyd.demo.databinding.ActivityReadAndWriteBinding
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.imgcodecs.Imgcodecs
import java.io.File
import java.io.FileOutputStream


/**
 * 读写图像
 * author: yidong
 * 2020/1/7
 */
class ReadAndWriteActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityReadAndWriteBinding
    private val sLeanName = "lena.png"
    private lateinit var mLenaPath: String
    private var currentImreadMode = Imgcodecs.IMREAD_UNCHANGED
        set(value) {
            field = value
            onImreadModeChange()
        }
    private var currentMat = Mat()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_read_and_write)
        supportActionBar?.title = intent.getStringExtra("title")
        mLenaPath = cacheDir.path + File.separator + sLeanName

        drawableToFile(
            R.drawable.lena,
            sLeanName
        )
        onImreadModeChange()
        mBinding.btSave.setOnClickListener {
            saveMatToStorage(currentMat)
        }
    }

    private fun loadLenaFromFile() {
        if (!File(mLenaPath).exists()) {
            Toast.makeText(this, "文件不存在", Toast.LENGTH_SHORT).show()
            return
        }
        currentMat = Imgcodecs.imread(mLenaPath, currentImreadMode)
        val bitmap =
            Bitmap.createBitmap(currentMat.width(), currentMat.height(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(currentMat, bitmap)
        mBinding.ivLena.setImageBitmap(bitmap)
    }

    private fun onImreadModeChange() {
        loadLenaFromFile()
        mBinding.tvMode.text = getModeName(currentImreadMode)
    }

    private fun saveMatToStorage(source: Mat) {
        val file = File(cacheDir.path + File.separator + "${System.currentTimeMillis()}.jpg")
        if (!file.exists()) {
            file.createNewFile()
        }
        Imgcodecs.imwrite(file.path, source)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_imread, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.IMREAD_UNCHANGED -> currentImreadMode = Imgcodecs.IMREAD_UNCHANGED
            R.id.IMREAD_GRAYSCALE -> currentImreadMode = Imgcodecs.IMREAD_GRAYSCALE
            R.id.IMREAD_COLOR -> currentImreadMode = Imgcodecs.IMREAD_COLOR
            R.id.IMREAD_ANYDEPTH -> currentImreadMode = Imgcodecs.IMREAD_ANYDEPTH
            R.id.IMREAD_ANYCOLOR -> currentImreadMode = Imgcodecs.IMREAD_ANYCOLOR
            R.id.IMREAD_LOAD_GDAL -> currentImreadMode = Imgcodecs.IMREAD_LOAD_GDAL
            R.id.IMREAD_REDUCED_GRAYSCALE_2 -> currentImreadMode =
                Imgcodecs.IMREAD_REDUCED_GRAYSCALE_2
            R.id.IMREAD_REDUCED_COLOR_2 -> currentImreadMode = Imgcodecs.IMREAD_REDUCED_COLOR_2
            R.id.IMREAD_REDUCED_GRAYSCALE_4 -> currentImreadMode =
                Imgcodecs.IMREAD_REDUCED_GRAYSCALE_4
            R.id.IMREAD_REDUCED_COLOR_4 -> currentImreadMode = Imgcodecs.IMREAD_REDUCED_COLOR_4
            R.id.IMREAD_REDUCED_GRAYSCALE_8 -> currentImreadMode =
                Imgcodecs.IMREAD_REDUCED_GRAYSCALE_8
            R.id.IMREAD_REDUCED_COLOR_8 -> currentImreadMode = Imgcodecs.IMREAD_REDUCED_COLOR_8
            R.id.IMREAD_IGNORE_ORIENTATION -> currentImreadMode =
                Imgcodecs.IMREAD_IGNORE_ORIENTATION
        }
        return true
    }

    private fun getModeName(mode: Int): String {
        return when (mode) {
            Imgcodecs.IMREAD_UNCHANGED -> "IMREAD_UNCHANGED"
            Imgcodecs.IMREAD_GRAYSCALE -> "IMREAD_GRAYSCALE"
            Imgcodecs.IMREAD_COLOR -> "IMREAD_COLOR"
            Imgcodecs.IMREAD_ANYDEPTH -> "IMREAD_ANYDEPTH"
            Imgcodecs.IMREAD_ANYCOLOR -> "IMREAD_ANYCOLOR"
            Imgcodecs.IMREAD_LOAD_GDAL -> "IMREAD_LOAD_GDAL"
            Imgcodecs.IMREAD_REDUCED_GRAYSCALE_2 -> "IMREAD_REDUCED_GRAYSCALE_2"
            Imgcodecs.IMREAD_REDUCED_COLOR_2 -> "IMREAD_REDUCED_COLOR_2"
            Imgcodecs.IMREAD_REDUCED_GRAYSCALE_4 -> "IMREAD_REDUCED_GRAYSCALE_4"
            Imgcodecs.IMREAD_REDUCED_COLOR_4 -> "IMREAD_REDUCED_COLOR_4"
            Imgcodecs.IMREAD_REDUCED_GRAYSCALE_8 -> "IMREAD_REDUCED_GRAYSCALE_8"
            Imgcodecs.IMREAD_REDUCED_COLOR_8 -> "IMREAD_REDUCED_COLOR_8"
            Imgcodecs.IMREAD_IGNORE_ORIENTATION -> "IMREAD_IGNORE_ORIENTATION"
            else -> "IMREAD_UNCHANGED"
        }
    }

    /**
     * Drawable to File
     */
    private fun drawableToFile(drawableId: Int, fileName: String): File? {
        val bitmap: Bitmap = BitmapFactory.decodeResource(resources, drawableId)
        val defaultImgPath = "$cacheDir/$fileName"
        val file = File(defaultImgPath)
        val fOut = FileOutputStream(file)
        try {
            file.createNewFile()
            bitmap.compress(Bitmap.CompressFormat.PNG, 20, fOut)
            fOut.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            fOut.close()
        }
        return file
    }

}