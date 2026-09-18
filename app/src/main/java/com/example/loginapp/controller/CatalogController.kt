package com.example.loginapp.controller

import com.example.loginapp.model.Product
import com.example.loginapp.model.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CatalogController(
    private val repository: ProductRepository = ProductRepository()
) {

    interface View {
        fun showLoading()
        fun hideLoading()
        fun showProducts(products: List<Product>)
        fun showCategories(categories: List<String>)
        fun showError(message: String)
    }

    private var currentProducts: List<Product> = emptyList()

    /** US03 - Cargar catálogo completo */
    fun loadAllProducts(view: View) {
        view.showLoading()
        view.showProducts(emptyList())   // US04: limpiar array previo

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val products = repository.getAllProducts()
                currentProducts = products
                withContext(Dispatchers.Main) {
                    view.hideLoading()
                    view.showProducts(products)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    view.hideLoading()
                    view.showError("No se pudo cargar el catálogo")
                }
            }
        }
    }

    /** US04 - Obtener categorías */
    fun loadCategories(view: View) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val categories = repository.getCategories()
                withContext(Dispatchers.Main) {
                    view.showCategories(categories)
                }
            } catch (e: Exception) {
                // Silencioso: si falla, simplemente no se muestran chips
            }
        }
    }

    /** US04 - Filtrar por categoría */
    fun filterByCategory(category: String, view: View) {
        view.showLoading()
        view.showProducts(emptyList())   // Limpia memoria local

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val products = repository.getProductsByCategory(category)
                currentProducts = products
                withContext(Dispatchers.Main) {
                    view.hideLoading()
                    view.showProducts(products)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    view.hideLoading()
                    view.showError("Error al filtrar productos")
                }
            }
        }
    }

    /** US04 - Quitar filtro */
    fun clearFilter(view: View) {
        loadAllProducts(view)
    }
}