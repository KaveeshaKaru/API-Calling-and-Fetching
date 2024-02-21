package com.example.sqlitedb

//class LoginActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityLoginBinding
//    private lateinit var databaseHelper: DatabaseHelper
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityLoginBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        databaseHelper = DatabaseHelper(this)
//
//        binding.loginButton.setOnClickListener {
//            val loginEmail= binding.loginEmail.text.toString()
//            val loginPassword = binding.loginPassword.text.toString()
//            loginDatabase(loginEmail,loginPassword)
//        }
//        binding.signupRedirect.setOnClickListener {
//            val intent = Intent(this,SignupActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
//
//    }
//    private fun loginDatabase(email: String, password: String){
//        val userExists = databaseHelper.readUser(email, password)
//        if(userExists){
//            Toast.makeText(this,"Login Successful", Toast.LENGTH_SHORT).show()
//            val intent = Intent(this,MainActivity::class.java)
//            startActivity(intent)
//            finish()
//        }else {
//            Toast.makeText(this,"Login Failed", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//}

//

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sqlitedb.api.ApiRequestBody
import com.example.sqlitedb.api.ApiResponse
import com.example.sqlitedb.api.ApiService
import com.example.sqlitedb.api.RetrofitInstance
import com.example.sqlitedb.databinding.ActivityLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val apiService = RetrofitInstance.getRetrofitInstance().create(ApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            val loginEmail = binding.loginEmail.text.toString()
            val loginPassword = binding.loginPassword.text.toString()

            // Field Validation
            if (loginEmail.isNotEmpty() && loginPassword.isNotEmpty()) {
                // Call the login API for user authentication
                loginUser(loginEmail, loginPassword)
            } else {
                Toast.makeText(this, "Email and password cannot be empty", Toast.LENGTH_SHORT)
                    .show()
            }
        }


        binding.signupRedirect.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loginUser(email: String, password: String) {
        val requestBody = ApiRequestBody(
            apiBody = listOf(
                mapOf(
                    "Unique_Id" to email,
                    "Pw" to password
                )
            ),
            apiAction = "GetUserData",
            companyCode = email,
            syncTime = ""
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // API call
                val response: Response<ApiResponse> = apiService.invokeApi(requestBody)

                if (response.isSuccessful) {
                    // Handle the successful response
                    val apiResponse = response.body()
                    if (apiResponse != null && apiResponse.statusCode == 200) {
                        val responseBody = apiResponse.responseBody
                        handleSuccessResponse(responseBody)
                    } else {
                        // API response indicating login failure
                        val message = apiResponse?.message ?: "Unknown Error"
                        runOnUiThread {
                            Toast.makeText(
                                this@LoginActivity,
                                "Login Failed: $message",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    // Unsuccessful response
                    runOnUiThread {
                        Toast.makeText(
                            this@LoginActivity,
                            "Error: ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                // Handle exceptions
                runOnUiThread {
                    Toast.makeText(this@LoginActivity, "Error: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }


    private fun handleSuccessResponse(responseBodyList: List<Map<String, Any>>?) {
        if (!responseBodyList.isNullOrEmpty()) {
            val responseBody = responseBodyList[0]

            // Check if "Doc_Msg" key is present in the response
            if ("Doc_Msg" in responseBody) {
                val docMsg = responseBody["Doc_Msg"] as? String

                if (docMsg != null && docMsg == "Invalid Password") {
                    runOnUiThread {
                        Toast.makeText(this@LoginActivity, "Login Failed: $docMsg", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Proceed with successful login
                    runOnUiThread {
                        Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            } else {
                runOnUiThread {
                    Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        } else {
            runOnUiThread {
                Toast.makeText(this@LoginActivity, "Error: Empty or null Response_Body", Toast.LENGTH_SHORT).show()
            }
        }
    }



//    private fun handleErrorResponse(apiResponse: ApiResponse?) {
//        val message = apiResponse?.message ?: "Unknown Error"
//        runOnUiThread {
//            Toast.makeText(this@LoginActivity, "Login Failed: $message", Toast.LENGTH_SHORT).show()
//        }
//    }

}
