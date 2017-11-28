package com.ibtikar.a3arfnii.applicationActivities.submitDetails.chooseArea.mvp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
import com.ibtikar.a3arfnii.model.utils.Constants
import kotlinx.android.synthetic.main.activity_choose_area.*
import javax.inject.Inject


class ChooseAreaActivity : AppCompatActivity(), ChooseAreaContract.View {


    @Inject
    lateinit var presenter: ChooseAreaContract.Presenter

    private var regions: ArrayList<String> = ArrayList()

    private var selectedRegion = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_area)
        resolveDaggerDependency(Injector.INSTANCE.appComponent)

        val filter = IntentFilter()
        filter.addAction(Constants.RECEIVE_NETWORK_STATE_ACTION)
        registerReceiver(networkStateReceiver, filter)

            presenter.getSpinnerValues()

        initSpinner()
        handleEvents()

    }

    private fun handleEvents() {
        complete.setOnClickListener { _ ->
            if (selectedRegion != "") {
                val intent = Intent(this, ComplaintDetailsActivity::class.java)
                intent.putExtra(Constants.SELECTED_REGION, selectedRegion)
                startActivity(intent)
                finish()
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
        regions = mRegions
        if(regions.isNotEmpty()) {
            spinner.setItems(regions)
        }
        if (regions.isNotEmpty()&&selectedRegion=="") {
            selectedRegion = regions[0]
        }
    }

    override fun failToGetSpinnerData() {
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
        unregisterReceiver(networkStateReceiver)
    }

    private val networkStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("choose area activity", "Network " + " connected")
            if (regions.isEmpty()) {
                presenter.getSpinnerValues()
            }

        }

    }

}
