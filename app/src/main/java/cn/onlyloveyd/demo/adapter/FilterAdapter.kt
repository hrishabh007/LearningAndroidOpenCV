package cn.onlyloveyd.demo.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import cn.onlyloveyd.demo.databinding.RvItemFilterBinding

/**
 * 九宫格适配器
 * author: yidong
 * 2020/3/28
 */
class FilterAdapter(private val context: Context, private var dataList: ArrayList<Float>) :
    RecyclerView.Adapter<FilterAdapter.FilterViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val binding = RvItemFilterBinding.inflate(LayoutInflater.from(context), parent, false)
        return FilterViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        val binding = holder.binding as RvItemFilterBinding
        val data = dataList[position]
        if (binding.value.tag != null && binding.value.tag is TextWatcher) {
            binding.value.removeTextChangedListener(binding.value.tag as TextWatcher)
        }

        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                try {
                    dataList[position] = s.toString().toFloat()
                } catch (e: NumberFormatException) {

                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


            }
        }

        binding.value.addTextChangedListener(textWatcher)
        binding.value.tag = textWatcher

        binding.value.setText(data.toString())
    }

    fun setData(data: Array<Float>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }

    class FilterViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)
}