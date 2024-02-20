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

        // Assuming you have obtained the message from the API response
        val message = ""

        // Set the message to the TextView
        binding.messageTextView.text = message
    }
}
