package cn.onlyloveyd.demo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import cn.onlyloveyd.demo.databinding.RvItemImageTextBinding
import cn.onlyloveyd.demo.ext.showMat

/**
 * 图片配文字适配器
 * author: yidong
 * 2020/12/5
 */
class ImageTextAdapter(
    private val context: Context,
    private var dataList: MutableList<ImageTextObject>
) : RecyclerView.Adapter<ImageTextAdapter.ImageTextViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageTextViewHolder {
        val binding = RvItemImageTextBinding.inflate(LayoutInflater.from(context), parent, false)
        return ImageTextViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageTextViewHolder, position: Int) {
        val item = dataList[position]
        val binding = holder.binding as RvItemImageTextBinding
        binding.icon.showMat(item.icon)
        binding.text.text = item.text
    }

    override fun getItemCount() = dataList.size

    class ImageTextViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)
}
