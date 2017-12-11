package com.ibtikar.a3arfnii.applicationActivities.submitDetails.complaintDetails.mvp

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.android.gms.maps.model.LatLng
import com.ibtikar.a3arfnii.R
import com.ibtikar.a3arfnii.applicationActivities.submitDetails.complaintDetails.di.ComplaintDetailsModule
import com.ibtikar.a3arfnii.applicationActivities.submitDetails.complaintDetails.di.DaggerComplaintDetailsComponent
import com.ibtikar.a3arfnii.applicationActivities.submitDetails.complaintDetails.mvp.adapter.CallBack
import com.ibtikar.a3arfnii.applicationActivities.submitDetails.complaintDetails.mvp.adapter.ImageAdapter
import com.ibtikar.a3arfnii.di.Injector
import com.ibtikar.a3arfnii.di.component.ApplicationComponent
import com.ibtikar.a3arfnii.model.pojos.ComplaintPojo
import com.ibtikar.a3arfnii.model.utils.Constants
import com.leo.simplearcloader.ArcConfiguration
import com.leo.simplearcloader.SimpleArcDialog
import kotlinx.android.synthetic.main.activity_complaint_details.*
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File
import javax.inject.Inject


class ComplaintDetailsActivity : AppCompatActivity(), ComplaintDetailsContract.View, CallBack {


    @Inject
    lateinit var presenter: ComplaintDetailsContract.Presenter

    var imageAdapter: ImageAdapter? = null
    var imagesUri: ArrayList<File>? = null
    var latLong: LatLng? = null


    private val PHOTOS_KEY = "easy_image_photos_list"
    private val LAT_KEY = "lat"
    private val LONG_KEY = "Long"
    private val LAT_LONG_CHECK = "latLongCheck"
    private val TYPE_KEY = "type"

    private var types: ArrayList<String> = ArrayList()

    var mDialog: SimpleArcDialog? = null

    private var selectedType = ""

    private var selectedRegion = ""

    val PLACE_PICKER_REQUEST = 1

