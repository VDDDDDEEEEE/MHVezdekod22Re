package com.vde.mhvezdekod22re.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vde.mhvezdekod22re.models.Category
import com.vde.mhvezdekod22re.models.Products
import com.vde.mhvezdekod22re.models.Tags
import com.vde.mhvezdekod22re.utils.UtilJson.loadJSONFromAssets

class JsonInfo {

    fun getCategoryList(context:Context): ArrayList<Category>{
        val gson = Gson()
        val listCatType = object : TypeToken<ArrayList<Category>>() {}.type
        val categoryString =  context.loadJSONFromAssets("Categories.json")
        val categoryList: ArrayList<Category> = gson.fromJson(categoryString, listCatType)
        return categoryList
    }

    fun getTagList(context:Context): ArrayList<Tags>{
        val gson = Gson()
        val listTagType = object : TypeToken<ArrayList<Tags>>() {}.type
        val tagsString =  context.loadJSONFromAssets("Tags.json")
        val tagList: ArrayList<Tags> = gson.fromJson(tagsString, listTagType)
        return tagList
    }

    fun getProductList(context:Context): ArrayList<Products>{
        val gson = Gson()
        val listProductType = object : TypeToken<ArrayList<Products>>() {}.type
        val productString =  context.loadJSONFromAssets("Products.json")
        val productList: ArrayList<Products> = gson.fromJson(productString, listProductType)
        return productList
    }




}

