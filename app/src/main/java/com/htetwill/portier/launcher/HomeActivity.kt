package com.htetwill.portier.launcher

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar
import com.htetwill.portier.launcher.activity.AppActivity
import com.htetwill.portier.launcher.databinding.ActivityHomeBinding
import com.htetwill.portier.launcher.model.Config
import com.htetwill.portier.launcher.state.ResultOf
import com.htetwill.portier.launcher.viewmodel.HomeViewModel

class HomeActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {
    private lateinit var btnProceed: Button
    private lateinit var indicator: LinearProgressIndicator
    private lateinit var binding: ActivityHomeBinding
    private lateinit var tvGreeting: TextView
    private val viewModel by lazy {
        ViewModelProvider(this, HomeViewModel.Factory())
            .get(HomeViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tvGreeting = binding.tvGreeting
        indicator = binding.busyIndicator
        btnProceed = binding.btnProceed
        btnProceed.setOnClickListener { showPermissionPreview() }
        intent.getStringExtra("param")?.let { viewModel.fetchResponse(it) }
        viewModel.isLoadingLiveData().observe(this, { setLoadingState(it) })
        viewModel.configLiveData().observe(this, { result -> setWelcomeUI(result) })


    }

    private fun showPermissionPreview() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            val i = Intent(this, AppActivity::class.java)
            when (val result = viewModel.configLiveData().value) {
                is ResultOf.Success -> {
                    if (result.value.apps.isNullOrEmpty())
                        i.putStringArrayListExtra("SPONSORED_APP_LIST", ArrayList())
                    else
                        i.putStringArrayListExtra(
                            "SPONSORED_APP_LIST",
                            ArrayList(result.value.apps)
                        )
                }
                is ResultOf.Failure -> {
                    i.putStringArrayListExtra("SPONSORED_APP_LIST", ArrayList())
                }
            }
            startActivity(i)
        } else {
            // Permission is missing and must be requested.
            requestPermission()
        }
    }

    private fun setWelcomeUI(result: ResultOf<Config>?) {
        when (result) {
            is ResultOf.Success -> {
                binding.tvGreeting.text = getString(
                    R.string.dummy_content,
                    result.value.hotel!!.name, result.value.hotel!!.city
                )
                binding.tvGreeting.setTextColor(Color.parseColor(result.value.color))
                binding.layoutCoordinator.setBackgroundColor(Color.parseColor(result.value.background))
            }
            is ResultOf.Failure -> {
                result.message?.let { msg -> getSnackbar(msg).show() }
            }
        }
    }

    private fun setLoadingState(isLoading: Boolean) {
        when (isLoading) {
            true -> {
                indicator.show()
                tvGreeting.visibility = View.INVISIBLE
                btnProceed.visibility = View.INVISIBLE
            }
            false -> {
                indicator.hide()
                tvGreeting.visibility = View.VISIBLE
                btnProceed.visibility = View.VISIBLE
            }
        }
    }

    private fun getSnackbar(msg: String): Snackbar {
        val snackbar = Snackbar.make(binding.layoutCoordinator, msg, LENGTH_SHORT)
        snackbar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.snackbar_primary))
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.snackbar_text))
        snackbar.setTextColor(ContextCompat.getColor(this, R.color.snackbar_text))
        return snackbar
    }

    private fun requestPermission() {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                0
            )
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                0
            )

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 0) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivity(Intent(this, AppActivity::class.java))
            } else {
                getSnackbar("Permission Denied !")
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}