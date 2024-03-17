package com.example.swiftconnect.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swiftconnect.Model.Post
import com.example.swiftconnect.Model.User
import com.example.swiftconnect.R
import com.example.swiftconnect.adapters.FollowAdapter
import com.example.swiftconnect.adapters.PostAdapter
import com.example.swiftconnect.databinding.FragmentHomeBinding
import com.example.swiftconnect.utils.FOLLOW
import com.example.swiftconnect.utils.POST
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private var postList = ArrayList<Post>()
    private lateinit var adapter: PostAdapter

    private lateinit var followAdapter: FollowAdapter
    private var followList = ArrayList<User>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentHomeBinding.inflate(inflater, container, false)
        adapter = PostAdapter(requireContext(), postList)
        binding.postRv.layoutManager = LinearLayoutManager(requireContext())
        binding.postRv.adapter = adapter
        (requireContext() as AppCompatActivity).setSupportActionBar(binding.materialToolbar)
        setHasOptionsMenu(true)

        binding.followRv.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        followAdapter = FollowAdapter(requireContext(), followList)
        binding.followRv.adapter = followAdapter

        loadFollowData()
        loadData()

        return binding.root
    }

    private fun loadFollowData() {
        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid+ FOLLOW).get()
            .addOnSuccessListener {
                var tempList = ArrayList<User>()
                followList.clear()
                for(i in it.documents){
                    val post = i.toObject<User>()!!
                    tempList.add(post)
                }

                followList.addAll(tempList)
                followAdapter.notifyDataSetChanged()
            }
    }

    private fun loadData() {
        Firebase.firestore.collection(POST).get().addOnSuccessListener {
            var tempList = ArrayList<Post>()
            postList.clear()
            for(i in it.documents){
                val post = i.toObject<Post>()!!
                tempList.add(post)
            }

            postList.addAll(tempList)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.option_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}