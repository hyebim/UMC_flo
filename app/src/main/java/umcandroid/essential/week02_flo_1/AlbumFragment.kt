package umcandroid.essential.week02_flo_1

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import com.google.android.material.tabs.TabLayoutMediator
import umcandroid.essential.week02_flo_1.databinding.FragmentAlbumBinding
import umcandroid.essential.week02_flo_1.ui.home.HomeFragment

class AlbumFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var _binding: FragmentAlbumBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumBinding.inflate(inflater, container, false)

        // ViewPager2와 TabLayout 초기화
        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        // ViewPager2 어댑터 설정
        val adapter = MyPagerAdapter(requireActivity())
        viewPager.adapter = adapter

        // TabLayout과 ViewPager2 연결
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "수록곡"
                1 -> tab.text = "상세정보"
                2 -> tab.text = "영상"
            }
        }.attach()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val switch1 = binding.switch1
        val ivAlbum = binding.ivAlbum

        switch1.isEnabled = true

        arguments?.let {
            val title = it.getString("title")
            val singer = it.getString("singer")
            val imageResId = it.getInt("imageResId")

            binding.tvAlbumname.text = title
            binding.tvSinger.text = singer
            binding.ivAlbum.setImageResource(imageResId)
        }

        // "뒤로 가기" 버튼 클릭 리스너 설정
        binding.ivBack.setOnClickListener {
            // FragmentTransaction을 사용하여 HomeFragment로 돌아가기
            val homeFragment = HomeFragment() // HomeFragment 인스턴스를 생성
            parentFragmentManager.beginTransaction()
                .replace(R.id.view_main, homeFragment) // 기존 Fragment를 HomeFragment로 교체
                .addToBackStack(null) // 뒤로 가기 기능 추가
                .commit()
        }

        switch1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // 스위치가 켜지면 이미지 변경
                ivAlbum.setImageResource(R.drawable.img_album_exp) // 켠 상태에서 사용할 이미지
                val params = ivAlbum.layoutParams
                params.width = 600 // 너비를 250dp로 설정
                params.height = 600 // 높이를 250dp로 설정
                ivAlbum.layoutParams = params
            } else {
                // 스위치가 꺼지면 다른 이미지로 변경
                ivAlbum.setImageResource(R.drawable.img_album_exp2) // 껐 상태에서 사용할 이미지
                val params = ivAlbum.layoutParams
                params.width = 600 // 너비를 250dp로 설정
                params.height = 600 // 높이를 250dp로 설정
                ivAlbum.layoutParams = params
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun setInit(album: Album){
        binding.ivAlbum.setImageResource(album.coverImg!!)
        binding.tvAlbumname.text = album.title.toString()
        binding.tvSinger.text = album.singer.toString()
    }
}