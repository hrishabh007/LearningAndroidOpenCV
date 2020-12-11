package cn.onlyloveyd.demo.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cn.onlyloveyd.demo.R
import cn.onlyloveyd.demo.card.Stat
import cn.onlyloveyd.demo.databinding.ActivityConnectedComponentsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.opencv.android.Utils
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Rect
import org.opencv.core.Scalar
import org.opencv.imgproc.Imgproc

/**
 * 连通域分析
 * author: yidong
 * 2020/6/7
 */
class ConnectedComponentsActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityConnectedComponentsBinding
    private lateinit var mBinary: Mat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_connected_components)

        val bgr = Utils.loadResource(this, R.drawable.number)
        val gray = Mat()
        Imgproc.cvtColor(bgr, gray, Imgproc.COLOR_BGR2GRAY)
        mBinary = Mat()
        Imgproc.threshold(gray, mBinary, 50.0, 255.0, Imgproc.THRESH_BINARY)
        showMat(mBinding.ivLena, mBinary)

        bgr.release()
        gray.release()
    }

    private fun showLoading() {
        mBinding.progressBar.show()
    }

    private fun dismissLoading() {
        mBinding.progressBar.hide()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_connected_componenets, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.connected_components -> {
                connectedComponent()
            }
            R.id.connected_components_with_algorithm_4_wu -> {
                connectedComponentsWithAlgorithm(4, Imgproc.CCL_WU)
            }
            R.id.connected_components_with_algorithm_8_wu -> {
                connectedComponentsWithAlgorithm(8, Imgproc.CCL_WU)
            }
            R.id.connected_components_with_algorithm_4_grana -> {
                connectedComponentsWithAlgorithm(4, Imgproc.CCL_GRANA)
            }
            R.id.connected_components_with_algorithm_8_grana -> {
                connectedComponentsWithAlgorithm(8, Imgproc.CCL_GRANA)
            }
            R.id.connected_components_with_stats_with_algorithm -> {
                connectedComponentsWithStatsWithAlgorithm()
            }
        }
        return true
    }

    private fun showMat(view: ImageView, source: Mat) {
        val bitmap = Bitmap.createBitmap(source.width(), source.height(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(source, bitmap)
        view.setImageBitmap(bitmap)
    }

    private fun connectedComponent() {
        val labels = Mat()
        val count = Imgproc.connectedComponents(mBinary, labels)
        labels.convertTo(labels, CvType.CV_8U)
        showLoading()
        GlobalScope.launch(Dispatchers.IO) {
            drawConnectedComponent(count, labels)
        }
    }

    private fun connectedComponentsWithAlgorithm(connectivity: Int, algorithm: Int) {
        val labels = Mat()
        val count = Imgproc.connectedComponentsWithAlgorithm(
            mBinary,
            labels,
            connectivity,
            CvType.CV_32S,
            algorithm
        )
        labels.convertTo(labels, CvType.CV_8U)
        showLoading()
        GlobalScope.launch(Dispatchers.IO) {
            drawConnectedComponent(count, labels)
        }
    }

    private fun connectedComponentsWithStatsWithAlgorithm() {
        val labels = Mat()
        val stats = Mat()
        val centroids = Mat()
        val labelCount = Imgproc.connectedComponentsWithStatsWithAlgorithm(
            mBinary,
            labels,
            stats,
            centroids,
            8,
            CvType.CV_32S,
            Imgproc.CCL_GRANA
        )
        labels.convertTo(labels, CvType.CV_8U)
        val statList = mutableListOf<Stat>()
        for (count in 0 until labelCount) {
            val stat = Stat(
                centroids.get(count, 0)?.get(0) ?: 0.0,
                centroids.get(count, 1)?.get(0) ?: 0.0,
                stats.get(count, 0)?.get(0)?.toInt() ?: 0,
                stats.get(count, 1)?.get(0)?.toInt() ?: 0,
                stats.get(count, 2)?.get(0)?.toInt() ?: 0,
                stats.get(count, 3)?.get(0)?.toInt() ?: 0,
                count
            )
            statList.add(stat)
        }
        showLoading()
        GlobalScope.launch(Dispatchers.IO) {
            drawConnectedComponentWithStats(labelCount, labels, statList)
        }
    }

    private fun drawConnectedComponent(count: Int, labels: Mat) {
        val result = Mat.zeros(labels.rows(), labels.cols(), CvType.CV_8UC3)
        val color = arrayListOf<Scalar>()
        for (index in 0..count) {
            val scalar = Scalar(
                (Math.random() * 255) + 1,
                (Math.random() * 255) + 1,
                (Math.random() * 255) + 1
            )
            color.add(scalar)
        }
        for (row in 0..labels.rows()) {
            for (col in 0..labels.cols()) {
                val label = labels.get(row, col)?.get(0)?.toInt() ?: 0
                if (label == 0) {
                    continue
                } else {
                    result.put(
                        row,
                        col,
                        color[label].`val`[0],
                        color[label].`val`[1],
                        color[label].`val`[2]
                    )
                }
            }
        }
        GlobalScope.launch(Dispatchers.Main) {
            dismissLoading()
            showMat(mBinding.ivResult, result)
            result.release()
        }
        labels.release()
    }

    private fun drawConnectedComponentWithStats(
        count: Int,
        labels: Mat,
        statList: MutableList<Stat>
    ) {
        val result = Mat.zeros(labels.rows(), labels.cols(), CvType.CV_8UC3)
        val color = arrayListOf<Scalar>()
        for (index in 0..count) {
            val scalar = Scalar(
                (Math.random() * 255) + 1,
                (Math.random() * 255) + 1,
                (Math.random() * 255) + 1
            )
            color.add(scalar)
        }
        for (row in 0..labels.rows()) {
            for (col in 0..labels.cols()) {
                val label = labels.get(row, col)?.get(0)?.toInt() ?: 0
                if (label == 0) {
                    continue
                } else {
                    result.put(
                        row,
                        col,
                        color[label].`val`[0],
                        color[label].`val`[1],
                        color[label].`val`[2]
                    )
                }
            }
        }

        for (index in 0 until statList.size) {
            val stat = statList[index]
            val rect = Rect(stat.left, stat.top, stat.width, stat.height)
            Imgproc.rectangle(result, rect, color[stat.label], 10)
        }
        GlobalScope.launch(Dispatchers.Main) {
            dismissLoading()
            showMat(mBinding.ivResult, result)
            result.release()
        }
        labels.release()
    }
}