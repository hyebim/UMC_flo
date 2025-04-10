package umcandroid.essential.week02_flo_1

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.coroutines.launch
import umcandroid.essential.week02_flo_1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // registerForActivityResult로 콜백 등록
    private val songActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // SongActivity에서 전달된 데이터를 받음
            val title = result.data?.getStringExtra("title")
            val singer = result.data?.getStringExtra("singer")

            // 받은 데이터를 MainActivity의 UI에 업데이트
            findViewById<TextView>(R.id.tv_miniplayer_title).text = title
            findViewById<TextView>(R.id.tv_miniplayer_singer).text = singer

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Song 정보 초기화
        val song = Song(binding.tvMiniplayerTitle.text.toString(), binding.tvMiniplayerSinger.text.toString(), 0, 60, false)

        binding.bottomLayout.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java).apply {
                //putExtra("title", "라일락")  // 초기값 전달
                //putExtra("singer", "아이유 (IU)")  // 초기값 전달
                putExtra("title", song.title)
                putExtra("singer", song.singer)
                putExtra("second", song.second)
                putExtra("playTime", song.playTime)
                putExtra("isPlaying", song.isPlaying)
                //startActivity(intent)
            }
            songActivityLauncher.launch(intent)  // SongActivity 실행
            //startActivity(intent)
        }

        val navView: BottomNavigationView = binding.bottomNavigation

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.navigation_dashboard,
                R.id.navigation_notifications,
                R.id.navigation_box
            )
        )

        //추가한 코드
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            lifecycleScope.launch {
                val currentDestination = navController.currentDestination?.id

                if (currentDestination != item.itemId) {
                    navController.popBackStack(item.itemId, true) // 백스택 정리
                    navController.navigate(item.itemId) // Fragment 전환
                }
            }

            true
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


    }

    private val seekBarReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val progress = intent?.getIntExtra("progress", 0) ?: 0
            binding.seekBarMain.progress = progress
        }
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter("com.example.UPDATE_SEEKBAR")
        registerReceiver(seekBarReceiver, filter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(seekBarReceiver)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent?.getIntExtra("progress", -1)?.let { progress ->
            if (progress >= 0) {
                binding.seekBarMain.progress = progress
            }
        }
    }


}