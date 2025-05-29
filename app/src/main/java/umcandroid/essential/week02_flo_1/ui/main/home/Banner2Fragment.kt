package umcandroid.essential.week02_flo_1.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import umcandroid.essential.week02_flo_1.databinding.FragmentBanner2Binding

class Banner2Fragment : Fragment() {

    lateinit var binding: FragmentBanner2Binding
    private var imgRes: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imgRes = arguments?.getInt("imgRes") ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBanner2Binding.inflate(inflater, container, false)
        binding.ivBackground.setImageResource(imgRes)
        return binding.root
    }

    companion object {
        fun newInstance(imgRes: Int): Banner2Fragment {
            val fragment = Banner2Fragment()
            val args = Bundle()
            args.putInt("imgRes", imgRes)
            fragment.arguments = args
            return fragment
        }
    }
}
