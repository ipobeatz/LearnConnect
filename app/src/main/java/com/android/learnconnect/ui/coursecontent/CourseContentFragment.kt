package com.android.learnconnect.ui.coursecontent

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.learnconnect.R
import com.android.learnconnect.databinding.FragmentCourseContentBinding
import com.android.learnconnect.domain.entity.VideoItem
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.ui.PlayerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import java.io.InputStreamReader

@AndroidEntryPoint
class CourseContentFragment : Fragment() {

    private var _binding: FragmentCourseContentBinding? = null
    private val binding get() = _binding!!

    private var player: ExoPlayer? = null
    private var isFullscreen = false
    private var currentVideoIndex = 0
    private lateinit var videoList: List<VideoItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCourseContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // JSON'dan videoları yükle
        videoList = loadVideosFromJson()

        // ExoPlayer ayarları
        player = ExoPlayer.Builder(requireContext()).build()
        binding.playerView.player = player

        // Kontrolleri ayarla
        setupControls()

        // RecyclerView ayarları
        binding.videoRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.videoRecyclerView.adapter = VideoPlayerAdapter(videoList,
            onItemClick = { video ->
                playVideo(video.videoUrl)
                currentVideoIndex = videoList.indexOf(video)
            },
            onDownloadClick = { video ->
                Toast.makeText(requireContext(), "${video.title} indiriliyor...", Toast.LENGTH_SHORT).show()
                // İndirme işlemini burada başlatabilirsiniz.
            }
        )

        // İlk videoyu oynat
        if (videoList.isNotEmpty()) {
            playVideo(videoList.first().videoUrl)
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isFullscreen) {
                    toggleFullscreen()
                } else {
                    isEnabled = false
                    requireActivity().onBackPressed()
                }
            }
        }

        // Callback'i bu fragment'in OnBackPressedDispatcher'ına ekleyin
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun setupControls() {
        // Oynat/Durdur Butonu
        val playPauseButton: ImageButton = binding.playerView.findViewById(R.id.exo_play_pause)
        playPauseButton.setOnClickListener {
            if (player?.isPlaying == true) {
                player?.pause()
                playPauseButton.setImageResource(android.R.drawable.ic_media_play)
            } else {
                player?.play()
                playPauseButton.setImageResource(android.R.drawable.ic_media_pause)
            }
        }

        // Sonraki Butonu
        val nextButton: ImageButton = binding.playerView.findViewById(R.id.exo_next)
        nextButton.setOnClickListener {
            playNextVideo()
        }

        // Hız Ayarlama Butonu
        val speedButton: ImageView = binding.playerView.findViewById(R.id.btn_speed)
        speedButton.setOnClickListener {
            showSpeedOptions()
        }

        // Tam Ekran Butonu
        val fullScreenButton: ImageView = binding.playerView.findViewById(R.id.btn_fullscreen)
        fullScreenButton.setOnClickListener {
            toggleFullscreen()
        }
    }

    private fun showSpeedOptions() {
        val speedOptions = arrayOf("0.5x", "1x", "1.5x", "2x")
        val speedValues = arrayOf(0.5f, 1f, 1.5f, 2f)

        AlertDialog.Builder(requireContext())
            .setTitle("Oynatma Hızı")
            .setItems(speedOptions) { dialog, which ->
                val params = PlaybackParameters(speedValues[which])
                player?.playbackParameters = params
            }
            .show()
    }

    private fun toggleFullscreen() {
        if (isFullscreen) {
            // Portre moda geçiş
            requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE

            // PlayerView boyutlarını güncelle
            val params = binding.playerView.layoutParams
            params.height = resources.getDimensionPixelSize(R.dimen.player_default_height)
            binding.playerView.layoutParams = params

            // RecyclerView'ı görünür yap
            binding.videoRecyclerView.visibility = View.VISIBLE

        } else {
            // Landscape moda geçiş
            requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            requireActivity().window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    )

            // PlayerView boyutlarını güncelle
            val params = binding.playerView.layoutParams
            params.height = ViewGroup.LayoutParams.MATCH_PARENT
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            binding.playerView.layoutParams = params

            // RecyclerView'ı gizle
            binding.videoRecyclerView.visibility = View.GONE
        }
        isFullscreen = !isFullscreen
    }

    private fun playNextVideo() {
        if (currentVideoIndex < videoList.size - 1) {
            currentVideoIndex++
            playVideo(videoList[currentVideoIndex].videoUrl)
            // RecyclerView'da seçili öğeyi görünür yap
            binding.videoRecyclerView.scrollToPosition(currentVideoIndex)
        } else {
            Toast.makeText(requireContext(), "Son video oynatılıyor.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadVideosFromJson(): List<VideoItem> {
        val inputStream = requireContext().resources.openRawResource(R.raw.videos)
        val reader = InputStreamReader(inputStream)
        val type = object : TypeToken<List<VideoItem>>() {}.type
        return Gson().fromJson(reader, type)
    }

    private fun playVideo(url: String) {
        player?.apply {
            setMediaItem(MediaItem.fromUri(url))
            prepare()
            playWhenReady = true
        }
        // Oynat/Durdur butonunun ikonunu güncelle
        val playPauseButton: ImageButton = binding.playerView.findViewById(R.id.exo_play_pause)
        playPauseButton.setImageResource(android.R.drawable.ic_media_pause)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        player?.release()
        player = null
    }
}
