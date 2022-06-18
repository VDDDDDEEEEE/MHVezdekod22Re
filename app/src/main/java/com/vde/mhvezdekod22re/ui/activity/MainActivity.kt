package com.vde.mhvezdekod22re.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vde.mhvezdekod22re.data.JsonInfo
import com.vde.mhvezdekod22re.models.Category
import com.vde.mhvezdekod22re.models.Products
import com.vde.mhvezdekod22re.models.Tags
import com.vde.mhvezdekod22re.utils.L
import com.vde.mhvezdekod22re.databinding.ActivityMainBinding
import com.vde.mhvezdekod22re.ui.adapters.ItemCardAdapter
import com.vde.mhvezdekod22re.utils.Const
import com.vde.mhvezdekod22re.utils.DialogFilter
import com.vde.mhvezdekod22re.utils.HideKeyboard.hideSoftKeyBoard
import com.vde.mhvezdekod22re.utils.NumberTextWatcherForThousand
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var allProductList = ArrayList<Products>()
    private var productList = ArrayList<Products>()
    private var productListForCat = ArrayList<Products>()

    private var allCategoryList = ArrayList<Category>()
    private var categoryList = ArrayList<Category>()

    private var tagList = ArrayList<Tags>()

    //private var categories = ArrayList<Int>()
    private lateinit var itemCardAdapter: ItemCardAdapter
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var myTabLayout: TabLayout
    private var isUserScrolling = false
    private var isListGoingUp = true
    private lateinit var selectedProduct : Pair<Int, Products>
    private var price = 0
    //private var tabAndItemPosition = ArrayList<TabHelperCategory>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setAdapter()
        initTabLayout()
        setItem()

        setListener()
        //.d("product info = ${JsonInfo().getProductList()}")
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setListener(){
        binding.addToCartBtn.setOnClickListener {
            openCartActivity()
        }

        binding.filterIv.setOnClickListener {
            L.d("tagList = $tagList")
            DialogFilter(this, ArrayList(tagList)){
                tagList = it
                L.d("tagList = $tagList")
                filterProductList()
            }
        }

        binding.clearTextIv.setOnClickListener {
           binding.searchEditText.setText("")
        }

        binding.searchEditText.addTextChangedListener { editable ->
            L.d("editable = $editable")
            binding.editTextIsEmpty = editable.isNullOrEmpty()
            filterProductList((editable?: "").toString())

        }

        binding.searchIv.setOnClickListener {
            binding.isOpenSearch = true
            binding.searchEditText.requestFocus()
        }

        binding.closeSearchIv.setOnClickListener {
            binding.isOpenSearch = false
            binding.searchEditText.setText("")
        }
    }

   private fun filterProductList(searchWord: String = ""){
       var _helpingList = ArrayList<Products>()
       var countFilter = 0
       productList.clear()
       if(tagList.any{it.isChecked}) {
            val checkedTagList = ArrayList<Tags>()
               for(tag in tagList){
                   if(tag.isChecked){
                       countFilter ++
                       checkedTagList.add(tag)
                   }
               }
           L.d("checkedTagList = $checkedTagList")
           for (product in ArrayList(allProductList.sortedWith(compareBy { it.category_id }))) {
               L.d("product = $product")
               var productFilteredOk  = true
               for(i in 0 until countFilter){
                   if(!product.tag_ids.any { it == checkedTagList[i].id }){
                       productFilteredOk = false
                   }
               }

               //val info = product.tag_ids.all{ id -> checkedTagList.any{it.id == id}}
               L.d("product productFilteredOk = $productFilteredOk")
               if(productFilteredOk){
                   _helpingList.add(product)
               }
           }

       }else{
           _helpingList = ArrayList(allProductList.sortedWith(compareBy { it.category_id }))
       }

       if(searchWord.isNotEmpty()){
           for(_product in _helpingList){
               if (_product.name.contains(searchWord.trim(), true) || _product.description.contains(searchWord.trim(), true) ){
                   productList.add(_product)
               }
           }
       }else{
           productList = _helpingList
       }
       binding.listIsEmpty = productList.isEmpty()
       binding.filterCount = countFilter
       setCategoryList()
       updateAdapter()
   }

    private fun openCartActivity(){
        val receiveIntent = Intent(this, CartActivity::class.java)
        receiveIntent.putExtra(Const.PRODUCT_LIST_GSON, Gson().toJson(productListForCat))
        openCartDetails.launch(receiveIntent)
        binding.searchEditText.setText("")
        binding.isOpenSearch = false
    }
    val openCartDetails =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                L.d("RESULT OK")
                val resultData = result.data?.getStringExtra(Const.PRODUCT_LIST_GSON)
                L.d("resultData = $resultData")
                val returnedList: ArrayList<Products> = Gson().fromJson(resultData, object : TypeToken<ArrayList<Products>>() {}.type)
                //productList = returnedList
                L.d("returnedList = $returnedList")

                updateItemList(returnedList)
            }else{
                setItem()
            }
            itemCardAdapter.list = productList
            itemCardAdapter.notifyDataSetChanged()
        }

    private fun updateItemList(_list : ArrayList<Products>){
        for(i in 0 until allProductList.size){
            val product = allProductList[i]
            for(listItem in _list){
                if(listItem.id == product.id){
                    product.count = listItem.count
                }
            }
        }
        setPrice()
        filterProductList()
    }

    private fun openProductDetailsActivity(products: Products) {
        val receiveIntent = Intent(this, ProductDetails::class.java)
        receiveIntent.putExtra(Const.PRODUCT_GSON, Gson().toJson(products))
        openProductDetails.launch(receiveIntent)
    }

    val openProductDetails =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                L.d("selectedProduct.second.count = ${selectedProduct.second.count}")
                selectedProduct.second.count = selectedProduct.second.count + 1
                itemCardAdapter.notifyItemChanged(selectedProduct.first)
                setPrice()
                productListForCat.find { it.id == selectedProduct.second.id }!!.count = selectedProduct.second.count
                L.d("RESULT OK")
            }
        }


    private fun initTabLayout() {
        myTabLayout = binding.tabLayout
    }

    private fun setTabListener(){
        myTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                isUserScrolling = false
                val categoryId = categoryList[tab!!.position].id
                for (i in 0 until productList.size) {
                    val product = productList[i]
                    if (categoryId == product.category_id) {
                        //binding.recyclerView.smoothScrollToPosition(i)
                        gridLayoutManager.scrollToPositionWithOffset(i, 0)
                        break
                    }

                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                L.d("onTabUnselected")
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                L.d("onTabReselected")
            }
        })
    }

    private fun setTabCategory(){
        myTabLayout.removeAllTabs()
        for (category in categoryList) {
            L.d("myTabLayout category = $category")
            myTabLayout.addTab(myTabLayout.newTab().setText(category.name))
        }
        setTabListener()
    }

    private fun setAdapter() {
        itemCardAdapter = ItemCardAdapter(productList,{ positon, product ->
            selectedProduct = Pair(positon, product)
            openProductDetailsActivity(product)
            productListForCat.find{it.id == product.id}!!.count = product.count
        },{ updatePrice ->
            price += updatePrice
            setPriceInLayout(price)
            hideSoftKeyBoard(this@MainActivity, binding.root)
        })

        gridLayoutManager = GridLayoutManager(this, 2)
        binding.recyclerView.layoutManager = gridLayoutManager
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                //L.d("dx = $dx, dy = $dy")
                super.onScrolled(recyclerView, dx, dy)



                if (isUserScrolling) {
                    hideSoftKeyBoard(this@MainActivity, binding.root)
                    val itemPosition: Int =
                        if (dy < 0) gridLayoutManager.findFirstCompletelyVisibleItemPosition() else gridLayoutManager.findLastCompletelyVisibleItemPosition()
                    L.d("itemPosition = $itemPosition")
                    if (itemPosition >= 0) {
                        val productId = productList[itemPosition].category_id

                        for (i in 0 until categoryList.size) {
                            val cat = categoryList[i]
                            if (cat.id == productId) {
                                val tab: TabLayout.Tab? = myTabLayout.getTabAt(i)
                                if (tab != null && !tab.isSelected) {
                                    tab.select()
                                    break
                                }
                            }
                        }

                    }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    isUserScrolling = true
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    isUserScrolling = false
                }
                super.onScrollStateChanged(recyclerView, newState)
            }

        })

        binding.recyclerView.adapter = itemCardAdapter
    }


    private fun setItem() {
        allProductList = JsonInfo().getProductList(this)
        productListForCat = ArrayList(allProductList.sortedWith(compareBy { it.category_id }))
        L.d("sortedProductList info = $allProductList}")
        productList = ArrayList(allProductList.sortedWith(compareBy { it.category_id }))
        L.d("productList info = $productList}")
        allCategoryList = JsonInfo().getCategoryList(this)
        setCategoryList()
        tagList = JsonInfo().getTagList(this)
        L.d("allCategoryList info = ${allCategoryList}")
        L.d("categoryList info = ${categoryList}")
        L.d("tagList info = $tagList")
        setPrice()
        updateAdapter()
    }

    private fun setCategoryList(){
       /* for (product in productList) {
            if (!allCategoryList.any { it.id == product.category_id }) {
                val newCat = allCategoryList.find { it.id == product.category_id }
                if (newCat != null) {
                    categoryList.add(newCat)
                }
            }
        }*/
        categoryList.clear()
        //for(product in productList){
            for(cat in allCategoryList){
                if(productList.any{it.category_id == cat.id}){
                    categoryList.add(cat)
                }
            }
        //}
        setTabCategory()
    }

    private fun setPrice(){
        price = 0
        for (product in productList){
            if(product.count > 0){
                price += (product.price_current.toInt() * product.count)
            }
        }
        setPriceInLayout(price)
    }

    private fun setPriceInLayout(currentPrice: Int){
        if(currentPrice!= 0) {
            val formattedPrice = NumberTextWatcherForThousand.getDecimalFormattedString(
                (currentPrice.toDouble() / 100).toInt().toString()
            )
            binding.price = "Заказать за ${formattedPrice} ₽"
        }else{
            binding.price = ""
        }
    }

    private fun updateAdapter(){
        itemCardAdapter.list = productList
        itemCardAdapter.notifyDataSetChanged()
    }
}