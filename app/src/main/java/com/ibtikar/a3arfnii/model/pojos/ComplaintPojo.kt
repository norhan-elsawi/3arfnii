package com.ibtikar.a3arfnii.model.pojos

import android.net.Uri
import com.google.android.gms.maps.model.LatLng

/**
 * Created by norhan.elsawi on 11/27/2017.
 */
data class ComplaintPojo(var name:String, var email:String, var description:String, var address:String,
                         var long:String,var lat:String, var type:String, var selectedRegion:String,var imagesUri:ArrayList<String>){
}