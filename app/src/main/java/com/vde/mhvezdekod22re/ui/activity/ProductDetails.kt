package com.vde.mhvezdekod22re.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.vde.mhvezdekod22re.R
import com.vde.mhvezdekod22re.databinding.ActivityProductDetailsBinding
import com.vde.mhvezdekod22re.models.Products
import com.vde.mhvezdekod22re.ui.adapters.ProductInfoAdapter
import com.vde.mhvezdekod22re.utils.Const
import com.vde.mhvezdekod22re.utils.L

class ProductDetails : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailsBinding
    private lateinit var adapter: ProductInfoAdapter
    private lateinit var product: Products

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setProduct()

        setAdapter()
        setListener()
    }

    private fun setProduct(){
        product = Gson().fromJson(intent.getStringExtra(Const.PRODUCT_GSON), Products::class.java)
        L.d("product = $product")
        binding.product = product
        binding.addToBtn.text = "${getString(R.string.toCard)} ${(product.price_current.toDouble()/100).toInt()} ₽"
    }

    private fun setAdapter(){
        adapter = ProductInfoAdapter(product)
        binding.detailsRv.layoutManager = LinearLayoutManager(this)
        binding.detailsRv.addItemDecoration(DividerItemDecoration(binding.detailsRv.context, DividerItemDecoration.VERTICAL))
        binding.detailsRv.adapter = adapter
    }

    private fun setListener(){
        binding.arrowLeftBtn.setOnClickListener {
            onBackPressed()
        }

        binding.addToBtn.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }



    }
}