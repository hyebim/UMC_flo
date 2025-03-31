package umcandroid.essential.week02_flo_1.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import me.relex.circleindicator.CircleIndicator
import umcandroid.essential.week02_flo_1.AlbumFragment
import umcandroid.essential.week02_flo_1.Banner2Fragment
import umcandroid.essential.week02_flo_1.BannerFragment
import umcandroid.essential.week02_flo_1.BannerVP2Adapter
import umcandroid.essential.week02_flo_1.BannerVPAdapter
import umcandroid.essential.week02_flo_1.R
import umcandroid.essential.week02_flo_1.databinding.FragmentHomeBinding
import java.util.Timer
import java.util.TimerTask

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val timer = Timer()
    private val handler = Handler(Looper.getMainLooper())


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
        // 일정 간격으로 슬라이드 변경 (3초마다)
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                handler.post {
                    val nextItem = binding.ivBackground.currentItem + 1
                    if (nextItem < adapter.itemCount) {
                        binding.ivBackground.currentItem = nextItem
                    } else {
                        binding.ivBackground.currentItem = 0 // 마지막 페이지에서 첫 페이지로 순환
                    }
                }
            }
        }, 3000, 3000) // 3초 지연 후, 3초마다 반복
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivAlbum3.setOnClickListener {
            val title = binding.tvAlbum3Title.text.toString()   // 제목
            val singer = binding.tvAlbum3Singer.text.toString() // 가수
            val imageResId = R.drawable.img_album_exp2 // 이미지 리소스 ID (실제 이미지로 변경)

            // 1. Bundle 생성 후 데이터 저장
            val bundle = Bundle()
            bundle.putString("title", title)
            bundle.putString("singer", singer)
            bundle.putInt("imageResId", imageResId)

            // FragmentTransaction을 사용하여 Fragment 전환
            val albumFragment = AlbumFragment()
            albumFragment.arguments = bundle // 데이터 전달
            activity?.supportFragmentManager!!.beginTransaction()
                .replace(R.id.view_main, albumFragment)
                .addToBackStack(null)  // 뒤로 가기 추가
                .commit()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}