package com.htetwill.portier.launcher

import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.htetwill.portier.launcher.databinding.ActivityFullscreenBinding
import com.htetwill.portier.launcher.state.ResultOf
import com.htetwill.portier.launcher.viewmodel.HomeViewModel

class HomeActivity : AppCompatActivity() {
    private val viewModel by lazy {
        ViewModelProvider(this, HomeViewModel.Factory())
            .get(HomeViewModel::class.java)
    }
    private val hideHandler = Handler()
    private lateinit var binding: ActivityFullscreenBinding
    private lateinit var fullscreenContent: TextView

    @SuppressLint("InlinedApi")
    private val hidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar
        fullscreenContent.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LOW_PROFILE or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }
    private var isFullscreen: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFullscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        isFullscreen = true

//         Set up the user interaction to manually show or hide the system UI.
        fullscreenContent = binding.fullscreenContent

        Toast.makeText(this, "param is "+ intent.getStringExtra("param"), Toast.LENGTH_SHORT).show()
        viewModel.fetchResponse("")
        viewModel.configLiveData().observe(
            this, Observer {
                result ->
                when (result) {
                    is ResultOf.Success -> {
                        Toast.makeText(this, "Fetch SUCCESS", Toast.LENGTH_SHORT).show()
                    }
                    is ResultOf.Failure -> {
                        Toast.makeText(this, "Fetch FAILURE", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )



    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        hideHandler.postDelayed(hidePart2Runnable, 3000.toLong())
    }

}