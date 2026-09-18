package com.example.loginapp.controller

import com.example.loginapp.model.Product
import com.example.loginapp.model.ProductRepository
import com.example.loginapp.model.Role
import com.example.loginapp.model.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductDetailController(
    private val repository: ProductRepository = ProductRepository(),
    private val session: SessionManager
) {

    interface View {
        fun showLoading()
        fun hideLoading()
        fun showProduct(product: Product, canManage: Boolean)
        fun showError(message: String)
        fun navigateBackToCatalog()
    }

    /** US05 - Cargar detalle del producto */
    fun loadProduct(productId: Int, view: View) {
        view.showLoading()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val product = repository.getProductById(productId)

                // El rol se lee LOCAL, no de la API
                val canManage = session.getRole() == Role.ADMIN

                withContext(Dispatchers.Main) {
                    view.hideLoading()
                    view.showProduct(product, canManage)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    view.hideLoading()
                    view.showError("Producto no disponible")
                    view.navigateBackToCatalog()
                }
            }
        }
    }
}