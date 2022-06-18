package com.vde.mhvezdekod22re.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vde.mhvezdekod22re.databinding.ActivityCartBinding
import com.vde.mhvezdekod22re.models.Products
import com.vde.mhvezdekod22re.ui.adapters.ProductInCartAdapter
import com.vde.mhvezdekod22re.utils.Const
import com.vde.mhvezdekod22re.utils.L
import com.vde.mhvezdekod22re.utils.NumberTextWatcherForThousand

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var cartAdapter: ProductInCartAdapter
    private var productList = ArrayList<Products>()
    private var filteredProductList = ArrayList<Products>()
    private var price = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setProduct()
        setAdapter()
        setListener()
    }

    private fun setProduct(){

        productList = Gson().fromJson(intent.getStringExtra(Const.PRODUCT_LIST_GSON), object : TypeToken<ArrayList<Products>>() {}.type)
        L.d("productList = $productList")
        price = 0
        for (product in productList){
            if(product.count > 0){
                price += (product.price_current.toInt() * product.count)
                filteredProductList.add(product)
            }
        }
        setPrice(price)
    }


    private fun setAdapter(){
        cartAdapter = ProductInCartAdapter(filteredProductList){ position, priceChange, product ->
            price += priceChange
            setPrice(price)
            if(product.count == 0){
                cartAdapter.notifyItemRemoved(position)
                filteredProductList.remove(product)
                if(filteredProductList.isEmpty()){
                    binding.listIsEmpty = true
                }
            }
        }
        binding.cartItemRv.layoutManager = LinearLayoutManager(this)
        binding.cartItemRv.addItemDecoration(DividerItemDecoration(binding.cartItemRv.context, DividerItemDecoration.VERTICAL))
        binding.cartItemRv.adapter = cartAdapter
    }

    private fun setPrice(currentPrice: Int){
        val formattedPrice = NumberTextWatcherForThousand.getDecimalFormattedString((currentPrice.toDouble() / 100).toInt().toString())
        binding.addToBtn.text = "Заказать за ${formattedPrice} ₽"
    }

    override fun onBackPressed() {
        L.d("productList = $productList")
        if(filteredProductList.isNotEmpty()) {
            val data = intent.putExtra(Const.PRODUCT_LIST_GSON, Gson().toJson(filteredProductList))
            setResult(RESULT_OK, data)

        }else{
            setResult(RESULT_CANCELED)
        }
        finish()
        //super.onBackPressed()
    }
    
    private fun setListener(){
        binding.arrowLeftIv.setOnClickListener { onBackPressed() }
        binding.addToBtn.setOnClickListener { Toast.makeText(this, "Заказ успешно создан!", Toast.LENGTH_SHORT).show() }
    }
}