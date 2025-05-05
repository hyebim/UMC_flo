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
    private var trackDatas = ArrayList<Track>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 더미 데이터는 여기서 한 번만 초기화
        trackDatas.apply {
            add(Track("Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp))
            add(Track("Lilac", "아이유 (IU)", R.drawable.img_album_exp2))
            add(Track("Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp))
            add(Track("Lilac", "아이유 (IU)", R.drawable.img_album_exp2))
            add(Track("Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp))
            add(Track("Lilac", "아이유 (IU)", R.drawable.img_album_exp2))
            add(Track("Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp))
            add(Track("Lilac", "아이유 (IU)", R.drawable.img_album_exp2))
            add(Track("Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp))
            add(Track("Lilac", "아이유 (IU)", R.drawable.img_album_exp2))
            add(Track("Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp))
            add(Track("Lilac", "아이유 (IU)", R.drawable.img_album_exp2))
            add(Track("Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp))
            add(Track("Lilac", "아이유 (IU)", R.drawable.img_album_exp2))
            add(Track("Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp))
            add(Track("Lilac", "아이유 (IU)", R.drawable.img_album_exp2))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSavedTracksBinding.inflate(inflater, container, false)

        val trackRVAdapter = TrackRVAdapter(trackDatas)
        binding.savedTrackRv.adapter = trackRVAdapter
        binding.savedTrackRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        return binding.root
    }
}




