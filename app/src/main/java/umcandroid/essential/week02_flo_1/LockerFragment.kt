package umcandroid.essential.week02_flo_1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import umcandroid.essential.week02_flo_1.databinding.FragmentLockerBinding

class LockerFragment : Fragment() {
    private var _binding: FragmentLockerBinding? = null
    private val binding get() = _binding!!

    private lateinit var pagerAdapter: LockerPagerAdapter

    private val information = arrayListOf("저장한곡", "음악파일")

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
        val lockerAdapter = LockerPagerAdapter(this)
        binding.viewPager2.adapter = lockerAdapter
        TabLayoutMediator(binding.tabLayout2, binding.viewPager2) { tab, position ->
            tab.text = information[position]
        }.attach()

        // BottomSheetFragment에 전달할 데이터를 준비
        val bottomSheetFragment = BottomSheetFragment()

        binding.lockerSelectAllTv.setOnClickListener {
            val bottomSheetFragment = BottomSheetFragment()
            bottomSheetFragment.setOnDeleteClickListener(object : BottomSheetFragment.OnDeleteClickListener {
                override fun onDeleteClicked() {
                    val currentFragment =
                        childFragmentManager.findFragmentByTag("f" + binding.viewPager2.currentItem)

                    if (currentFragment is SavedTracksFragment) {
                        currentFragment.deleteAllSongs()
                    }
                }
            })
            bottomSheetFragment.show(parentFragmentManager, "BottomSheetDialog")
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // 메모리 누수 방지
    }
}


