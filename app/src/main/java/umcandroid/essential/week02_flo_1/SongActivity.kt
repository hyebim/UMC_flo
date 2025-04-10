package umcandroid.essential.week02_flo_1

import android.app.Activity
import android.content.Intent
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
    lateinit var song: Song
    lateinit var timer: Timer
    private var isPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSong()
        setPlayer(song)

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

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()

        // 마지막 SeekBar progress 전달
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("progress", binding.seekBarSong.progress)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    //song 데이터 클래스 초기화 함수
    private fun initSong() {
        if (intent.hasExtra("title") && intent.hasExtra("singer")) {
            song = Song(
                intent.getStringExtra("title")!!,
                intent.getStringExtra("singer")!!,
                intent.getIntExtra("second", 0),
                intent.getIntExtra("playTime", 0),
                intent.getBooleanExtra("isPlaying", false)
            )
        }
        startTimer()
    }

    //songactivity 화면에 받아와서 초기화 된 song에 대한 data를 뷰 랜더링
    private fun setPlayer(song: Song) {
        binding.tvSongTitle.text = intent.getStringExtra("title")!!
        binding.tvSongSinger.text = intent.getStringExtra("singer")!!
        binding.tvSongStartTime.text = String.format("%02d:%02d", song.second / 60, song.second % 60)
        binding.tvSongEndTime.text = String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
        binding.seekBarSong.progress = (song.second * 1000 / song.playTime)

        setPlayerStatus(song.isPlaying)
    }

    private fun startTimer() {
        timer = Timer(song.playTime, song.isPlaying)
        timer.start()
    }

    private fun setPlayerStatus (isPlaying : Boolean) {
        song.isPlaying = isPlaying
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