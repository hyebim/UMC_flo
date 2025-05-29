package umcandroid.essential.week02_flo_1.ui.main.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import umcandroid.essential.week02_flo_1.R
import umcandroid.essential.week02_flo_1.databinding.FragmentTrackBinding

class TrackFragment : Fragment() {
    lateinit var binding : FragmentTrackBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTrackBinding.inflate(inflater, container, false)

        binding.linearLayout1.setOnClickListener {
            Toast.makeText(activity, "LILAC", Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }


}