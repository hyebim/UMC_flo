package umcandroid.essential.week02_flo_1.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import umcandroid.essential.week02_flo_1.Album
import umcandroid.essential.week02_flo_1.AlbumFragment
import umcandroid.essential.week02_flo_1.AlbumRVAdapter
import umcandroid.essential.week02_flo_1.Banner2Fragment
import umcandroid.essential.week02_flo_1.BannerFragment
import umcandroid.essential.week02_flo_1.BannerVP2Adapter
import umcandroid.essential.week02_flo_1.BannerVPAdapter
import umcandroid.essential.week02_flo_1.MainActivity
import umcandroid.essential.week02_flo_1.R
import umcandroid.essential.week02_flo_1.databinding.FragmentHomeBinding
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val handler = Handler(Looper.getMainLooper())
    private val timer = Timer()
    private var slideTask: TimerTask? = null

    private var albumDatas = ArrayList<Album>()

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

        // 더미 데이터
        albumDatas.apply {
            add(Album("Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp))
            add(Album("Lilac", "아이유 (IU)", R.drawable.img_album_exp2))
            add(Album("Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp))
            add(Album("Lilac", "아이유 (IU)", R.drawable.img_album_exp2))
            add(Album("Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp))
            add(Album("Lilac", "아이유 (IU)", R.drawable.img_album_exp2))
        }

        val albumRVAdapter = AlbumRVAdapter(albumDatas)
        binding.homeTodayMusicAlbumRv.adapter = albumRVAdapter
        binding.homeTodayMusicAlbumRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

//        albumRVAdapter.setMyItemClickListener(object: AlbumRVAdapter.MyItemClickListener{
//            override fun onItemClick() {
//                (context as MainActivity).supportFragmentManager.beginTransaction()
//                    .replace(R.id.view_main, AlbumFragment().apply {
//                        arguments = Bundle().apply {
//                            val gson = Gson()
//                            val albumJson = gson.toJson(album)
//                            putString("album", albumJson)
//                        }
//                    })
//                    .commitAllowingStateLoss()
//            }
//        })

        val bannerAdapter = BannerVPAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        val banner2Adapter = BannerVP2Adapter(this)
        banner2Adapter.addFragment(Banner2Fragment(R.drawable.img_first_album_default))
        banner2Adapter.addFragment(Banner2Fragment(R.drawable.img_home_viewpager_exp2))
        binding.ivBackground.adapter = banner2Adapter
        binding.ivBackground.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        binding.circleIndicator.setViewPager(binding.ivBackground)

        startAutoSlide(banner2Adapter)

        return root
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

//        binding.ivAlbum3.setOnClickListener {
//            val title = binding.tvAlbum3Title.text.toString()
//            val singer = binding.tvAlbum3Singer.text.toString()
//            val imageResId = R.drawable.img_album_exp2
//
//            val bundle = Bundle().apply {
//                putString("title", title)
//                putString("singer", singer)
//                putInt("imageResId", imageResId)
//            }
//
//            val albumFragment = AlbumFragment().apply {
//                arguments = bundle
//            }
//
//            activity?.supportFragmentManager?.beginTransaction()
//                ?.replace(R.id.view_main, albumFragment)
//                ?.addToBackStack(null)
//                ?.commit()
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        slideTask?.cancel()     // 슬라이드 중지
        slideTask = null
        _binding = null
    }
}
