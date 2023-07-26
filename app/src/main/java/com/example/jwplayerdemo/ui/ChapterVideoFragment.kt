package com.example.jwplayerdemo.ui


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.jwplayerdemo.adapter.ChapterVideoAdapter
import com.example.jwplayerdemo.databinding.FragmentChapterDetailBinding
import com.example.jwplayerdemo.models.NetworkResult
import com.example.jwplayerdemo.models.VideoContent
import com.example.jwplayerdemo.viewmodel.ChapterVideoViewModel


class ChapterVideoFragment : Fragment() {

    private var _binding: FragmentChapterDetailBinding? = null
    private lateinit var viewModel: ChapterVideoViewModel
    private val chapterVideoAdapter = ChapterVideoAdapter { video ->
        findNavController().navigate(
            ChapterVideoFragmentDirections.actionChapterVideoFragmentToDrmVideoFragment(
                VideoContent(
                    chapterName = video.chapterName,
                    chapterId = video.chapterId,
                    topicName = video.topicName,
                    topicId = video.topicId,
                    id = video.id,
                    subjectName = video.subjectName,
                    subjectId = video.subjectId,
                    jwMediaId = video.jwMediaId,
                    contentType = video.contentType,
                    contentName = video.contentName,
                    contentUrl = video.contentUrl,
                    thumbNailUrl = video.thumbNailUrl,
                    videoId = video.videoId,
                    videoSource = video.videoSource,
                    isDrm = video.isDrm
                ), toolbarTitle = video.contentName
            )
        )
    }
    // private val args: ChapterVideoFragmentArgs by navArgs()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentChapterDetailBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ChapterVideoViewModel::class.java]
        binding.recyclerView.adapter = chapterVideoAdapter
        viewModel.responseData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    binding.progressIndicator.visibility = View.GONE
                    response.data?.let {
                        chapterVideoAdapter.submitList(it.videoList!!)
                    }
                }

                is NetworkResult.Error -> {
                    binding.progressIndicator.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is NetworkResult.Loading -> {
                    binding.progressIndicator.visibility = View.VISIBLE
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}