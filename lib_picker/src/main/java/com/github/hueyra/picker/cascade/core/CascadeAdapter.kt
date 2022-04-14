package com.github.hueyra.picker.cascade.core

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.github.hueyra.picker.R

class CascadeAdapter(val context: Context, val list: List<ICascadePickerData>) :
    RecyclerView.Adapter<CascadeAdapter.VH>() {

    private var mOnItemClickListener: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(context).inflate(R.layout.item_cascade_picker, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val entity = list[position]
        holder.tvName.text = entity.getPickerValue()
        holder.ivSelect.visibility = if (entity.isCascadePicked) View.VISIBLE else View.GONE
        holder.llContent.setOnClickListener {
            holder.llContent.postDelayed({
                mOnItemClickListener?.invoke(position)
            }, 200)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tv_title)
        val ivSelect: ImageView = itemView.findViewById(R.id.iv_selected)
        val llContent: View = itemView.findViewById(R.id.ll_content)
    }

    fun setOnItemClickListener(l: (Int) -> Unit) {
        mOnItemClickListener = l
    }
}