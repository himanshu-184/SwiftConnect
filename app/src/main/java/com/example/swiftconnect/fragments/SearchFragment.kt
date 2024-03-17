package com.example.swiftconnect.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swiftconnect.Model.User
import com.example.swiftconnect.R
import com.example.swiftconnect.adapters.SearchAdapter
import com.example.swiftconnect.databinding.FragmentSearchBinding
import com.example.swiftconnect.utils.USER_NODE
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: SearchAdapter
    private var userList =  ArrayList<User>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentSearchBinding.inflate(inflater, container, false)
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        adapter = SearchAdapter(requireContext(), userList)
        binding.rv.adapter = adapter

        loadData()

        binding.searchBtn.setOnClickListener {
            var text = binding.searchView.text.toString()
            if(text.isEmpty()){
                Toast.makeText(requireContext(), "Enter Some Value", Toast.LENGTH_SHORT).show()
            }
            else{
                searchData(text)
            }
        }

        return binding.root
    }

    private fun searchData(text: String) {
        Firebase.firestore.collection(USER_NODE).whereEqualTo("name",text).get().addOnSuccessListener {
            var tempList = ArrayList<User>()
            userList.clear()

            if(it.isEmpty){
                Toast.makeText(requireContext(), "User Not Found", Toast.LENGTH_SHORT).show()
            }
            else{
                for(i in it.documents){

                    if(i.id == Firebase.auth.currentUser!!.uid) continue
                    val user = i.toObject<User>()!!
                    tempList.add(user)
                }
                userList.addAll(tempList)
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun loadData() {
        Firebase.firestore.collection(USER_NODE).get().addOnSuccessListener {
            var tempList = ArrayList<User>()
            userList.clear()

            for(i in it.documents){

                if(i.id == Firebase.auth.currentUser!!.uid) continue
                val user = i.toObject<User>()!!
                tempList.add(user)
            }
            userList.addAll(tempList)
            adapter.notifyDataSetChanged()
        }
    }


}