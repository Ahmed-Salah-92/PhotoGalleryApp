package com.ragdoll.photogalleryapp.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.ragdoll.photogalleryapp.data.model.Photo
import com.ragdoll.photogalleryapp.data.model.Photo.Companion.TABLE_NAME
import kotlinx.coroutines.flow.Flow
@Dao
interface PhotoDAO {
    @Upsert
    suspend fun upsertPhoto(photo: List<Photo>)

    @Query("SELECT * FROM photos_table")
    fun getPhoto(): Flow<List<Photo>>
}