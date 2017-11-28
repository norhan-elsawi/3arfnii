package com.ibtikar.a3arfnii.applicationActivities.submitDetails.complaintDetails.mvp

import android.app.Activity
import android.content.ContentValues
import android.net.Uri
import android.util.Log
import com.google.firebase.database.*
import com.google.firebase.storage.StorageReference
import com.ibtikar.a3arfnii.base.implementation.IBasePresenterImpl
import com.ibtikar.a3arfnii.model.pojos.ComplaintPojo
import id.zelory.compressor.Compressor
import java.io.File
import java.util.*
import javax.inject.Inject


class ComplaintDetailsPresenter @Inject constructor(var view: ComplaintDetailsContract.View,
                                                    var model: ComplaintDetailsContract.Model) : IBasePresenterImpl(), ComplaintDetailsContract.Presenter {
    @Inject
    lateinit var database: FirebaseDatabase

    @Inject
    lateinit var mStorageRef: StorageReference

    private val randomId = UUID.randomUUID().toString()

    override fun getSpinnerValues(): ArrayList<String> {
        var types: ArrayList<String> = ArrayList()
        if (isNetworkConnected()) {
            var myRef: DatabaseReference = if (Locale.getDefault().language == "ar") {
                database.getReference("typeArabic")
            } else {
                database.getReference("typeEnglish")
            }

            myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    dataSnapshot.children.mapTo(types) { it.getValue(String::class.java)!! }
                    view.returnSpinnerValues(types)
                    Log.e(ContentValues.TAG, "finally")
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
                    view.failToGetSpinnerData()
                }
            })
        } else {
            view.failToGetSpinnerData()
        }
        return types
    }

    override fun uploadImages(images: ArrayList<File>) {

        view.setTextAndProgress("1/${images.size}", 0.toDouble())
        view.showProgressLayout()

        var imageUri: ArrayList<String> = ArrayList()
        var file: Uri? = null

        var x = 1

        for (i: Int in images.indices) {

            val riversRef = mStorageRef?.child("images/${images[i].name}")

            file = if (images[i].length() > 200) {
                var compressedImageFile = Compressor(view as Activity).compressToFile(images[i])
                Log.e("compress", "done")
                Uri.fromFile(compressedImageFile)
            } else {
                Uri.fromFile(images[i])
            }

            riversRef?.putFile(file)
                    ?.addOnSuccessListener({ taskSnapshot ->
                        view.setTextAndProgress("$x/${images.size}", ((100.0*x)/images.size ))
                        x++
                        val downloadUrl = taskSnapshot.downloadUrl
                        imageUri.add(downloadUrl!!.toString())
                        if (i == images.size - 1) {
                            view.returnImageUri(imageUri)
                        }
                        Log.e("success","success")

                    })
                    ?.addOnFailureListener({
                        view.failToUploadImages()
                    })
//                    ?.addOnProgressListener({ taskSnapshot ->
//                        view.setTextAndProgress("$x/${images.size}",
//                                (100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount)
//                        Log.e("progress","${(100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount}")
//                        Log.e("total byte","${taskSnapshot.totalByteCount}")
//                        Log.e("byte Transfered","${taskSnapshot.bytesTransferred}")
//
//                    })

        }


    }

    override fun getUserId(): String {
        return model.getUserId()
    }

    override fun uploadPojo(complaintPojo: ComplaintPojo) {
        if (isNetworkConnected()) {
            val mDatabase = FirebaseDatabase.getInstance().getReference("usersReports")
            mDatabase.child(getUserId()).child(randomId).setValue(complaintPojo)
                    .addOnCompleteListener({ task ->
                        view.pojoUploaded()
                    })
                    ?.addOnFailureListener({
                        view.showInternetError()
                    })

        } else {
            view.showInternetError()
        }
    }


}