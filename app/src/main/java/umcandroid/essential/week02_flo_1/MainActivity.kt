package umcandroid.essential.week02_flo_1

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
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
import com.google.gson.Gson
import kotlinx.coroutines.launch
import umcandroid.essential.week02_flo_1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var song: Song = Song()
    private var gson: Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputDummySongs()
        inputDummyAlbums()

        binding.bottomLayout.setOnClickListener {
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songId", song.id)
            editor.apply()

            val intent = Intent(this,SongActivity::class.java)
            startActivity(intent)
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

    private val playReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "com.example.PLAY_SONG_DUMMY") {
                val title = intent.getStringExtra("title")
                val singer = intent.getStringExtra("singer")
                val playTime = intent.getIntExtra("playTime", 60)

                binding.tvMiniplayerTitle.text = title
                binding.tvMiniplayerSinger.text = singer

                // SeekBar 설정
                binding.seekBarMain.max = playTime
                binding.seekBarMain.progress = 0

                // SeekBar 동기화 (1초마다 증가)
                object : Thread() {
                    override fun run() {
                        for (i in 0..playTime) {
                            // UI 스레드에서 SeekBar 진행 상태 업데이트
                            runOnUiThread {
                                binding.seekBarMain.progress = i
                            }
                            sleep(1000) // 1초마다 업데이트
                        }
                    }
                }.start()
            }
        }
    }

    private fun setMiniPlayer(song : Song) {
        binding.tvMiniplayerTitle.text = song.title
        binding.tvMiniplayerSinger.text = song.singer
        binding.seekBarMain.progress = (song.second * 100000 / song.playTime)
    }

    override fun onStart() {
        super.onStart()
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        val songDB = SongDatabase.getInstance(this)!!

        song = if(songId == 0) {
            songDB.songDao().getSong(1)
        } else{
            songDB.songDao().getSong(songId)
        }

        Log.d("song ID", song.id.toString())
        setMiniPlayer(song)
    }
    override fun onResume() {
        super.onResume()
        val filter = IntentFilter("com.example.UPDATE_SEEKBAR")
        registerReceiver(playReceiver, IntentFilter("com.example.PLAY_SONG_DUMMY"))
        registerReceiver(seekBarReceiver, filter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(seekBarReceiver)
        unregisterReceiver(playReceiver)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent?.getIntExtra("progress", -1)?.let { progress ->
            if (progress >= 0) {
                binding.seekBarMain.progress = progress
            }
        }
    }

    private fun inputDummySongs(){
        val songDB = SongDatabase.getInstance(this)!!
        val songs = songDB.songDao().getSongs()

        if (songs.isNotEmpty()) return

        songDB.songDao().insert(
            Song(
                "Lilac",
                "아이유 (IU)",
                0,
                200,
                false,
                "music_lilac",
                R.drawable.img_album_exp2,
                false,
                1
            )
        )

        songDB.songDao().insert(
            Song(
                "Flu",
                "아이유 (IU)",
                0,
                200,
                false,
                "music_flu",
                R.drawable.img_album_exp2,
                false,
                1
            )
        )

        songDB.songDao().insert(
            Song(
                "Butter",
                "방탄소년단 (BTS)",
                0,
                190,
                false,
                "music_butter",
                R.drawable.img_album_exp,
                false,
                2
            )
        )

        songDB.songDao().insert(
            Song(
                "Next Level",
                "에스파 (AESPA)",
                0,
                210,
                false,
                "music_next",
                R.drawable.img_album_exp2,
                false,
                3
            )
        )


        songDB.songDao().insert(
            Song(
                "Boy with Luv",
                "music_boy",
                0,
                230,
                false,
                "music_butter",
                R.drawable.img_album_exp,
                false,
                4
            )
        )

        songDB.songDao().insert(
            Song(
                "BBoom",
                "momoland",
                0,
                230,
                false,
                "music_bboom",
                R.drawable.img_album_exp,
                false,
                5
                )
        )

        val _songs = songDB.songDao().getSongs()
        Log.d("DB data", _songs.toString())
    }

    private fun inputDummyAlbums(){
        val songDB = SongDatabase.getInstance(this)!!
        val albums = songDB.albumDao().getAlbums()

        if (albums.isNotEmpty()) return

        songDB.albumDao().insert(
            Album(
                1,
                "IU 5th Album 'LILAC'",
                "아이유 (IU)",
                R.drawable.img_album_exp2
            )
        )

        songDB.albumDao().insert(
            Album(
                2,
                "Butter",
                "방탄소년단 (BTS)",
                R.drawable.img_album_exp
            )
        )

        songDB.albumDao().insert(
            Album(
                3,
                "iScreaM Vol.10: Next Level Remixes",
                "에스파 (AESPA)",
                R.drawable.img_album_exp2
            )
        )

        songDB.albumDao().insert(
            Album(
                4,
                "Map of the Soul Persona",
                "뮤직 보이 (Music Boy)",
                R.drawable.img_album_exp,
            )
        )


        songDB.albumDao().insert(
            Album(
                5,
                "Great!",
                "모모랜드 (MOMOLAND)",
                R.drawable.img_album_exp2
            )
        )

        val songDBData = songDB.albumDao().getAlbums()
        Log.d("DB data", songDBData.toString())
    }

}