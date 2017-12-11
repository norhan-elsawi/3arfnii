package com.ibtikar.a3arfnii.applicationActivities.submitDetails.chooseArea.mvp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.ibtikar.a3arfnii.R
import com.ibtikar.a3arfnii.applicationActivities.submitDetails.chooseArea.di.ChooseAreaModule
import com.ibtikar.a3arfnii.applicationActivities.submitDetails.chooseArea.di.DaggerChooseAreaComponent
import com.ibtikar.a3arfnii.applicationActivities.submitDetails.complaintDetails.mvp.ComplaintDetailsActivity
import com.ibtikar.a3arfnii.di.Injector
import com.ibtikar.a3arfnii.di.component.ApplicationComponent
import com.ibtikar.a3arfnii.events.NetworkStateChange
import com.ibtikar.a3arfnii.model.utils.Constants
import com.leo.simplearcloader.ArcConfiguration
import com.leo.simplearcloader.SimpleArcDialog
import kotlinx.android.synthetic.main.activity_choose_area.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject


class ChooseAreaActivity : AppCompatActivity(), ChooseAreaContract.View {


    @Inject
    lateinit var presenter: ChooseAreaContract.Presenter

    private var regions: ArrayList<String> = ArrayList()

    private var selectedRegion = ""

    var mDialog: SimpleArcDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_area)
        resolveDaggerDependency(Injector.INSTANCE.appComponent!!)
        initLoader()
        EventBus.getDefault().register(this);
//        val filter = IntentFilter()
//        filter.addAction(Constants.RECEIVE_NETWORK_STATE_ACTION)
//        registerReceiver(networkStateReceiver, filter)

        if (presenter.getUserId() == "") {
            requestAuth()
        } else {
            mDialog?.show()
            presenter.getSpinnerValues()
        }
        initSpinner()
        handleEvents()

    }

    private fun requestAuth() {

        if (presenter.getUserId() == "") {
            val isNetworkAvailableFlag = presenter?.isNetworkConnected()
            if (isNetworkAvailableFlag!!) {
                mDialog?.show()
                presenter?.requestAuth()!!
            } else {
                showInternetError()
            }
        }

    }

    override fun authResult(result: Boolean) {
        if (result) {
            presenter.getSpinnerValues()
        } else {
            mDialog?.hide()
        }
    }


    private fun showInternetError() {
        Toast.makeText(this, getString(R.string.open_internet_connection), Toast.LENGTH_SHORT).show()
    }

    private fun handleEvents() {
        complete.setOnClickListener { _ ->
            if (selectedRegion != "") {
                val intent = Intent(this, ComplaintDetailsActivity::class.java)
                intent.putExtra(Constants.SELECTED_REGION, selectedRegion)
                startActivity(intent)
            } else {
                Toast.makeText(this, getString(R.string.should_select_region), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun initSpinner() {
        spinner.setOnItemSelectedListener({ _, position, _, _ ->
            selectedRegion = regions[position]
        })

    }

    override fun returnSpinnerValues(mRegions: ArrayList<String>) {
        mDialog?.hide()
        regions = mRegions
        if (regions.isNotEmpty()) {
            spinner.setItems(regions)
        }
        if (regions.isNotEmpty() && selectedRegion == "") {
            selectedRegion = regions[0]
        }
    }

    override fun failToGetSpinnerData() {
        mDialog?.hide()
        Toast.makeText(this, getString(R.string.fail_to_get_spinner_data), Toast.LENGTH_LONG).show()
    }

    private fun resolveDaggerDependency(appComponent: ApplicationComponent) {
        DaggerChooseAreaComponent
                .builder()
                .applicationComponent(appComponent)
                .chooseAreaModule(ChooseAreaModule(this))
                .build()
                .inject(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        // unregisterReceiver(networkStateReceiver)
        EventBus.getDefault().unregister(this);
    }

    private fun initLoader() {
        mDialog = SimpleArcDialog(this)
        val arcCon = ArcConfiguration(this)
        arcCon.colors = intArrayOf(Color.parseColor("#008080"))
        mDialog?.setConfiguration(arcCon)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNetworkStateChange(networkStateChange: NetworkStateChange) {
        Log.d("choose area activity", "Network " + " connected")
        if (networkStateChange.isNetworkAvailable) {
            if (presenter.getUserId() == "") {
                requestAuth()
            } else {
                if (regions.isEmpty()) {
                    mDialog?.show()
                    presenter.getSpinnerValues()
                }
            }

        }
    }
}

//    private val networkStateReceiver = object : BroadcastReceiver() {
//        override fun onReceive(context: Context?, intent: Intent?) {
//
//
//    }


