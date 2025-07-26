package com.ragdoll.photogalleryapp.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ragdoll.photogalleryapp.data.local.entity.PhotoEntity

@Dao
interface PhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertALll(photo: List<PhotoEntity>)

    @Query("SELECT * FROM photos")
    fun getPagingSource(): PagingSource<Int, PhotoEntity>

    @Query("SELECT * FROM photos WHERE id = :id")
    suspend fun getPhotoById(id: Int): PhotoEntity?

    @Query("DELETE FROM photos")
    suspend fun clearAll()
}