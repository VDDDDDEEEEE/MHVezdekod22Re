package com.vde.mhvezdekod22re.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vde.mhvezdekod22re.databinding.ItemTagFilterBinding
import com.vde.mhvezdekod22re.models.Tags

class TagsAdapter(var tagsList: ArrayList<Tags>, val checkClick: (Int,Tags) -> Unit) : RecyclerView.Adapter<TagsAdapter.ViewHolder>() {


    class ViewHolder(private val binding: ItemTagFilterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(tags: Tags, checkClick: (Int,Tags) -> Unit) {
            binding.tagCheckBox.isChecked = tags.isChecked
            binding.tagNameTv.text = tags.name
            binding.tagCheckBox.setOnCheckedChangeListener{view, isChecked ->
                tags.isChecked = !tags.isChecked
                checkClick(adapterPosition,tags)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemTagFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = tagsList[position]
        holder.bind(item, checkClick)
    }

    override fun getItemCount(): Int {
        return tagsList.size
    }



}