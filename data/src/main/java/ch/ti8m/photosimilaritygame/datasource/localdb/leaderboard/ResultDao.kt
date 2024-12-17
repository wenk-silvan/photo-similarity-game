package ch.wenksi.photosimilaritygame.datasource.localdb.leaderboard

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ResultDao {
    @Insert
    suspend fun insert(result: ResultEntity): Long

    @Query("SELECT * FROM resultentity")
    suspend fun getAll(): List<ResultEntity>

    @Query("DELETE FROM resultentity")
    suspend fun delete()
}
