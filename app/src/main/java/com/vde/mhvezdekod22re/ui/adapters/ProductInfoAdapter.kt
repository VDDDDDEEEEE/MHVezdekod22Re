package com.vde.mhvezdekod22re.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vde.mhvezdekod22re.R
import com.vde.mhvezdekod22re.databinding.ItemProductDetailsBinding
import com.vde.mhvezdekod22re.models.ProductInfo
import com.vde.mhvezdekod22re.models.Products

class ProductInfoAdapter(val products: Products) :
    RecyclerView.Adapter<ProductInfoAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemProductDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(productInfo: ProductInfo) {
            binding.nameTv.text = productInfo.name
            binding.countTv.text = productInfo.count
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemProductDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val productInfo: ProductInfo = when (position) {
            0 -> {
                ProductInfo(
                    context.getString(R.string.weight),
                    "${products.measure} ${products.measure_unit}"
                )
            }
            1 -> {
                ProductInfo(
                    context.getString(R.string.energy),
                    "${products.energy_per_100_grams} ${context.getString(R.string.energy_unit)} "
                )
            }
            2 -> {
                ProductInfo(
                    context.getString(R.string.protein),
                    "${products.proteins_per_100_grams} ${products.measure_unit}"
                )
            }
            3 -> {
                ProductInfo(
                    context.getString(R.string.fats),
                    "${products.fats_per_100_grams} ${products.measure_unit}"
                )
            }
            else -> {
                ProductInfo(
                    context.getString(R.string.carbohydrates),
                    "${products.carbohydrates_per_100_grams} ${products.measure_unit}"
                )
            }
        }
        holder.bind(productInfo)
    }

    override fun getItemCount(): Int {
        return 5
    }

    //override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    //        val binding =
    //            ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    //        return ViewHolder(
    //            binding
    //        )
    //    }
    //
    //    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    //        val item = list[position]
    //        holder.bind(item)
    //    }
    //
    //    override fun getItemCount(): Int {
    //        return list.size
    //    }
}