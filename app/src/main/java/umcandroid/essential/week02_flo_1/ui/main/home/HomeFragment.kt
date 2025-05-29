package umcandroid.essential.week02_flo_1.ui.main.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.gson.Gson
import umcandroid.essential.week02_flo_1.data.entities.Album
import umcandroid.essential.week02_flo_1.ui.main.album.AlbumFragment
import umcandroid.essential.week02_flo_1.ui.main.album.AlbumRVAdapter
import umcandroid.essential.week02_flo_1.R
import umcandroid.essential.week02_flo_1.data.local.SongDatabase
import umcandroid.essential.week02_flo_1.databinding.FragmentHomeBinding
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val handler = Handler(Looper.getMainLooper())
    private val timer = Timer()
    private var slideTask: TimerTask? = null

    private var albumDatas = ArrayList<Album>()
    private lateinit var songDB: SongDatabase


    companion object {
        private const val ARG_POSITION = "position"

        fun newInstance(position: Int): HomeFragment {
            val fragment = HomeFragment()
            val args = Bundle()
            args.putInt(ARG_POSITION, position)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //inputDummyAlbums()

        songDB = SongDatabase.getInstance(requireContext())!!
        albumDatas.addAll(songDB.albumDao().getAlbums())
        Log.d("albumlist", albumDatas.toString())

        val albumRVAdapter = AlbumRVAdapter(albumDatas)
        binding.homeTodayMusicAlbumRv.adapter = albumRVAdapter
        binding.homeTodayMusicAlbumRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        //앨범
        albumRVAdapter.setMyItemClickListener(object: AlbumRVAdapter.MyItemClickListener{
            override fun onItemClick(album : Album) {
                changeAlbumFragment(album)
            }
        })

        val bannerAdapter = BannerVPAdapter(this)
        bannerAdapter.addFragment(BannerFragment.newInstance(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment.newInstance(R.drawable.img_home_viewpager_exp2))
        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        val banner2Adapter = BannerVP2Adapter(this)
        banner2Adapter.addFragment(Banner2Fragment.newInstance(R.drawable.img_first_album_default))
        banner2Adapter.addFragment(Banner2Fragment.newInstance(R.drawable.img_home_viewpager_exp2))
        binding.ivBackground.adapter = banner2Adapter
        binding.ivBackground.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        binding.circleIndicator.setViewPager(binding.ivBackground)

        startAutoSlide(banner2Adapter)

        return root
    }

    private fun changeAlbumFragment(album: Album) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.view_main, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumJson = gson.toJson(album)
                    putString("album", albumJson)
                }
            })
            .commitAllowingStateLoss()
    }

    private fun startAutoSlide(adapter: BannerVP2Adapter) {
        slideTask = object : TimerTask() {
            override fun run() {
                handler.post {
                    _binding?.let { binding ->
                        val nextItem = binding.ivBackground.currentItem + 1
                        binding.ivBackground.currentItem =
                            if (nextItem < adapter.itemCount) nextItem else 0
                    }
                }
            }
        }
        timer.scheduleAtFixedRate(slideTask, 3000, 3000)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        slideTask?.cancel()     // 슬라이드 중지
        slideTask = null
        _binding = null
    }
}
