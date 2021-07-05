package com.htetwill.portier.launcher.activity

import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.htetwill.portier.launcher.R
import com.htetwill.portier.launcher.adapter.CustomAdapter
import com.htetwill.portier.launcher.databinding.ActivityAppBinding
import com.htetwill.portier.launcher.logger.Log
import com.htetwill.portier.launcher.logger.LogWrapper
import com.htetwill.portier.launcher.viewmodel.AppViewModel


class AppActivity : AppCompatActivity(), SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener{

    private lateinit var currentLayoutManagerType: LayoutManagerType
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var menuSearch: MenuItem
    private lateinit var searchView: SearchView
    enum class LayoutManagerType { LINEAR_LAYOUT_MANAGER,GRID_LAYOUT_MANAGER }
    private val viewModel by lazy {
        ViewModelProvider(this, AppViewModel.Factory())
            .get(AppViewModel::class.java)
    }
    private lateinit var binding: ActivityAppBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))

        binding.toolbarLayout.title = getString(R.string.app_name)
        binding.fab.setOnClickListener { finish() }
        viewModel.loadInstalledPackage(packageManager)
        intent.getStringArrayListExtra("SPONSORED_APP_LIST")?.let { viewModel.filterApps(it) }
        viewModel.listLiveData().observe(
            this, {
                list ->
                binding.appListRecyclerview.adapter = CustomAdapter(list)
            }
        )
        setRecyclerViewLayout(LayoutManagerType.GRID_LAYOUT_MANAGER)

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            currentLayoutManagerType = savedInstanceState
                .getSerializable("KEY_LAYOUT_MANAGER") as LayoutManagerType
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        outState.putSerializable("KEY_LAYOUT_MANAGER", currentLayoutManagerType)
        super.onSaveInstanceState(outState, outPersistentState)

    }

    private fun setRecyclerViewLayout(layoutManagerType: LayoutManagerType) {
        when (layoutManagerType) {
            LayoutManagerType.GRID_LAYOUT_MANAGER ->{
                layoutManager = GridLayoutManager(applicationContext, 2)
                currentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER
            }
            LayoutManagerType.LINEAR_LAYOUT_MANAGER->{
                layoutManager = LinearLayoutManager(applicationContext)
                currentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER
            }
        }
        with(binding.appListRecyclerview) {
            layoutManager = this@AppActivity.layoutManager
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_app, menu)
        if (menu != null) {
            menuSearch = menu.findItem(R.id.search)
            menuSearch.setOnActionExpandListener(this)

            searchView = menu.findItem(R.id.search).actionView as SearchView

            (menu.findItem(R.id.search).actionView as SearchView).apply {
                setOnQueryTextListener(this@AppActivity)
            }
        }

        return true
    }


    override fun onQueryTextSubmit(query: String?): Boolean {
        viewModel.findAppName(query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        viewModel.loadSuggestion(newText)
        return false
    }

    override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
        viewModel.loadSuggestion("")
        setRecyclerViewLayout(LayoutManagerType.LINEAR_LAYOUT_MANAGER)
        return true
    }

    override fun onStart() {
        super.onStart()
        /** Set up targets to receive log data  */
        val logWrapper = LogWrapper()
        Log.logNode = logWrapper
    }

    override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
        intent.getStringArrayListExtra("SPONSORED_APP_LIST")?.let { viewModel.filterApps(it) }
        setRecyclerViewLayout(LayoutManagerType.GRID_LAYOUT_MANAGER)
        return true
    }

    companion object {
        val TAG = "AppActivity"
    }

}