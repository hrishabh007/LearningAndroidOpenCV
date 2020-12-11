package cn.onlyloveyd.demo.card

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cn.onlyloveyd.demo.databinding.FragmentCardBinding

class CardFragment : Fragment() {
    lateinit var mBinding: FragmentCardBinding

    companion object {
        fun getInstance(card: Card): CardFragment {
            val fragment = CardFragment()
            fragment.arguments = card.toBundle()
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentCardBinding.inflate(inflater, container, false)
        if (arguments != null) {
            val bitmap = arguments?.getParcelable<Bitmap>(Card.ARG_BITMAP)
            val name = arguments?.getString(Card.ARG_NAME)
            mBinding.ivCard.setImageDrawable(BitmapDrawable(context?.resources, bitmap))
            mBinding.tvTitle.text = name
        }
        return mBinding.root
    }
}