    val PERMISSION_REQUEST_CODE = 3


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complaint_details)
        resolveDaggerDependency(Injector.INSTANCE.appComponent!!)
        initLoader()

        val filter = IntentFilter()
        filter.addAction(Constants.RECEIVE_NETWORK_STATE_ACTION)
        registerReceiver(networkStateReceiver, filter)

        if (savedInstanceState != null) {
            imagesUri = savedInstanceState.getSerializable(PHOTOS_KEY) as ArrayList<File>
            selectedType = savedInstanceState.getString(TYPE_KEY)
            selectedRegion = savedInstanceState.getString(Constants.SELECTED_REGION)

            if (savedInstanceState.getBoolean(LAT_LONG_CHECK)) {
                latLong = LatLng(savedInstanceState.getString(LAT_KEY).toDouble(), savedInstanceState.getString(LONG_KEY).toDouble())
            }
        } else {
            imagesUri = ArrayList()
            selectedRegion = intent.getStringExtra(Constants.SELECTED_REGION)
        }
        presenter.getSpinnerValues()

        initSpinner()
        initRecyclerView()
        handleEvents()
    }

    private fun initSpinner() {
        spinnerType.setOnItemSelectedListener({ _, position, _, _ ->
            selectedType = types[position]
        })
    }

    override fun returnSpinnerValues(mTypes: ArrayList<String>) {
        types = mTypes
        if (types.isNotEmpty()) {
            spinnerType.setItems(types)
        }
        if (types.isNotEmpty() && selectedType == "") {
            selectedType = types[0]
        }
    }


    private fun initRecyclerView() {
        imageAdapter = ImageAdapter(imagesUri as List<File>, this)
        val mLayoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        imageContainer.layoutManager = mLayoutManager
        imageContainer.adapter = imageAdapter
    }

    private fun handleEvents() {
        location.setOnClickListener { _ ->
            val builder = PlacePicker.IntentBuilder()
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST)
        }
        takePhoto.setOnClickListener { _ ->
            if (imagesUri?.size!! < 4) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_DENIED) {
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), PERMISSION_REQUEST_CODE);

                    } else {
                        EasyImage.openChooserWithGallery(this, getString(R.string.select_image), 0)
                    }
                } else {
                    EasyImage.openChooserWithGallery(this, getString(R.string.select_image), 0)
                }
            }else{
                Toast.makeText(this, getString(R.string.only_four_image_allowed), Toast.LENGTH_SHORT).show()
            }
        }

        send.setOnClickListener { _ ->
            if (presenter.isNetworkConnected()) {
                if (name.text.toString() != "" && email.text.toString() != "" && description.text.toString() != "" && selectedType != "" && latLong != null) {
                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
                        if (imagesUri!!.isNotEmpty()) {
                            mDialog?.show()
                            presenter.uploadImages(imagesUri!!)
                        } else {
                            returnImageUri(ArrayList())
                        }
                    } else {
                        Toast.makeText(this, getString(R.string.invalid_email), Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, getString(R.string.empty_fields), Toast.LENGTH_SHORT).show()
                }
            } else {
                showInternetError()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val place = PlacePicker.getPlace(this, data)
                val toastMsg = String.format("Place: %s", place.address)
                Toast.makeText(this, toastMsg, Toast.LENGTH_SHORT).show()
                address.text = toastMsg
                latLong = place.latLng
            }
        } else {
            if (resultCode == Activity.RESULT_OK) {
                EasyImage.handleActivityResult(requestCode, resultCode, data, this, object : DefaultCallback() {
                    override fun onImagePicked(imageFile: File?, source: EasyImage.ImageSource?, type: Int) {
                        if (imageFile != null) {
                            imagesUri?.add(imageFile)
                            imageAdapter?.notifyDataSetChanged()
                        }
                    }

                    override fun onImagePickerError(e: Exception?, source: EasyImage.ImageSource?, type: Int) {

                    }

                    override fun onCanceled(source: EasyImage.ImageSource, type: Int) {
                        if (source == EasyImage.ImageSource.CAMERA) {
                            val photoFile = EasyImage.lastlyTakenButCanceledPhoto(this@ComplaintDetailsActivity)
                            photoFile?.delete()
                        }
                    }

                })
            }
        }
    }

    override fun failToGetSpinnerData() {
        Toast.makeText(this, getString(R.string.fail_to_get_spinner_data_types), Toast.LENGTH_LONG).show()
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    EasyImage.openChooserWithGallery(this, getString(R.string.select_image), 0)
                } else {
                    Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }


    override fun deleteImage(position: Int) {
        imagesUri?.removeAt(position)
        imageAdapter?.notifyDataSetChanged()
    }

    override fun failToUploadImages() {
        mDialog?.hide()
        progressLayOut.visibility = View.INVISIBLE
        showInternetError()
    }

    override fun returnImageUri(mImageUri: ArrayList<String>) {
        mDialog?.hide()
        progressLayOut.visibility = View.INVISIBLE
        var complaintPojo = ComplaintPojo(name.text.toString(), email.text.toString(),
                description.text.toString(), address.text.toString(), latLong!!.latitude.toString(),
                latLong!!.longitude.toString(), selectedType, selectedRegion, mImageUri!!)
        presenter.uploadPojo(complaintPojo)

    }

    override fun pojoUploaded() {
        onBackPressed()
    }


    public override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState!!.putSerializable(PHOTOS_KEY, imagesUri)
        outState!!.putString(Constants.SELECTED_REGION, selectedRegion)
        outState!!.putString(TYPE_KEY, selectedType)

        if (latLong != null) {
            outState!!.putBoolean(LAT_LONG_CHECK, true)
            outState!!.putString(LAT_KEY, latLong?.latitude.toString())
            outState!!.putString(LONG_KEY, latLong?.longitude.toString())

        } else {
            outState!!.putBoolean(LAT_LONG_CHECK, false)
        }
    }

    private fun resolveDaggerDependency(appComponent: ApplicationComponent) {
        DaggerComplaintDetailsComponent
                .builder()
                .applicationComponent(appComponent)
                .complaintDetailsModule(ComplaintDetailsModule(this))
                .build()
                .inject(this)
    }

    override fun showInternetError() {
        Toast.makeText(this, getString(R.string.open_internet_connection), Toast.LENGTH_SHORT).show()
    }

    private fun initLoader() {
        mDialog = SimpleArcDialog(this)
        val arcCon = ArcConfiguration(this)
        arcCon.colors = intArrayOf(Color.parseColor("#008080"))
        mDialog?.setConfiguration(arcCon)
        mDialog?.setCancelable(false)
    }

    override fun showProgressLayout() {
        progressLayOut.visibility = View.VISIBLE
    }

    override fun setTextAndProgress(text: String, progress: Double) {
        progressText.text = text
        progressBar.progress = progress.toInt()

    }


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkStateReceiver)
    }


    private val networkStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("complaint activity", "Network " + " connected")
            if (types.isEmpty()) {
                presenter.getSpinnerValues()
            }
        }

    }

}
