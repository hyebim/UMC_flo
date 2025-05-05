package umcandroid.essential.week02_flo_1

data class Track(
    var title: String? = "",
    var singer: String? = "",
    var coverImg: Int? = null,
    var songs: ArrayList<Song>? = null,

    var isSwitchOn: Boolean = false
)
