package umcandroid.essential.week02_flo_1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
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
    private var isPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.hasExtra("title") && intent.hasExtra("singer")) {
            binding.tvSongTitle.text = intent.getStringExtra("title")
            binding.tvSongSinger.text = intent.getStringExtra("singer")
        }

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

        // iv_play 버튼 클릭 시 이미지 변경
        binding.ivPlay.setOnClickListener {
            if (isPlaying) {
                // 일시 정지 상태로 변경
                binding.ivPlay.setImageResource(R.drawable.btn_miniplayer_play)  // 플레이 아이콘으로 변경
                isPlaying = false
            } else {
                // 재생 상태로 변경
                binding.ivPlay.setImageResource(R.drawable.btn_miniplay_pause)  // 일시 정지 아이콘으로 변경
                isPlaying = true
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}