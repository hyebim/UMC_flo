package umcandroid.essential.week02_flo_1

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import umcandroid.essential.week02_flo_1.databinding.ItemSavedTrackBinding

class TrackRVAdapter(): RecyclerView.Adapter<TrackRVAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onShowBottomSheet(songId: Int, position: Int)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    private val songs = ArrayList<Song>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemSavedTrackBinding =
            ItemSavedTrackBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(songs[position])
    }

    override fun getItemCount(): Int = songs.size

    fun getCurrentItems(): List<Song> = songs

    @SuppressLint("NotifyDataSetChanged")
    fun removeAll() {
        songs.clear()
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        songs.removeAt(position)
        notifyItemRemoved(position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addSongs(songs: ArrayList<Song>) {
        this.songs.clear()
        this.songs.addAll(songs)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemSavedTrackBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(song: Song) {
            binding.tvMain1.text = song.title
            binding.tvSinger1.text = song.singer
            binding.ivAlbum.setImageResource(song.coverImg!!)

            binding.ivMore1.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    itemClickListener?.onShowBottomSheet(song.id, pos)
                }
            }


        }
    }
}

