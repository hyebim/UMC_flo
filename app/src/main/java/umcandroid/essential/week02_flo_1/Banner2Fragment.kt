package umcandroid.essential.week02_flo_1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import umcandroid.essential.week02_flo_1.databinding.FragmentBanner2Binding

class Banner2Fragment (val imgRes : Int) : Fragment() {
    lateinit var binding : FragmentBanner2Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBanner2Binding.inflate(inflater, container, false)

        binding.ivBackground.setImageResource(imgRes)

        return binding.root
    }
}