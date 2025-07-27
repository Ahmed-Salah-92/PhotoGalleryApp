package com.ragdoll.photogalleryapp.data.local.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ragdoll.photogalleryapp.data.local.dao.PhotoDao
import com.ragdoll.photogalleryapp.data.local.dao.RemoteKeysDao
import com.ragdoll.photogalleryapp.data.local.entity.PhotoEntity
import com.ragdoll.photogalleryapp.data.local.entity.RemoteKeys

@Database(entities = [PhotoEntity::class, RemoteKeys::class], version = 2, exportSchema = false)
@AutoMigration (from = 1, to = 2) // Automatically handle migration from version 1 to 2
abstract class AppDatabase : RoomDatabase() {
    abstract fun photoDao(): PhotoDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        const val DATABASE_NAME = "photo_database"
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val newInstance = Room
                    .databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                    .fallbackToDestructiveMigration() // Add this to handle schema changes by recreating the database
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = newInstance
                newInstance
            }
        }
    }
}