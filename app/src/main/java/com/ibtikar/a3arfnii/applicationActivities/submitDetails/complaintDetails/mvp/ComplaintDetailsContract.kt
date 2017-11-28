package com.ibtikar.a3arfnii.applicationActivities.submitDetails.complaintDetails.mvp

import android.net.Uri
import com.ibtikar.a3arfnii.base.interfaces.IBasePresenter
import com.ibtikar.a3arfnii.model.pojos.ComplaintPojo
import java.io.File


interface ComplaintDetailsContract {

    interface View {
        fun failToGetSpinnerData()
        fun returnSpinnerValues(types:ArrayList<String>)
        fun failToUploadImages()
        fun returnImageUri(imageUri:ArrayList<String>)
        fun showInternetError()
        fun pojoUploaded()
        fun showProgressLayout();
        fun setTextAndProgress(text:String,progress:Double)

    }

    interface Model {
        fun getUserId(): String
    }

    interface Presenter : IBasePresenter {
        fun getSpinnerValues(): ArrayList<String>
        fun uploadImages(images:ArrayList<File>)
        fun getUserId():String
        fun uploadPojo(complaintPojo: ComplaintPojo)
    }

}