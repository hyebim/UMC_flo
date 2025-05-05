package umcandroid.essential.week02_flo_1

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import umcandroid.essential.week02_flo_1.databinding.ItemAlbumBinding
import umcandroid.essential.week02_flo_1.databinding.ItemSavedTrackBinding

class TrackRVAdapter(private val trackList: ArrayList<Track>): RecyclerView.Adapter<TrackRVAdapter.ViewHolder>() {

//    interface MyItemClickListener{
//        fun onRemoveTrack(position: Int)
//    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): TrackRVAdapter.ViewHolder {
        val binding: ItemSavedTrackBinding = ItemSavedTrackBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrackRVAdapter.ViewHolder, position: Int) {
        holder.bind(trackList[position])
    }

    override fun getItemCount(): Int = trackList.size

//    fun removeItem(position: Int){
//        trackList.removeAt(position)
//        notifyDataSetChanged()
//    }

    inner class ViewHolder(val binding: ItemSavedTrackBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(track: Track) {
            binding.tvMain1.text = track.title
            binding.tvSinger1.text = track.singer
            binding.ivAlbum.setImageResource(track.coverImg!!)

            binding.ivMore1.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    trackList.removeAt(pos)
                    notifyItemRemoved(pos)
                }
            }

            // 스위치 상태 초기화
            binding.switchButton.isChecked = track.isSwitchOn

            // 스위치 상태가 변경되면 Track 객체의 isSwitchOn 값도 업데이트
            binding.switchButton.setOnCheckedChangeListener { _, isChecked ->
                track.isSwitchOn = isChecked // 상태 업데이트
            }
        }

    }
}