package com.ragdoll.photogalleryapp.data.model


import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable


@Entity(tableName = "photos_table")
data class Photo(
    @PrimaryKey(autoGenerate = true)    // Room requires a primary key, so we add this field
    @SerializedName("id") val id: Int,
    @ColumnInfo(name = "placeholder_color")
    @SerializedName("avg_color") val avgColor: String,
    @ColumnInfo(name = "photographer_name")
    @SerializedName("photographer") val photographer: String,
    @ColumnInfo(name = "photographer_profile_url")
    @SerializedName("photographer_url") val photographerUrl: String,
    @ColumnInfo(name = "photo_url")
    @SerializedName("url") val url: String,
    @ColumnInfo(name = "photo_description")
    @SerializedName("alt") val alt: String,
    @Embedded // Embedded object for src, which contains different sizes of the photo
    @SerializedName("src") val src: Src,
) : Serializable {

    @Ignore
    @SerializedName("photographer_id")
    val photographerId: Long = 0L

    @Ignore
    @SerializedName("liked")
    val liked: Boolean = false

    @Ignore
    @SerializedName("height")
    val height: Int = 0

    @Ignore
    @SerializedName("width")
    val width: Int = 0

    override fun hashCode(): Int {
        var result = 17
        result = 31 * result + id.hashCode()
        result = 31 * result + avgColor.hashCode()
        result = 31 * result + photographer.hashCode()
        result = 31 * result + photographerUrl.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + alt.hashCode()
        result = 31 * result + src.hashCode()
        /*result = 31 * result + src.medium.hashCode()
        result = 31 * result + src.original.hashCode()
        result = 31 * result + src.portrait.hashCode()
        result = 31 * result + src.landscape.hashCode()*/
        return result
    }


    companion object {
        const val TABLE_NAME = "photos_table"
    }

}


