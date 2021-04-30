package com.example.musicfinder.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.musicfinder.R
import com.example.musicfinder.api.RetrofitInstance
import com.example.musicfinder.db.SongsDB
import com.example.musicfinder.models.SearchResponse
import com.example.musicfinder.repository.Repository
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    private lateinit var repository: Repository
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = SongsDB.getDatabase(this)
        repository = Repository(db)

        query_edittext.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                search()
            }
            false
        }
        search_icon.setOnClickListener {
            searchOffline()
            hideKeyboard()
        }
    }

    private fun searchOffline() {
        GlobalScope.launch { 
            val songs = repository.searchForQueryOffline(query_edittext.text.toString())
            Log.d(TAG, "searchOffline: ${songs.size} songs found")
        }
    }

    private fun hideKeyboard() {
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mainLayout.windowToken, 0)
    }

    private fun search() {
        GlobalScope.launch {
            val response: Response<SearchResponse> = RetrofitInstance.api.searchForQuery(query_edittext.text.toString())
            if (response.isSuccessful) {
                Log.d(TAG, "search: Inserting into DB")
                response.body()?.results?.let {
                    Log.d(TAG, "search: Inserted into DB")
                    repository.insertSongs(it)
                }
            } else {
                Log.d(TAG, "search: Something went wrong...")
            }
        }
    }
}