package umcandroid.essential.week02_flo_1

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import umcandroid.essential.week02_flo_1.ui.home.DetailFragment
import umcandroid.essential.week02_flo_1.ui.home.TrackFragment


class MyPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    private val NUM_PAGES = 3

    override fun getItemCount(): Int = NUM_PAGES

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TrackFragment() // 수록곡 Fragment
            1 -> DetailFragment() // 상세정보 Fragment
            2 -> VideoFragment() // 영상 Fragment
            else -> AlbumFragment()
        }
    }
}