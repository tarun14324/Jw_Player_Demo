package com.example.jwplayerdemo.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jwplayerdemo.databinding.VideoListBinding
import com.example.jwplayerdemo.models.VideoContent


class ChapterVideoAdapter(
    val onVideoClicked: (VideoContent) -> Unit
) : RecyclerView.Adapter<ChapterVideoAdapter.ViewHolder>() {

    var listItems: ArrayList<VideoContent> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(data: List<VideoContent>) {

        listItems.clear()

        listItems.addAll(ArrayList(data))
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: VideoListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = VideoListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            binding.item = listItems[layoutPosition]
            Glide.with(binding.root.context)
                .load(listItems[layoutPosition].thumbNailUrl)
                .into(binding.ivSubject)
            binding.cardView.setOnClickListener {
               onVideoClicked(listItems[layoutPosition])
                Log.e("TAG", "adapter: ${listItems[layoutPosition].isDrm}", )
            }
        }

    }

    override fun getItemCount(): Int {
        return listItems.size
    }
}

