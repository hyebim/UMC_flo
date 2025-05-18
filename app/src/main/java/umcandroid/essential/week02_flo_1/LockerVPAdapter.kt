package umcandroid.essential.week02_flo_1

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class LockerVPAdapter (fragment: Fragment) : FragmentStateAdapter(fragment) {

    companion object {
        private const val NUM_PAGES = 3
    }

    override fun getItemCount(): Int = NUM_PAGES

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SavedTracksFragment() // 저장한 곡
            1 -> MusicFilesFragment() // 음악 파일
            else -> SavedAlbumFragment()
        }
    }
}