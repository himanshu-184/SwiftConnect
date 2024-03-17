package com.example.swiftconnect.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.swiftconnect.Model.Post
import com.example.swiftconnect.Model.Reel
import com.example.swiftconnect.R
import com.example.swiftconnect.adapters.MyPostRvAdapter
import com.example.swiftconnect.adapters.MyReelAdapter
import com.example.swiftconnect.databinding.FragmentMyReelsBinding
import com.example.swiftconnect.utils.REEL
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase


class MyReelsFragment : Fragment() {
    private lateinit var binding: FragmentMyReelsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentMyReelsBinding.inflate(inflater, container, false)

        var reelList = ArrayList<Reel>()
        var adapter = MyReelAdapter(requireContext(), reelList)
        binding.rv.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.rv.adapter = adapter

        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid+ REEL).get().addOnSuccessListener {
            var tempList = ArrayList<Reel>()
            for(i in it.documents){
                tempList.add(i.toObject<Reel>()!!)
            }
            reelList.addAll(tempList)
            adapter.notifyDataSetChanged()
        }

        return binding.root
    }
}