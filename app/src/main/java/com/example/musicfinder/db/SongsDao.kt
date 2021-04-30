package com.example.musicfinder.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.musicfinder.models.Song

@Dao
interface SongsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSongs(songs: List<Song>): List<Long>

    @Query("SELECT * FROM songs WHERE artistName LIKE :artistName")
    suspend fun getSongsByArtist(
        artistName: String
    ): List<Song>
}