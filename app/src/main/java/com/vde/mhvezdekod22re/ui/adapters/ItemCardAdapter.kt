package com.vde.mhvezdekod22re.ui.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vde.mhvezdekod22re.databinding.ItemCardBinding
import com.vde.mhvezdekod22re.models.Products
import com.vde.mhvezdekod22re.utils.L

class ItemCardAdapter(var list: ArrayList<Products>, val onItemClick: (Int, Products) -> Unit, val itemPriceUpdate: (price: Int) -> Unit) :
    RecyclerView.Adapter<ItemCardAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(itemCard: Products, position: Int, onItemClick: (Int, Products) -> Unit, itemPriceUpdate: (price: Int) -> Unit) {
            L.d("bind itemCard = ${itemCard.name}")
            L.d("bind count = ${itemCard.count}")

            binding.weightTv.text = "${itemCard.measure} ${itemCard.measure_unit}"
            binding.strikeTV.paintFlags = binding.strikeTV.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            updatePrice(itemCard ,0)

            binding.isSpicy = itemCard.tag_ids.any { it == 4 }
            binding.isVegan = itemCard.tag_ids.any { it == 2 }
            binding.btn.setOnClickListener {
                updatePrice(itemCard, 1)
                itemPriceUpdate((itemCard.price_current).toInt())
            }

            binding.plusBtn.setOnClickListener {
                updatePrice(itemCard, 1)
                itemPriceUpdate((itemCard.price_current).toInt())
            }
            binding.minusBtn.setOnClickListener {
                updatePrice(itemCard, -1)
                itemPriceUpdate(-(itemCard.price_current).toInt())
            }

            binding.bigIv.setOnClickListener { onItemClick(position, itemCard) }
        }
        fun updatePrice(itemCard: Products, isPLus: Int){
            if(isPLus > 0) {
                itemCard.count += 1
            }else if(isPLus < 0){
                itemCard.count -= 1
            }
            binding.count = itemCard.count
            binding.product = itemCard
            val itemCount = if(itemCard.count != 0) itemCard.count else 1
            binding.priceTv.text = "${(itemCard.price_current.toDouble() / 100).toInt() * itemCount} ₽"
            if (itemCard.price_old != null) {
                binding.strikeTV.text = "${(itemCard.price_old.toDouble() / 100).toInt() * itemCount} ₽"
                binding.strikeTV.visibility = View.VISIBLE
            } else {
                binding.strikeTV.visibility = View.GONE
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item, position, onItemClick, itemPriceUpdate)
        //holder.itemView.setOnClickListener { onItemClick(position ,item) }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}