package cn.onlyloveyd.demo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.onlyloveyd.demo.R
import cn.onlyloveyd.demo.adapter.ImageTextAdapter
import cn.onlyloveyd.demo.adapter.ImageTextObject
import cn.onlyloveyd.demo.databinding.ActivityDecolorBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc
import org.opencv.photo.Photo

/**
 * 脱色
 * author: yidong
 * 2020/12/5
 */
class DecolorActivity : AppCompatActivity() {

    private val mBinding: ActivityDecolorBinding by lazy {
        ActivityDecolorBinding.inflate(layoutInflater)
    }

    private val mList = mutableListOf<ImageTextObject>()
    private lateinit var mAdapter: ImageTextAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        mAdapter = ImageTextAdapter(this, mList)
        mBinding.container.adapter = mAdapter
        val bgr = Utils.loadResource(this, R.drawable.ceil)
        val rgb = Mat()
        Imgproc.cvtColor(bgr, rgb, Imgproc.COLOR_BGR2RGB)
        val gray = Mat()
        val dst = Mat()
        val boost = Mat()
        mBinding.isLoading = true
        GlobalScope.launch(Dispatchers.IO) {
            Imgproc.cvtColor(rgb, gray, Imgproc.COLOR_RGB2GRAY)
            Photo.decolor(rgb, dst, boost)
            launch(Dispatchers.Main) {
                mList.add(ImageTextObject(rgb, "原图"))
                mList.add(ImageTextObject(gray, "RGB2GRAY"))
                mList.add(ImageTextObject(dst, "DeColor"))
                mList.add(ImageTextObject(boost, "ColorBoosting"))
                mAdapter.notifyItemRangeInserted(0, 4)
                mBinding.isLoading = false
            }
        }
    }
}