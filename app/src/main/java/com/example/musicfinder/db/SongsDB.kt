package com.example.musicfinder.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.musicfinder.models.Song


@Database(
    entities = [Song::class],
    version = 1
)
abstract class SongsDB: RoomDatabase() {
    abstract fun songsDao() : SongsDao

    companion object {
        private var instance: SongsDB? = null

        fun getDatabase(context: Context) : SongsDB {
            val tempInstance = instance
            if(tempInstance!=null) return tempInstance
            synchronized(this) {
                val newInstance = Room.databaseBuilder(
                    context.applicationContext,
                    SongsDB::class.java,
                    "Songs_db"
                ).build()
                instance = newInstance
                return newInstance
            }
        }

    }
}