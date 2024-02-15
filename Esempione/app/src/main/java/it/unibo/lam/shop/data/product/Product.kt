package it.unibo.lam.shop.data.product


data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val price: Int,
    val category: Category,
    val images: List<String>
)
