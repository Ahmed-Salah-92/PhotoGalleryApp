package com.ragdoll.photogalleryapp.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ragdoll.photogalleryapp.data.local.dao.PhotoDao
import com.ragdoll.photogalleryapp.data.local.entity.PhotoEntity

@Database(entities = [PhotoEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun photoDao(): PhotoDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        const val DATABASE_NAME = "photo_database"
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val newInstance = Room
                    .databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = newInstance
                newInstance
            }
        }
    }
}