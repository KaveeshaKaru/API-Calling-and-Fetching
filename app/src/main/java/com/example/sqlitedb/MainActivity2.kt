package com.example.sqlitedb


import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sqlitedb.api.ApiBodyItem
import com.example.sqlitedb.api.ApiRequestBodyItems
import com.example.sqlitedb.api.ApiResponseItems
import com.example.sqlitedb.api.ApiService
import com.example.sqlitedb.api.RetrofitInstance
import com.example.sqlitedb.databinding.ActivityMain2Binding
import retrofit2.Response

class MainActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding
    private lateinit var recyclerView: RecyclerView
    private lateinit var albumAdapter: AlbumAdapter
    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        albumAdapter = AlbumAdapter()
        recyclerView.adapter = albumAdapter

        db = DatabaseHelper(this)

        val retrofitService = RetrofitInstance.getRetrofitInstance().create(ApiService::class.java)

        val requestBody = ApiRequestBodyItems(
            listOf(ApiBodyItem("05e0e2342d4594f6")),
            "GetItemsData",
            "",
            "EZCMP-1"
        )

        val responseLiveData: LiveData<Response<ApiResponseItems>> = liveData {
            val response = retrofitService.getAlbums(requestBody)
            emit(response)
        }

        responseLiveData.observe(this, Observer { response ->
            if (response.isSuccessful) {
                val responseBody = response.body()
                val albumList = responseBody?.responseBody

                if (albumList != null) {
                    // Clear existing items from the database
//                    clearItemsFromDatabase()

                    // Insert new items into the database
                    for (albumItem in albumList) {
                        insertItem(
                            albumItem.name ?: "",
                            albumItem.code ?: "",
                            albumItem.printingName ?: "",
                            albumItem.price ?: 0.0
                        )
                    }

                    // Update the UI or perform any other actions
                    albumAdapter.setData(albumList)
                }
            } else {
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                Log.e("API Error", errorBody)
            }
        })
    }

    // Clear existing items from the database
    private fun clearItemsFromDatabase() {
        db.writableDatabase.delete(DatabaseHelper.ITEMS_TABLE, null, null)
    }

    // Insert item into the database
    private fun insertItem(name: String, code: String, printingName: String, price: Double) {
        val values = ContentValues().apply {
            put(DatabaseHelper.ITEMS_COLUMN_NAME, name)
            put(DatabaseHelper.ITEMS_COLUMN_CODE, code)
            put(DatabaseHelper.ITEMS_COLUMN_PRINTING_NAME, printingName)
            put(DatabaseHelper.ITEMS_COLUMN_PRICE, price)
        }
        db.writableDatabase.insert(DatabaseHelper.ITEMS_TABLE, null, values)
    }
}

