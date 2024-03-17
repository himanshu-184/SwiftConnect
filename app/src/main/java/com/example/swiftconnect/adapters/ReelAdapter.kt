package com.example.swiftconnect.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.swiftconnect.Model.Reel
import com.example.swiftconnect.R
import com.example.swiftconnect.databinding.ReelDesignBinding
import com.squareup.picasso.Picasso

class ReelAdapter(var context: Context, val reelList: ArrayList<Reel>): RecyclerView.Adapter<ReelAdapter.ViewHolder>(){
    inner class ViewHolder(var binding: ReelDesignBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ReelDesignBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return reelList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(reelList[position].profileLink).placeholder(R.drawable.boy).into(holder.binding.profileImg)
        holder.binding.caption.text = reelList[position].caption
        holder.binding.videoView.setVideoPath(reelList[position].reelUrl)
        holder.binding.videoView.setOnPreparedListener{
            holder.binding.progressBar.visibility = View.GONE
            holder.binding.videoView.start()
        }
    }
}