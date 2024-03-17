package com.example.swiftconnect.utils

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID
import kotlin.random.Random

fun uploadImage(uri: Uri, foldername: String, callback:(String?)->Unit){
    var imageUrl: String?= null
    FirebaseStorage.getInstance().getReference(foldername).child(UUID.randomUUID().toString())
        .putFile(uri)
        .addOnSuccessListener {it->
            it.storage.downloadUrl.addOnSuccessListener {
                imageUrl = it.toString()
                callback(imageUrl)
            }
        }
}

fun uploadVideo(uri: Uri, foldername: String, progressDialog: ProgressDialog, callback:(String?)->Unit){
    var imageUrl: String?= null
    progressDialog.setTitle("Uploading...")
    progressDialog.show()
    FirebaseStorage.getInstance().getReference(foldername).child(UUID.randomUUID().toString())
        .putFile(uri)
        .addOnSuccessListener {it->
            it.storage.downloadUrl.addOnSuccessListener {
                progressDialog.dismiss()
                imageUrl = it.toString()
                callback(imageUrl)
            }
        }
        .addOnProgressListener {
            val uploadValue:Long = it.bytesTransferred/it.totalByteCount
            progressDialog.setMessage("Uploaded $uploadValue %")
        }
}

fun generateRandomId(length: Int): String {
    val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    return (1..length)
        .map { Random.nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("")
}