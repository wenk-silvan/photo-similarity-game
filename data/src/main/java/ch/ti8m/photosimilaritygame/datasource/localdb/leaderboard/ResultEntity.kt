package ch.wenksi.photosimilaritygame.datasource.localdb.leaderboard

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "user_name") val userName: String,
    @ColumnInfo(name = "similarity_score") val similarityScore: Int,
    @ColumnInfo(name = "random_photo_url") val randomPhotoUrl: String,
    @ColumnInfo(name = "camera_photo_uri_path") val cameraPhotoUriPath: String,
    @ColumnInfo(name = "random_photo_labels") val randomPhotoLabels: String,
    @ColumnInfo(name = "camera_photo_labels") val cameraPhotoLabels: String,
)
