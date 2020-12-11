package cn.onlyloveyd.demo.ext

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import cn.onlyloveyd.demo.R
import cn.onlyloveyd.demo.card.Card
import cn.onlyloveyd.demo.card.CardFragment
import cn.onlyloveyd.demo.databinding.ActivityCardGalleryBinding
import org.opencv.android.Utils
import org.opencv.core.Mat

abstract class CardGalleryActivity : AppCompatActivity() {
    lateinit var mBinding: ActivityCardGalleryBinding

    var cards = arrayListOf<Card>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_card_gallery
        )
        supportActionBar?.title = intent.getStringExtra("title")
        buildCards()
        mBinding.container.adapter = object : FragmentStateAdapter(this) {
            override fun createFragment(position: Int): Fragment {
                return CardFragment.getInstance(cards[position])
            }

            override fun getItemCount(): Int {
                return cards.size
            }
        }

        mBinding.container.offscreenPageLimit = 1
        mBinding.container.apply {
            offscreenPageLimit = 1
            val recyclerView = getChildAt(0) as RecyclerView
            recyclerView.apply {
                val padding = 80
                setPadding(padding, 0, padding, 0)
                clipToPadding = false
            }
        }
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(AlphaAndScalePageTransformer())
        compositePageTransformer.addTransformer(MarginPageTransformer(10))
        mBinding.container.setPageTransformer(compositePageTransformer)
    }

    abstract fun buildCards()

    fun decodeResourceToBitmap(resId: Int = R.drawable.road): Bitmap {
        val option = BitmapFactory.Options()
        option.inScaled = false
        return BitmapFactory.decodeResource(
            resources,
            resId,
            option
        )
    }

    fun addCardFromMat(name: String, mat: Mat, density: Int = 0) {
        val bitmap = Bitmap.createBitmap(mat.width(), mat.height(), Bitmap.Config.ARGB_8888)
        if (density > 0) {
            bitmap.density = density
        }
        Utils.matToBitmap(mat, bitmap)
        cards.add(Card(name, bitmap))
    }

    fun printMatInfo(mat: Mat) {
        val cols = mat.cols()
        val rows = mat.rows()
        val channels = mat.channels()
        Log.e("LearningAndroidOpenCV", "cols = $cols rows = $rows channels = $channels")
    }

    override fun onDestroy() {
        for (card in cards) {
            card.recycle()
        }
        super.onDestroy()
    }

}