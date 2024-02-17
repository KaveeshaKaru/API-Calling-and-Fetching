package com.example.sqlitedb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.sqlitedb.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper= DatabaseHelper(this)
        binding.signupButton.setOnClickListener {

            val signupEmail = binding.signupEmail.text.toString()
            val signupPassword = binding.signupPassword.text.toString()
            signupDatabase(signupEmail, signupPassword)
        }

        binding.loginRedirect.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
    private fun signupDatabase(email: String, password: String){
        val insertedRowId = databaseHelper.insertUser(email, password)
        if(insertedRowId != -1L){

            Toast.makeText(this,"Signup Successful", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }else {
            Toast.makeText(this,"Signup Failed", Toast.LENGTH_SHORT).show()
        }
    }


}