package ch.wenksi.photosimilaritygame.datasource.localdb

import androidx.room.Database
import androidx.room.RoomDatabase
import ch.wenksi.photosimilaritygame.datasource.localdb.leaderboard.ResultDao
import ch.wenksi.photosimilaritygame.datasource.localdb.leaderboard.ResultEntity
import ch.wenksi.photosimilaritygame.datasource.localdb.users.UserDao
import ch.wenksi.photosimilaritygame.datasource.localdb.users.UserEntity

@Database(entities = [ResultEntity::class, UserEntity::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun resultDao(): ResultDao
    abstract fun userDao(): UserDao
}
