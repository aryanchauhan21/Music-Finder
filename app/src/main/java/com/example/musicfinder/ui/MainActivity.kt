package com.example.musicfinder.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.musicfinder.R
import com.example.musicfinder.api.RetrofitInstance
import com.example.musicfinder.models.SearchResponse
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        query_edittext.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                // implement search
                search()
            }
            false
        }

        search_icon.setOnClickListener {
            search()
            hideKeyboard()
        }
    }

    private fun hideKeyboard() {
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mainLayout.windowToken, 0)
    }


    private fun search() {
        GlobalScope.launch {
            var response: Response<SearchResponse> = RetrofitInstance.api.searchForQuery(query_edittext.text.toString())
            if (response.isSuccessful) {
                Log.d(TAG, "search: Response message = ${response.message()}")
                Log.d(TAG, "search: Body = ${response.body()}")
            } else {
                Log.d(TAG, "search: Something went wrong...")
            }
        }
    }
}