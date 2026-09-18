package com.example.loginapp.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loginapp.R
import com.example.loginapp.controller.CatalogController
import com.example.loginapp.model.Product

class CatalogActivity : AppCompatActivity(), CatalogController.View {

    private val controller = CatalogController()
    private lateinit var adapter: ProductAdapter
    private lateinit var recycler: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorContainer: LinearLayout
    private lateinit var errorText: TextView
    private lateinit var categoriesContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalog)

        recycler = findViewById(R.id.recycler_products)
        progressBar = findViewById(R.id.progress_bar)
        errorContainer = findViewById(R.id.error_container)
        errorText = findViewById(R.id.error_text)
        categoriesContainer = findViewById(R.id.categories_container)

        adapter = ProductAdapter(emptyList()) { product ->
            // Navegar a detalle (US05)
            val intent = Intent(this, ProductDetailActivity::class.java).apply {
                putExtra("PRODUCT_ID", product.id)
            }
            startActivity(intent)
        }

        recycler.layoutManager = GridLayoutManager(this, 2)
        recycler.adapter = adapter

        findViewById<Button>(R.id.btn_retry).setOnClickListener {
            controller.loadAllProducts(this)
        }

        findViewById<Button>(R.id.btn_clear_filter).setOnClickListener {
            controller.clearFilter(this)
        }

        // Cargar categorías y productos
        controller.loadCategories(this)
        controller.loadAllProducts(this)
    }

    // ===== Callbacks del controlador =====

    override fun showLoading() {
        progressBar.visibility = View.VISIBLE
        errorContainer.visibility = View.GONE
        recycler.visibility = View.GONE
    }

    override fun hideLoading() {
        progressBar.visibility = View.GONE
        recycler.visibility = View.VISIBLE
    }

    override fun showProducts(products: List<Product>) {
        adapter.updateProducts(products)
    }

    override fun showCategories(categories: List<String>) {
        categoriesContainer.removeAllViews()
        for (category in categories) {
            val chip = Button(this).apply {
                text = category
                textSize = 12f
                setBackgroundColor(0xFF3B84F1.toInt())
                setTextColor(0xFFFFFFFF.toInt())
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(8, 0, 8, 0)
                layoutParams = params
                setOnClickListener {
                    controller.filterByCategory(category, this@CatalogActivity)
                }
            }
            categoriesContainer.addView(chip)
        }
    }

    override fun showError(message: String) {
        errorContainer.visibility = View.VISIBLE
        recycler.visibility = View.GONE
        errorText.text = message
    }
}