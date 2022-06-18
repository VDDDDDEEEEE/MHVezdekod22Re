package com.vde.mhvezdekod22re.models

data class Products(
    val id: Int,
    val category_id: Int,
    val name: String,
    val description: String,
    val image: String,
    val price_current: String,
    val price_old: Int?,
    val measure: String,
    val measure_unit: String,
    val energy_per_100_grams: String,
    val proteins_per_100_grams: String,
    val fats_per_100_grams: String,
    val carbohydrates_per_100_grams: String,
    val tag_ids: IntArray,
    var count: Int = 0
){
}
