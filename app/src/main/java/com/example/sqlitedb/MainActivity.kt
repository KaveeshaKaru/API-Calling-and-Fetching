package com.example.sqlitedb


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sqlitedb.api.ApiRequestBody2
import com.example.sqlitedb.api.ApiResponseInvoice
import com.example.sqlitedb.api.ApiService
import com.example.sqlitedb.api.RetrofitInstance
import com.example.sqlitedb.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var databaseHelper: DatabaseHelper
    private val apiService = RetrofitInstance.getRetrofitInstance().create(ApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)

        binding.saveBtn.setOnClickListener {
            val empName = binding.empNameET.text.toString()
            val empEmail = binding.empEmailET.text.toString()
            val empAddress = binding.empAddressET.text.toString()
            val empPhone = binding.empPhoneET.text.toString()

            if (isEmpty(empName, empEmail, empAddress, empPhone)) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                employeeDatabase(empName, empEmail, empAddress, empPhone)
            }
        }
        binding.showdbBtn.setOnClickListener {

            val intent = Intent(this, EmployeeTableActivity::class.java)
            startActivity(intent)
        }
        binding.viewInvoiceBtn.setOnClickListener {
            sendApiRequest()
        }
        binding.itemsBtn.setOnClickListener {
            val intent = Intent (this, MainActivity2::class.java)
            startActivity(intent)
        }
    }

    private fun sendApiRequest() {
        // Prepare the API request body
        val requestBody = ApiRequestBody2(
            apiBody = emptyList(),  // Assuming no specific data needed for the API request
            apiAction = "SaveInvoiceData",
            companyCode = "EZCMP-314",
            syncTime = "",
            deviceId = "41"
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response: Response<ApiResponseInvoice> = apiService.invoiceData(requestBody)

                if (response.isSuccessful) {
                    // Handle the API response
                    val apiResponse = response.body()
                    runOnUiThread {
                        apiResponse?.message?.let {

                            Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()

                            // success
                            if (it.contains("Successfully", ignoreCase = true)) {
                                // If successful, start InvoiceForm activity
                                val intent = Intent(this@MainActivity, InvoiceForm::class.java)
                                startActivity(intent)
                            }
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "API Request Failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
                Log.e("MainActivity", "Error: ${e.message}", e)
            }
        }

    }

    private fun employeeDatabase(name: String, email: String, address: String, phone: String){
        val insertedRowId = databaseHelper.insertEmployee(name, email, address, phone)
        if(insertedRowId != -1L){

            Toast.makeText(this,"Saved", Toast.LENGTH_SHORT).show()

        }else {
            Toast.makeText(this,"Saving Failed", Toast.LENGTH_SHORT).show()
        }
    }
    private fun isEmpty(vararg fields: String): Boolean {
        for (field in fields) {
            if (field.trim().isEmpty()) {
                return true
            }
        }
        return false
    }
}