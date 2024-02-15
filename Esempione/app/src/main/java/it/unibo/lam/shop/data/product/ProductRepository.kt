package it.unibo.lam.shop.data.product

abstract class ProductRepository {
    abstract fun getProducts(): List<Product>?;
    abstract fun getProduct(productID: Int): Product?;
}