package com.vde.mhvezdekod22re.ui.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vde.mhvezdekod22re.databinding.ItemCartDetailsBinding
import com.vde.mhvezdekod22re.models.Products
import com.vde.mhvezdekod22re.utils.L

class ProductInCartAdapter(var list: ArrayList<Products>, val onItemClick: (position:Int, priceChange: Int, product: Products) -> Unit) :
    RecyclerView.Adapter<ProductInCartAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemCartDetailsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(itemCard: Products, position: Int, onItemClick: (position:Int, priceChange: Int, product: Products) -> Unit) {
            L.d("bind itemCard = ${itemCard.name}")
            L.d("bind count = ${itemCard.count}")
            //var count = itemCard.count
            //binding.nameTv.text = itemCard.name
            //binding.weightTv.text = "${itemCard.measure} ${itemCard.measure_unit}"
            binding.strikeTV.paintFlags = binding.strikeTV.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            updatePrice(itemCard)
           /* binding.btn.setOnClickListener {
                itemCard.count += 1
                binding.count = itemCard.count
            }*/

            binding.plusBtn.setOnClickListener {
                itemCard.count += 1
                updatePrice(itemCard)
                onItemClick(position, (itemCard.price_current).toInt(), itemCard)
            }
            binding.minusBtn.setOnClickListener {
                itemCard.count -= 1
                updatePrice(itemCard)
                onItemClick(position, -(itemCard.price_current).toInt(), itemCard)

            }

            //binding.bigIv.setOnClickListener { onItemClick(position, itemCard) }
        }

        fun updatePrice(itemCard: Products){
            binding.count = itemCard.count
            binding.product = itemCard
            binding.priceTv.text = "${(itemCard.price_current.toDouble() / 100).toInt() * itemCard.count} ₽"
            if (itemCard.price_old != null) {
                binding.strikeTV.text = "${(itemCard.price_old.toDouble() / 100).toInt() * itemCard.count} ₽"
                binding.strikeTV.visibility = View.VISIBLE
            } else {
                binding.strikeTV.visibility = View.GONE
            }
        }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCartDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item, position, onItemClick)
        //holder.itemView.setOnClickListener { onItemClick(position ,item) }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}