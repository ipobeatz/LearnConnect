package com.android.learnconnect.ui.coursedetail

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.learnconnect.R
import com.android.learnconnect.databinding.FragmentCourseDetailBinding
import com.android.learnconnect.domain.entity.Course
import com.android.learnconnect.domain.entity.ResultData
import com.android.learnconnect.domain.entity.VideoItem
import com.android.learnconnect.ui.coursecontent.VideoPlayerAdapter
import com.bumptech.glide.RequestManager
import com.google.android.exoplayer2.ExoPlayer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CourseDetailFragment @Inject constructor() : Fragment() {

    private var _binding: FragmentCourseDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var player: ExoPlayer
    private val viewModel: CourseDetailViewModel by viewModels()
    private val args: CourseDetailFragmentArgs by navArgs()
    private var isRegistered = false
    private var isFavorite = false

    @Inject
    lateinit var glide: RequestManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCourseDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val courseId = args.videoId
        viewModel.getCourseDataFromId(courseId)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.courseData.collectLatest {
                    when (it) {
                        is ResultData.Success -> {
                            setupUI(it.data)
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
    private fun setupRecyclerView() {
        val videoAdapter = VideoPlayerAdapter(requireContext(), emptyList(), this::onVideoItemClick, this::onDownloadClick)
        binding.courseContentRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = videoAdapter
        }
    }

    private fun onVideoItemClick(videoItem: VideoItem) {
        if (isRegistered) {
            val action = CourseDetailFragmentDirections
                .actionCourseDetailFragmentToCourseContentFragment(videoItem.id)
            findNavController().navigate(action)
        } else {
            // Kullanıcı kayıtlı değilse mesaj göster
            Toast.makeText(requireContext(), "Derse kaydolmanız gerekiyor", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onDownloadClick(videoItem: VideoItem) {
        if (isRegistered) {
            downloadVideo(requireContext(), videoItem.videoUrl, videoItem.title)
        } else {
            // Kullanıcı kayıtlı değilse mesaj göster
            Toast.makeText(requireContext(), "Derse kaydolmanız gerekiyor", Toast.LENGTH_SHORT).show()
        }
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
            val downloadId = downloadManager.enqueue(request)

            // İndirme tamamlandığında bildirim yapmak için BroadcastReceiver
            val onComplete = object : BroadcastReceiver() {
                override fun onReceive(ctxt: Context?, intent: Intent?) {
                    val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                    if (id == downloadId) {
                        Toast.makeText(context, "$videoTitle indirildi!", Toast.LENGTH_LONG).show()
                        context.unregisterReceiver(this) // Receiver'ı temizle
                    }
                }
            }

            context.registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

            Toast.makeText(context, "$videoTitle indiriliyor...", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "İndirme başarısız oldu: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }


    private fun setupUI(course: Course) {
        isRegistered = course.isRegistered
        isFavorite = course.isFavorite
        (binding.courseContentRecyclerView.adapter as VideoPlayerAdapter).submitList(course.videoItem)
        // Toast mesajını kaldırabilirsiniz
        // binding.apply içinde aşağıdaki değişiklikleri yapın:
        binding.apply {

            detailCourseName.text = course.name
            detailCourseDescription.text = course.description
            detailCoursePrice.text = course.coursePrice.toString()
            glide.load(course.imageUrl).placeholder(R.drawable.studio).into(detailCourseImage)

            if (isRegistered) {
                // Kullanıcı kursa kayıtlıysa butonu gizle ve overlay'i gizle
                enrollButton.visibility = View.GONE
                lockOverlay.visibility = View.GONE
            } else {
                // Kullanıcı kayıtlı değilse butonu göster ve overlay'i göster
                enrollButton.visibility = View.VISIBLE
                lockOverlay.visibility = View.VISIBLE
                enrollButton.text = "Kursa Kaydolun"
                enrollButton.setOnClickListener {
                    viewModel.registerToCourse(courseId = course.id)
                    isRegistered = true
                    binding.enrollButton.visibility = View.GONE // Kaydolduktan sonra butonu gizle
                    binding.lockOverlay.visibility = View.GONE // Overlay'i gizle
                    Toast.makeText(
                        requireContext(), "Kursa başarıyla kaydoldunuz!", Toast.LENGTH_SHORT
                    ).show()
                }
            }

            setFavoriteIcon()

            favoriteButton.setOnClickListener {
                if (isFavorite) {
                    viewModel.setCourseFavorite(course.id, false)
                    isFavorite = false
                    setFavoriteIcon()
                } else {
                    viewModel.setCourseFavorite(course.id, true)
                    isFavorite = true
                    setFavoriteIcon()
                }
            }
        }
    }

    private fun setFavoriteIcon() {
        binding.favoriteButton.setImageResource(
            if (isFavorite) R.drawable.love_svgrepo_com else R.drawable.love_svgrepo_com_2
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
