package ch.wenksi.photosimilaritygame.datasource.localdb.users

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    suspend fun insert(entity: UserEntity): Long

    @Query("SELECT * FROM userentity where user_name = :userName")
    suspend fun get(userName: String): UserEntity?

    @Query("UPDATE userentity SET user_name = :newName WHERE user_name = :oldName")
    suspend fun updateUserName(oldName: String, newName: String)
}
