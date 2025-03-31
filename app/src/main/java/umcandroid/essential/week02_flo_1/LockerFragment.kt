package umcandroid.essential.week02_flo_1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import umcandroid.essential.week02_flo_1.databinding.FragmentLockerBinding

/**
 * A simple [Fragment] subclass.
 * Use the [LockerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LockerFragment : Fragment() {
    private var _binding: FragmentLockerBinding? = null
    private val binding get() = _binding!!

    private lateinit var pagerAdapter: LockerPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLockerBinding.inflate(inflater, container, false)

        // 어댑터가 초기화되지 않았다면 생성
        if (!::pagerAdapter.isInitialized) {
            pagerAdapter = LockerPagerAdapter(this)
        }

        // ViewPager2와 어댑터 연결
        binding.viewPager2.adapter = pagerAdapter

        // TabLayout과 ViewPager2 연결
        TabLayoutMediator(binding.tabLayout2, binding.viewPager2) { tab, position ->
            when (position) {
                0 -> tab.text = "저장한 곡"
                1 -> tab.text = "음악파일"
            }
        }.attach()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // 메모리 누수 방지
    }
}