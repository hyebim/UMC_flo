package umcandroid.essential.week02_flo_1.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "LikeTable")
data class Like(
    var userId : Int,
    var albumId : Int
){
    @PrimaryKey(autoGenerate = true) var id : Int = 0
}
