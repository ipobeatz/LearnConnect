package com.android.learnconnect.ui.coursecontent

import android.app.DownloadManager
import android.content.Context
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.android.learnconnect.R
import com.android.learnconnect.databinding.FragmentCourseContentBinding
import com.android.learnconnect.domain.entity.ResultData
import com.android.learnconnect.domain.entity.VideoItem
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackParameters
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CourseContentFragment : Fragment() {

    private var _binding: FragmentCourseContentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CourseContentViewModel by viewModels()
    private val args: CourseContentFragmentArgs by navArgs()
    private var player: ExoPlayer? = null
    private var isFullscreen = false
    private var currentVideoIndex = 0
    private lateinit var videoList: List<VideoItem>
    private var courseId = "0"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCourseContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        courseId = args.courseId
        viewModel.getCourseData(courseId)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.courseData.collectLatest {
                    when (it) {
                        is ResultData.Success -> {
                            videoList = it.data.videoItem
                            setupUI()
                        }

                        is ResultData.Error -> {

                        }

                        is ResultData.Loading -> {

                        }
                    }
                }
            }
        }

    }

    private fun setupUI() {


        player = ExoPlayer.Builder(requireContext()).build()
        binding.playerView.player = player


        setupControls()


        if (videoList.isNotEmpty()) {
            playVideo(videoList.first())
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE

                isEnabled = false
                requireActivity().onBackPressed()
            }
        }


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun setupControls() {

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


        val nextButton: ImageButton = binding.playerView.findViewById(R.id.exo_next)
        nextButton.setOnClickListener {
            playNextVideo()
        }


        val speedButton: ImageView = binding.playerView.findViewById(R.id.btn_speed)
        speedButton.setOnClickListener {
            showSpeedOptions()
        }


        val fullScreenButton: ImageView = binding.playerView.findViewById(R.id.btn_fullscreen)
        toggleFullscreen()
    }

    private fun showSpeedOptions() {
        val speedOptions = arrayOf("0.5x", "1x", "1.5x", "2x")
        val speedValues = arrayOf(0.5f, 1f, 1.5f, 2f)

        AlertDialog.Builder(requireContext()).setTitle("Oynatma Hızı")
            .setItems(speedOptions) { dialog, which ->
                val params = PlaybackParameters(speedValues[which])
                player?.playbackParameters = params
            }.show()
    }

    private fun downloadVideo(context: Context, videoUrl: String, videoTitle: String) {
        try {
            val request = DownloadManager.Request(Uri.parse(videoUrl)).apply {
                setTitle(videoTitle)
                setDescription("Video indiriliyor...")
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS, "$videoTitle.mp4"
                )
                setAllowedOverMetered(true)
                setAllowedOverRoaming(true)
            }

            val downloadManager =
                context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(request)

            Toast.makeText(context, "$videoTitle indiriliyor...", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "İndirme başarısız oldu: ${e.message}", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun toggleFullscreen() {
        if (isFullscreen) {

            requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE


            val params = binding.playerView.layoutParams
            params.height = resources.getDimensionPixelSize(R.dimen.player_default_height)
            binding.playerView.layoutParams = params


        } else {

            requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            requireActivity().window.decorView.systemUiVisibility =
                (View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)

            val params = binding.playerView.layoutParams
            params.height = ViewGroup.LayoutParams.MATCH_PARENT
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            binding.playerView.layoutParams = params
        }
        isFullscreen = !isFullscreen
    }

    private fun playNextVideo() {
        if (currentVideoIndex < videoList.size - 1) {
            currentVideoIndex++
            playVideo(videoList[currentVideoIndex])
        } else {
            Toast.makeText(requireContext(), "Son video oynatılıyor.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun playVideo(videoItem: VideoItem) {
        player?.apply {
            setMediaItem(MediaItem.fromUri(videoItem.videoUrl))
            prepare()
            playWhenReady = true
        }
        val playPauseButton: ImageButton = binding.playerView.findViewById(R.id.exo_play_pause)
        playPauseButton.setImageResource(android.R.drawable.ic_media_pause)
        if (videoItem.lastSecond != 0) {
            player?.seekTo(videoItem.lastSecond.toLong() * 1000)
        }
    }

    override fun onStop() {
        viewModel.updateLastSecond(
            courseId = courseId,
            videoId = videoList.get(currentVideoIndex).id,
            lastSecond = ((player?.currentPosition ?: 0L) / 1000).toInt()
        )
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        player?.release()
        player = null
    }
}