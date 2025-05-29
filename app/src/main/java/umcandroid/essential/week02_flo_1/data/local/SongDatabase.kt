package umcandroid.essential.week02_flo_1.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import umcandroid.essential.week02_flo_1.data.entities.Album
import umcandroid.essential.week02_flo_1.data.entities.Like
import umcandroid.essential.week02_flo_1.data.entities.Song
import umcandroid.essential.week02_flo_1.data.entities.User
import umcandroid.essential.week02_flo_1.data.local.dao.AlbumDao
import umcandroid.essential.week02_flo_1.data.local.dao.SongDao
import umcandroid.essential.week02_flo_1.data.local.dao.UserDao

@Database(entities = [Song::class, Album::class, User::class, Like::class], version = 6)
abstract class SongDatabase: RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun albumDao(): AlbumDao
    abstract fun userDao(): UserDao

    companion object {
        private var instance: SongDatabase? = null

        @Synchronized
        fun getInstance(context: Context): SongDatabase? {
            if (instance == null) {
                synchronized(SongDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SongDatabase::class.java,
                        "song-database" // 다른 데이터 베이스랑 이름이 겹치지 않도록 주의
                    ).allowMainThreadQueries() // 편의상 메인 쓰레드에서 처리하게 한다.
                         .fallbackToDestructiveMigration()
                        .build()
                }
            }

            return instance
        }
    }
}