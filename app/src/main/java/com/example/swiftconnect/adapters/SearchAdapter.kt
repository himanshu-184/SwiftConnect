package com.example.swiftconnect.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.swiftconnect.Model.User
import com.example.swiftconnect.databinding.SearchRvDesignBinding
import com.example.swiftconnect.utils.FOLLOW
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SearchAdapter(var context: Context, var searchList: ArrayList<User>): RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    inner class ViewHolder(var binding: SearchRvDesignBinding): RecyclerView.ViewHolder(binding.root)
    var isFollow = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SearchRvDesignBinding.inflate(LayoutInflater.from(context),parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return searchList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(searchList[position].image).into(holder.binding.profileImg)
        holder.binding.nameTxt.text = searchList[position].name



        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid+ FOLLOW)
            .whereEqualTo("email",searchList[position].email).get().addOnSuccessListener {
                if(it.documents.size==0){
                    isFollow = false
                }else{
                    holder.binding.followBtn.text = "Unfollow"
                    isFollow = true
                }
            }

        holder.binding.followBtn.setOnClickListener {

            if(isFollow){
                Firebase.firestore.collection(Firebase.auth.currentUser!!.uid+ FOLLOW)
                    .whereEqualTo("email",searchList[position].email).get().addOnSuccessListener {
                        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid+ FOLLOW)
                            .document(it.documents.get(0).id).delete()

                        holder.binding.followBtn.text = "Follow"
                        isFollow = false
                    }
            }else{
                Firebase.firestore.collection(Firebase.auth.currentUser!!.uid+ FOLLOW).document()
                    .set(searchList[position])

                holder.binding.followBtn.text = "Unfollow"
                isFollow = true
            }

        }

    }
}