package umcandroid.essential.week02_flo_1.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import umcandroid.essential.week02_flo_1.data.entities.Song

@Dao
interface SongDao {
    @Insert
    fun insert(song: Song)

    @Update
    fun update(song: Song)

    @Delete
    fun delete(song: Song)

    @Query("SELECT * FROM SongTable")
    fun getSongs(): List<Song>

    @Query("SELECT * FROM SongTable WHERE id = :id")
    fun getSong(id: Int): Song

    @Query("UPDATE SongTable SET isLike= :isLike WHERE id = :id")
    fun updateIsLikeById(isLike: Boolean, id: Int)

    @Query("SELECT * FROM SongTable WHERE isLike= :isLike")
    fun getLikedSongs(isLike: Boolean): List<Song>

    // 특정 곡의 isLike 값을 업데이트
    @Query("UPDATE SongTable SET isLike = :isLike WHERE id = :songId")
    fun updateIsLike(songId: Int, isLike: Boolean)

    // 모든 곡의 isLike 값을 false로 업데이트
    @Query("UPDATE SongTable SET isLike = :isLike")
    fun updateAllIsLike(isLike: Boolean)

}