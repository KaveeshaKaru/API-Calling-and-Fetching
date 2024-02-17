package com.example.sqlitedb

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sqlitedb.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var databaseHelper: DatabaseHelper

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