package umcandroid.essential.week02_flo_1

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import umcandroid.essential.week02_flo_1.databinding.ItemAlbumBinding
import umcandroid.essential.week02_flo_1.databinding.ItemSavedTrackBinding

class TrackRVAdapter(): RecyclerView.Adapter<TrackRVAdapter.ViewHolder>() {

//    interface MyItemClickListener{
//        fun onRemoveTrack(position: Int)
//    }

    private val songs = ArrayList<Song>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): TrackRVAdapter.ViewHolder {
        val binding: ItemSavedTrackBinding = ItemSavedTrackBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrackRVAdapter.ViewHolder, position: Int) {
        holder.bind(songs[position])
    }

    override fun getItemCount(): Int = songs.size

    @SuppressLint("NotifyDataSetChanged")
    fun addSongs(songs: ArrayList<Song>) {
        this.songs.clear()
        this.songs.addAll(songs)
        notifyDataSetChanged()
    }
//    fun removeItem(position: Int){
//        trackList.removeAt(position)
//        notifyDataSetChanged()
//    }

    inner class ViewHolder(val binding: ItemSavedTrackBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(song: Song) {
            binding.tvMain1.text = song.title
            binding.tvSinger1.text = song.singer
            binding.ivAlbum.setImageResource(song.coverImg!!)

            binding.ivMore1.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    songs.removeAt(pos)
                    notifyItemRemoved(pos)
                }
            }

            // 스위치 리스너 제거 → 상태 반영 → 리스너 재등록
//            binding.switchButton.setOnCheckedChangeListener(null) // 기존 리스너 제거
//            binding.switchButton.isChecked = track.isSwitchOn     // 상태 설정
//            binding.switchButton.setOnCheckedChangeListener { _, isChecked ->
//                track.isSwitchOn = isChecked
//            }
        }

    }
}