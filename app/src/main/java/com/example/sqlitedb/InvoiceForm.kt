package com.example.sqlitedb

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sqlitedb.databinding.ActivityInvoiceFormBinding

class InvoiceForm : AppCompatActivity() {
    private lateinit var binding: ActivityInvoiceFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInvoiceFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check if the intent contains the response message
        val responseMessage = intent.getStringExtra("responseMessage")

        // Set the response message to the TextView
        binding.messageTextView.text = responseMessage ?: "No response message available"
    }
}
