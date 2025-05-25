package umcandroid.essential.week02_flo_1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import umcandroid.essential.week02_flo_1.databinding.FragmentBannerBinding

class BannerFragment() : Fragment() {  // 기본 생성자 추가됨
    private lateinit var binding: FragmentBannerBinding
    private var imgRes: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Arguments에서 imgRes 값 읽기
        imgRes = arguments?.getInt(ARG_IMG_RES) ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBannerBinding.inflate(inflater, container, false)
        binding.bannerImageIv.setImageResource(imgRes)
        return binding.root
    }

    companion object {
        private const val ARG_IMG_RES = "imgRes"

        // 팩토리 메서드로 Fragment 생성 & Argument 세팅
        fun newInstance(imgRes: Int): BannerFragment {
            val fragment = BannerFragment()
            val args = Bundle()
            args.putInt(ARG_IMG_RES, imgRes)
            fragment.arguments = args
            return fragment
        }
    }
}
