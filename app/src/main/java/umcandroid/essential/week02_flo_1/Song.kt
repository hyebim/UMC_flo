package umcandroid.essential.week02_flo_1

data class Song(
    val title : String = "",
    val singer : String = "", 
    val second : Int = 0,
    val playTime : Int = 0,
    var isPlaying : Boolean = false
)
