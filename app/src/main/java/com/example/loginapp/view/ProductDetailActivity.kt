package com.example.loginapp.view

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.loginapp.R
import com.example.loginapp.controller.ProductDetailController
import com.example.loginapp.model.Product
import com.example.loginapp.model.SessionManager

class ProductDetailActivity : AppCompatActivity(), ProductDetailController.View {

    private lateinit var controller: ProductDetailController
    private lateinit var progressBar: ProgressBar
    private lateinit var imageView: ImageView
    private lateinit var titleText: TextView
    private lateinit var categoryText: TextView
    private lateinit var priceText: TextView
    private lateinit var descriptionText: TextView
    private lateinit var adminActions: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        val session = SessionManager(this)
        controller = ProductDetailController(session = session)

        progressBar = findViewById(R.id.progress_bar)
        imageView = findViewById(R.id.detail_image)
        titleText = findViewById(R.id.detail_title)
        categoryText = findViewById(R.id.detail_category)
        priceText = findViewById(R.id.detail_price)
        descriptionText = findViewById(R.id.detail_description)
        adminActions = findViewById(R.id.admin_actions)

        val productId = intent.getIntExtra("PRODUCT_ID", -1)
        if (productId == -1) {
            Toast.makeText(this, "Producto no disponible", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        controller.loadProduct(productId, this)
    }

    override fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressBar.visibility = View.GONE
    }

    override fun showProduct(product: Product, canManage: Boolean) {
        titleText.text = product.title
        categoryText.text = "Categoría: ${product.category}"
        priceText.text = product.formattedPrice
        descriptionText.text = product.description

        Glide.with(this).load(product.image).into(imageView)

        // US05 - Solo Admin ve los botones de gestión
        if (canManage) {
            adminActions.visibility = View.VISIBLE
            findViewById<Button>(R.id.btn_edit).setOnClickListener {
                Toast.makeText(this, "Editar producto", Toast.LENGTH_SHORT).show()
            }
            findViewById<Button>(R.id.btn_delete).setOnClickListener {
                Toast.makeText(this, "Eliminar producto", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Se excluye de la jerarquía visual
            adminActions.visibility = View.GONE
        }
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun navigateBackToCatalog() {
        finish()
    }
}