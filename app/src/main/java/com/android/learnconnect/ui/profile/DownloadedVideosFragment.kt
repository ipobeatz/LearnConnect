package com.android.learnconnect.ui.profile

import android.os.Environment
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.learnconnect.R
import com.android.learnconnect.databinding.FragmentDownloadedVideosBinding
import java.io.File

class DownloadedVideosFragment : Fragment() {

    private var _binding: FragmentDownloadedVideosBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDownloadedVideosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val downloadedVideos = getDownloadedVideos()
        if (downloadedVideos.isEmpty()) {
            Toast.makeText(requireContext(), "Henüz bir video indirilmedi.", Toast.LENGTH_SHORT).show()
            return
        }
        binding.toolbar?.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        val adapter = DownloadedVideosAdapter(downloadedVideos) { videoPath ->
            playVideo(videoPath)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }
    }


    private fun getDownloadedVideos(): List<String> {
        val downloadDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path)
        return downloadDir.listFiles()?.filter { it.extension == "mp4" }?.map { it.absolutePath }
            ?: emptyList()
    }

    private fun playVideo(videoPath: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(Uri.parse(videoPath), "video/*")
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
