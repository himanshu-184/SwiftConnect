package com.example.swiftconnect.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.swiftconnect.Model.Like
import com.example.swiftconnect.Model.Post
import com.example.swiftconnect.Model.User
import com.example.swiftconnect.R
import com.example.swiftconnect.databinding.PostRvBinding
import com.example.swiftconnect.utils.LIKES
import com.example.swiftconnect.utils.POST
import com.example.swiftconnect.utils.USER_NODE
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class PostAdapter(var context: Context, var postList: ArrayList<Post>) : RecyclerView.Adapter<PostAdapter.ViewHolder>(){

    inner class ViewHolder(var binding: PostRvBinding): RecyclerView.ViewHolder(binding.root)
    var liked = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PostRvBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var totalLikes:Int = postList[position].likes!!

        /*-----------------------------------------------------------------------------------*/
        try {
            Firebase.firestore.collection(USER_NODE).document(postList[position].Uid!!).get().addOnSuccessListener {
                val user = it.toObject<User>()

                Glide.with(context).load(user!!.image).placeholder(R.drawable.boy).into(holder.binding.profileImg)
                holder.binding.nameTxt.text = user.name
                holder.binding.profileName.text = user.name
            }

        }
        catch (ex:Exception){}
        /*-----------------------------------------------------------------------------------*/
        try{
            val time = TimeAgo.using(postList[position].time!!.toLong())
            holder.binding.timeTxt.text = time
        }
        catch (ex: Exception){
            holder.binding.timeTxt.text = ""
        }
        /*-----------------------------------------------------------------------------------*/

        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid+ LIKES).document(postList[position].postId!!).get()
            .addOnSuccessListener {
                if(it.exists()){
                    liked = true
                    holder.binding.likeBtn.setImageResource(R.drawable.like)
                }
                else{
                    liked = false
                    holder.binding.likeBtn.setImageResource(R.drawable.dislike)
                }
            }

        holder.binding.likeBtn.setOnClickListener {
            if(liked){
                var updatedLikes = totalLikes-1
                Firebase.firestore.collection(Firebase.auth.currentUser!!.uid+ LIKES)
                    .document(postList[position].postId!!).delete()

                val updates = hashMapOf<String, Any>(
                    "likes" to updatedLikes
                )

                Firebase.firestore.collection(POST).document(postList[position].postId!!).update(updates)
                    .addOnSuccessListener {
                        Firebase.firestore.collection(POST).document(postList[position].postId!!).get().addOnSuccessListener {
                            val post:Post = it.toObject<Post>()!!
                            holder.binding.likesTxt.text = post.likes.toString()
                            totalLikes = post.likes!!
                        }
                }
                liked = false
                holder.binding.likeBtn.setImageResource(R.drawable.dislike)
            }
            else{
                var updatedLikes = totalLikes+1

                val updates = hashMapOf<String, Any>(
                    "likes" to updatedLikes
                )

                Firebase.firestore.collection(POST).document(postList[position].postId!!).update(updates)
                    .addOnSuccessListener {
                        Firebase.firestore.collection(POST).document(postList[position].postId!!).get().addOnSuccessListener {
                            val post = it.toObject<Post>()!!
                            holder.binding.likesTxt.text = post.likes.toString()
                            totalLikes = post.likes!!
                        }
                }
                Firebase.firestore.collection(Firebase.auth.currentUser!!.uid+ LIKES)
                    .document(postList[position].postId!!).set(Like(true))
                liked = true
                holder.binding.likeBtn.setImageResource(R.drawable.like)
            }
        }


        val time = TimeAgo.using(postList[position].time!!.toLong())
        Glide.with(context).load(postList[position].postUrl).placeholder(R.drawable.loading).into(holder.binding.postImg)
        holder.binding.captionTxt.text = postList[position].caption


        holder.binding.likesTxt.text = postList[position].likes.toString()


        holder.binding.shareBtn.setOnClickListener {
            val i = Intent(Intent.ACTION_SEND)
            i.type = "text/plain"
            i.putExtra(Intent.EXTRA_TEXT,postList[position].postUrl)
            context.startActivity(i)
        }
    }
}