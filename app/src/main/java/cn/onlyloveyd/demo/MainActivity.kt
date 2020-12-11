package cn.onlyloveyd.demo

import android.R
import android.app.ListActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.SimpleAdapter
import cn.onlyloveyd.demo.ui.*

class MainActivity : ListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listAdapter = SimpleAdapter(
            this, getData(),
            R.layout.simple_list_item_1, arrayOf("title"),
            intArrayOf(R.id.text1)
        )
    }

    private fun getData(): List<Map<String, Any>> {
        val myData = mutableListOf<Map<String, Any>>()

        myData.add(
            mapOf(
                "title" to "读取和保存图像",
                "intent" to activityToIntent(ReadAndWriteActivity::class.java.name)
            )
        )
        myData.add(
            mapOf(
                "title" to "Mat操作",
                "intent" to activityToIntent(MatOperationActivity::class.java.name)
            )
        )
        myData.add(
            mapOf(
                "title" to "图像像素值统计",
                "intent" to activityToIntent(PixelStatisticsActivity::class.java.name)
            )
        )
        myData.add(
            mapOf(
                "title" to "绘制几何图形",
                "intent" to activityToIntent(ShapeRenderActivity::class.java.name)
            )
        )

        myData.add(
            mapOf(
                "title" to "调整对比度和亮度",
                "intent" to activityToIntent(ContrastBrightnessActivity::class.java.name)
            )
        )
        myData.add(
            mapOf(
                "title" to "颜色模型装换",
                "intent" to activityToIntent(ColorTransferActivity::class.java.name)
            )
        )

        myData.add(
            mapOf(
                "title" to "通道分离合并",
                "intent" to activityToIntent(ChannelSplitMergeActivity::class.java.name)
            )
        )

        myData.add(
            mapOf(
                "title" to "图像二值化",
                "intent" to activityToIntent(ImageBinaryzationActivity::class.java.name)
            )
        )

        myData.add(
            mapOf(
                "title" to "LUT查找表",
                "intent" to activityToIntent(LutActivity::class.java.name)
            )
        )

        myData.add(
            mapOf(
                "title" to "透视变换",
                "intent" to activityToIntent(PerspectiveTransformationActivity::class.java.name)
            )
        )

        myData.add(
            mapOf(
                "title" to "仿射变换",
                "intent" to activityToIntent(AffineActivity::class.java.name)
            )
        )
        myData.add(
            mapOf(
                "title" to "极坐标变换",
                "intent" to activityToIntent(PolarActivity::class.java.name)
            )
        )

        myData.add(
            mapOf(
                "title" to "图像金字塔",
                "intent" to activityToIntent(GLPyramidActivity::class.java.name)
            )
        )

        myData.add(
            mapOf(
                "title" to "图像直方图",
                "intent" to activityToIntent(HistActivity::class.java.name)
            )
        )

        myData.add(
            mapOf(
                "title" to "图像卷积",
                "intent" to activityToIntent(Filter2DActivity::class.java.name)
            )
        )

        myData.add(
            mapOf(
                "title" to "椒盐噪声",
                "intent" to activityToIntent(SaltPepperNoiseActivity::class.java.name)
            )
        )

        myData.add(
            mapOf(
                "title" to "高斯噪声",
                "intent" to activityToIntent(GaussianNoiseActivity::class.java.name)
            )
        )

        myData.add(
            mapOf(
                "title" to "均值滤波",
                "intent" to activityToIntent(MeanFilterActivity::class.java.name)
            )
        )

        myData.add(
            mapOf(
                "title" to "方框滤波",
                "intent" to activityToIntent(BoxFilterActivity::class.java.name)
            )
        )

        myData.add(
            mapOf(
                "title" to "高斯滤波",
                "intent" to activityToIntent(GaussianFilterActivity::class.java.name)
            )
        )

        myData.add(
            mapOf(
                "title" to "双边滤波",
                "intent" to activityToIntent(BilateralFilterActivity::class.java.name)
            )
        )

        myData.add(
            mapOf(
                "title" to "边缘检测",
                "intent" to activityToIntent(EdgeDetectionActivity::class.java.name)
            )
        )

        myData.add(
            mapOf(
                "title" to "Sobel算子边缘检测",
                "intent" to activityToIntent(SobelEdgeDetectionActivity::class.java.name)
            )
        )

        myData.add(
            mapOf(
                "title" to "Scharr算子边缘检测",
                "intent" to activityToIntent(ScharrEdgeDetectionActivity::class.java.name)
            )
        )

        myData.add(
            mapOf(
                "title" to "Laplacian算子边缘检测",
                "intent" to activityToIntent(LaplacianEdgeDetectionActivity::class.java.name)
            )
        )

        myData.add(
            mapOf(
                "title" to "Canny边缘检测",
                "intent" to activityToIntent(CannyEdgeDetectionActivity::class.java.name)
            )
        )

        myData.add(
            mapOf(
                "title" to "连通域分析",
                "intent" to activityToIntent(ConnectedComponentsActivity::class.java.name)
            )
        )

        myData.add(
            mapOf(
                "title" to "图像距离变换",
                "intent" to activityToIntent(DistanceTransformActivity::class.java.name)
            )
        )

        myData.add(
            mapOf(
                "title" to "图像腐蚀",
                "intent" to activityToIntent(ErosionActivity::class.java.name)
            )
        )
        myData.add(
            mapOf(
                "title" to "图像膨胀",
                "intent" to activityToIntent(DilateActivity::class.java.name)
            )
        )
        myData.add(
            mapOf(
                "title" to "形态学操作",
                "intent" to activityToIntent(MorphologyExActivity::class.java.name)
            )
        )

        myData.add(
            mapOf(
                "title" to "霍夫直线检测",
                "intent" to activityToIntent(HoughLineDetectActivity::class.java.name)
            )
        )


        myData.add(
            mapOf(
                "title" to "霍夫圆检测",
                "intent" to activityToIntent(HoughCircleDetectActivity::class.java.name)
            )
        )

        myData.add(
            mapOf(
                "title" to "直线拟合",
                "intent" to activityToIntent(FitLineActivity::class.java.name)
            )
        )

        myData.add(
            mapOf(
                "title" to "轮廓检测",
                "intent" to activityToIntent(FindContoursActivity::class.java.name)
            )
        )

        myData.add(
            mapOf(
                "title" to "轮廓外接多边形",
                "intent" to activityToIntent(ContourPolyActivity::class.java.name)
            )
        )

        myData.add(
            mapOf(
                "title" to "凸包检测",
                "intent" to activityToIntent(ConvexHullActivity::class.java.name)
            )
        )
        myData.add(
            mapOf(
                "title" to "模板匹配",
                "intent" to activityToIntent(MatchTemplateActivity::class.java.name)
            )
        )
        myData.add(
            mapOf(
                "title" to "QR二维码检测",
                "intent" to activityToIntent(QRDetectActivity::class.java.name)
            )
        )
        myData.add(
            mapOf(
                "title" to "图像分割（漫水填充法）",
                "intent" to activityToIntent(FloodFillActivity::class.java.name)
            )
        )
        myData.add(
            mapOf(
                "title" to "图像分割（分水岭法）",
                "intent" to activityToIntent(WaterShedActivity::class.java.name)
            )
        )
        myData.add(
            mapOf(
                "title" to "图像分割（Grabcut）",
                "intent" to activityToIntent(GrabcutActivity::class.java.name)
            )
        )
        myData.add(
            mapOf(
                "title" to "图像分割（均值漂移）",
                "intent" to activityToIntent(MeanShiftActivity::class.java.name)
            )
        )
        myData.add(
            mapOf(
                "title" to "图像修复",
                "intent" to activityToIntent(InPaintActivity::class.java.name)
            )
        )
        myData.add(
            mapOf(
                "title" to "非真实渲染",
                "intent" to activityToIntent(NonPhotoRealisticRenderingActivity::class.java.name)
            )
        )
        myData.add(
            mapOf(
                "title" to "脱色",
                "intent" to activityToIntent(DecolorActivity::class.java.name)
            )
        )
        return myData
    }

    private fun activityToIntent(activity: String): Intent =
        Intent(Intent.ACTION_VIEW).setClassName(this.packageName, activity)

    override fun onListItemClick(listView: ListView, view: View, position: Int, id: Long) {
        val map = listView.getItemAtPosition(position) as Map<*, *>

        val intent = Intent(map["intent"] as Intent)
        intent.putExtra("title", map["title"] as String)
        intent.addCategory(Intent.CATEGORY_SAMPLE_CODE)
        startActivity(intent)
    }
}
