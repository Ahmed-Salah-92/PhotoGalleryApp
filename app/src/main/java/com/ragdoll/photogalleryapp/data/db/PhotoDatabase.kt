package com.ragdoll.photogalleryapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ragdoll.photogalleryapp.data.model.Photo

@Database(entities = [Photo::class], version = 1, exportSchema = false)
abstract class PhotoDatabase : RoomDatabase() {

    abstract fun getPhotoDAO(): PhotoDAO

    /*companion object {
        const val DATABASE_NAME = "photo_database"

        @Volatile
        private var INSTANCE: PhotoDatabase? = null

        fun getInstance(context: Context): PhotoDatabase {
            return INSTANCE ?: synchronized(this) {
                val newInstance = Room
                    .databaseBuilder(context, PhotoDatabase::class.java, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = newInstance
                newInstance
            }
        }
    }*/
}