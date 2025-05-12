package umcandroid.essential.week02_flo_1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import umcandroid.essential.week02_flo_1.databinding.FragmentSavedTracksBinding

class SavedTracksFragment : Fragment() {

    private lateinit var binding: FragmentSavedTracksBinding
    private lateinit var trackRVAdapter: TrackRVAdapter
    lateinit var songDB: SongDatabase

    private val firebaseHelper = FirebaseHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        songDB = SongDatabase.getInstance(requireContext())!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSavedTracksBinding.inflate(inflater, container, false)

        initRecyclerView()

        // 어댑터 초기화
        val trackRVAdapter = TrackRVAdapter()
        binding.savedTrackRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.savedTrackRv.adapter = trackRVAdapter

        // Firebase에서 좋아요한 곡들 가져오기
        firebaseHelper.getLikedSongs { likedSongs ->
            trackRVAdapter.addSongs(likedSongs)
        }

        return binding.root
    }

    private fun initRecyclerView() {
        binding.savedTrackRv.layoutManager = LinearLayoutManager(context)

        trackRVAdapter = TrackRVAdapter()
        binding.savedTrackRv.adapter = trackRVAdapter

        val likedSongs = songDB.songDao().getLikedSongs(true) as ArrayList<Song>
        trackRVAdapter.addSongs(likedSongs)

        trackRVAdapter.setItemClickListener(object : TrackRVAdapter.OnItemClickListener {
            override fun onShowBottomSheet(songId: Int, position: Int) {
                val bottomSheet = BottomSheetFragment()
                bottomSheet.setSongData(songId, position, object : BottomSheetFragment.OnSongActionListener {
                    override fun onUnlike(songId: Int, position: Int) {
                        songDB.songDao().updateIsLikeById(false, songId)
                        trackRVAdapter.removeItem(position)
                    }
                })
                bottomSheet.show(parentFragmentManager, bottomSheet.tag)
            }
        })
    }

    fun deleteAllSongs() {
        val allSongs = trackRVAdapter.getCurrentItems()
        allSongs.forEach { song ->
            songDB.songDao().updateIsLikeById(false, song.id)
        }
        trackRVAdapter.removeAll()
    }
}





