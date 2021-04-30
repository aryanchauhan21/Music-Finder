package com.example.musicfinder.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.musicfinder.R
import com.example.musicfinder.adapter.GridAdapter
import com.example.musicfinder.api.RetrofitInstance
import com.example.musicfinder.db.SongsDB
import com.example.musicfinder.models.SearchResponse
import com.example.musicfinder.repository.Repository
import com.example.musicfinder.utils.Resource
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModelProviderFactory = SearchViewModelProviderFactory(application, SongsDB.getDatabase(this))
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(SearchViewModel::class.java)

        query_edittext.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                search()
            }
            false
        }
        search_icon.setOnClickListener {
            search()
            hideKeyboard()
        }

        viewModel.searchedSongs.observe(this, Observer {
            when(it){
                is Resource.Success -> {
                    progress.visibility = View.GONE
                    if(it.data == null || it.data.results.isEmpty()) {
                        Toast.makeText(baseContext, "No results Found", Toast.LENGTH_LONG).show()
                    } else {
                        grid.visibility = View.VISIBLE
                        val songList = it.data.results
                        val adapter = GridAdapter(this, it.data.results)
                        grid.adapter = adapter
                    }
                }

                is Resource.Error -> {
                    progress.visibility = View.GONE
                    grid.visibility = View.GONE
                    Log.d(TAG, "onCreate: Error occured : ${it.message}")
                    Toast.makeText(baseContext, "Something went wrong", Toast.LENGTH_LONG).show()
                }

                is Resource.Loading -> {
                    progress.visibility = View.VISIBLE
                    grid.visibility = View.GONE
                }
            }
        })
    }

    private fun hideKeyboard() {
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mainLayout.windowToken, 0)
    }

    private fun search() {
        viewModel.searchForQuery(query_edittext.text.toString())
    }

//    private fun searchOffline() {
//        GlobalScope.launch {
//            val songs = repository.searchForQueryOffline(query_edittext.text.toString())
//            Log.d(TAG, "searchOffline: ${songs.size} songs found")
//        }
//    }


//    private fun search() {
//        GlobalScope.launch {
//            val response: Response<SearchResponse> = RetrofitInstance.api.searchForQuery(query_edittext.text.toString())
//            if (response.isSuccessful) {
//                Log.d(TAG, "search: Inserting into DB")
//                response.body()?.results?.let {
//                    Log.d(TAG, "search: Inserted into DB")
//                    repository.insertSongs(it)
//                }
//            } else {
//                Log.d(TAG, "search: Something went wrong...")
//            }
//        }
//    }
}