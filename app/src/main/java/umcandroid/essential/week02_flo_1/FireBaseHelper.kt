package umcandroid.essential.week02_flo_1

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import com.google.firebase.database.*
import umcandroid.essential.week02_flo_1.Song

class FirebaseHelper {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val songRef: DatabaseReference = database.getReference("songs")

    // 좋아요 상태 업데이트
    fun updateLikeStatus(songId: String, isLiked: Boolean) {
        songRef.child(songId).child("isLiked").setValue(isLiked)
    }

    // 특정 곡 가져오기
    fun getSongById(songId: String, callback: (Song) -> Unit) {
        songRef.child(songId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val song = snapshot.getValue(Song::class.java)
                if (song != null) {
                    callback(song)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // 좋아요한 곡들 가져오기
    fun getLikedSongs(callback: (ArrayList<Song>) -> Unit) {
        val likedSongs = ArrayList<Song>()

        songRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (child in snapshot.children) {
                    val song = child.getValue(Song::class.java)
                    if (song != null && song.isLike) {
                        likedSongs.add(song)
                    }
                }
                callback(likedSongs)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(ArrayList()) // 실패 시 빈 리스트 반환
            }
        })
    }
}
