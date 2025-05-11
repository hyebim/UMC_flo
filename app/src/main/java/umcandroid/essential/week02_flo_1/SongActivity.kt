package umcandroid.essential.week02_flo_1

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import umcandroid.essential.week02_flo_1.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {

    // ViewBinding 객체
    private lateinit var binding: ActivitySongBinding
    lateinit var timer: Timer
    private var isPlaying = false
    private var mediaPlayer : MediaPlayer? = null

    val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    var nowPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPlayList()
        initSong()

//        if(intent.hasExtra("title") && intent.hasExtra("singer")) {
//            binding.tvSongTitle.text = intent.getStringExtra("title")
//            binding.tvSongSinger.text = intent.getStringExtra("singer")
//        }

        // 예시: 완료 버튼 클릭 시 결과 반환
        val resultIntent = Intent().apply {
            putExtra("title", "라일락")  // 사용자가 수정한 제목
            putExtra("singer", "어아유 (IU)")  // 사용자가 수정한 가수
        }
        setResult(Activity.RESULT_OK, resultIntent)  // 결과 설정

        // iv_down 클릭 시 MainActivity로 돌아가도록 설정
        findViewById<ImageView>(R.id.iv_down).setOnClickListener {
            // SongActivity 종료하고 MainActivity로 돌아감
            finish()  // 현재 Activity 종료
        }

        binding.ivPlay.setOnClickListener {
            isPlaying = !isPlaying  // 먼저 상태를 토글해주고

            setPlayerStatus(isPlaying)  // UI 상태 업데이트

            if (!timer.isAlive && isPlaying) {
                startTimer()  // 멈췄던 쓰레드를 다시 시작
            }
        }

        binding.ivNext.setOnClickListener {
            moveSong(+1)
        }

        binding.ivPrev.setOnClickListener {
            moveSong(-1)
        }

        binding.songLikeIv.setOnClickListener {
            setLike(songs[nowPos].isLike)
        }

        binding.seekBarSong.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val intent = Intent(this@SongActivity, MainActivity::class.java)
                    intent.putExtra("progress", progress)
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })


        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onPause() {
        super.onPause()
        songs[nowPos].second = (songs[nowPos].playTime * binding.seekBarSong.progress) / 100000
        songs[nowPos].isPlaying = false
        setPlayerStatus(false)

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putInt("songId", songs[nowPos].id)
        editor.putInt("second", songs[nowPos].second)
        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()

        // 마지막 SeekBar progress 전달
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("progress", binding.seekBarSong.progress)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    private fun initPlayList() {
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
    }


    //song 데이터 클래스 초기화 함수
    private fun initSong() {
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        nowPos = getPlayingSongPosition(songId)

        Log.d("now Song ID", songs[nowPos].id.toString())

        startTimer()
        setPlayer(songs[nowPos])
    }

    private fun setLike(isLike: Boolean){
        songs[nowPos].isLike = !isLike
        songDB.songDao().updateIsLikeById(!isLike,songs[nowPos].id)

        if (!isLike){
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        } else{
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }

    }

    private fun moveSong(direct: Int) {
        if (nowPos + direct < 0) {
            Toast.makeText(this,"first song",Toast.LENGTH_SHORT).show()
            return
        }
        if (nowPos + direct >= songs.size) {
            Toast.makeText(this,"last song",Toast.LENGTH_SHORT).show()
            return
        }

        nowPos += direct
        timer.interrupt()
        startTimer()

        mediaPlayer?.release()
        mediaPlayer = null

        setPlayer(songs[nowPos])

    }

    private fun getPlayingSongPosition(songId: Int): Int{
        for (i in 0 until songs.size){
            if (songs[i].id == songId){
                return i
            }
        }
        return 0
    }


    //songactivity 화면에 받아와서 초기화 된 song에 대한 data를 뷰 랜더링
    private fun setPlayer(song: Song) {
        binding.tvSongTitle.text = song.title
        binding.tvSongSinger.text = song.singer
        binding.tvSongStartTime.text = String.format("%02d:%02d", song.second / 60, song.second % 60)
        binding.tvSongEndTime.text = String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
        binding.imageView.setImageResource(song.coverImg!!)
        binding.seekBarSong.progress = (song.second * 1000 / song.playTime)

        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)

        if(song.isLike) {
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        }
        else {
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }

        setPlayerStatus(song.isPlaying)
    }

    private fun startTimer() {
        timer = Timer(songs[nowPos].playTime, songs[nowPos].isPlaying)
        timer.start()
    }

    private fun setPlayerStatus (isPlaying : Boolean) {
        songs[nowPos].isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if(isPlaying) {
            binding.ivPlay.setImageResource(R.drawable.btn_miniplay_pause)  // 플레이 아이콘으로 변경
            //isPlaying = false
        } else {
            // 재생 상태로 변경
            binding.ivPlay.setImageResource(R.drawable.btn_miniplayer_play)  // 일시 정지 아이콘으로 변경
            //isPlaying = true
        }
    }

    //쓰레드에서 시간이 지남에 따라 timer와 seekbar의 값을 바꾸어야하기 때문에 binding 변수를 사용해야하므로 inner 클래스로
    inner class Timer(private val playTime: Int, var isPlaying: Boolean = true): Thread() {
        private var second : Int = 0
        private var mills : Float = 0f

        override fun run() {
            super.run()
            try {
                while (true) {
                    if(second >= playTime) {
                        break
                    }
                    if(isPlaying) {
                        sleep(50)
                        mills += 50

                        runOnUiThread {
                            binding.seekBarSong.progress = ((mills / playTime) * 100).toInt()
                        }
                        if(mills % 1000 == 0f) {
                            runOnUiThread {
                                binding.tvSongStartTime.text = String.format("%02d:%02d", second / 60, second % 60)
                            }
                            second++
                        }
                    }
                }
            }catch (e: InterruptedException) {
                Log.d("Song", "쓰레드가 죽었습니다. ${e.message}")
            }

        }

    }



}