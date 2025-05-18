package umcandroid.essential.week02_flo_1

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import umcandroid.essential.week02_flo_1.databinding.ItemAlbumBinding

class AlbumRVAdapter(private val albumList: List<Album>): RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>() {

    //메인 화면에서 앨범 선택했을 때
    interface MyItemClickListener{
        fun onItemClick(album: Album)
    }

    private lateinit var mItemClickListener: MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AlbumRVAdapter.ViewHolder {
        val binding: ItemAlbumBinding = ItemAlbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumRVAdapter.ViewHolder, position: Int) {
        holder.bind(albumList[position])
        holder.itemView.setOnClickListener { mItemClickListener.onItemClick(albumList[position]) }
//
//        holder.binding.itemAlbumPlayImgIv.setOnClickListener {
//            itemClickListener.onPlayAlbum(albumList[position])
//        }
    }

    override fun getItemCount(): Int = albumList.size

    inner class ViewHolder(val binding: ItemAlbumBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(album: Album){
            binding.tvAlbum4Title.text = album.title
            binding.tvAlbum4Singer.text = album.singer
            binding.ivAlbum4.setImageResource(album.coverImg!!)

            binding.itemAlbumPlayImgIv.setOnClickListener {
                val context = binding.root.context
                val intent = Intent("com.example.PLAY_SONG_DUMMY").apply {
                    putExtra("title", album.title)
                    putExtra("singer", album.singer)
                    putExtra("playTime", 60) // 임의의 재생 시간 (초)
                }
                context.sendBroadcast(intent)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(album : Album)
        fun onPlayAlbum(album : Album)
    }

    private lateinit var itemClickListener : OnItemClickListener

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
}