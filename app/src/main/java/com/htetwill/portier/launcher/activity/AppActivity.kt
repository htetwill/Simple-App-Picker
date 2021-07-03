package com.htetwill.portier.launcher.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.htetwill.portier.launcher.R
import com.htetwill.portier.launcher.adapter.CustomAdapter
import com.htetwill.portier.launcher.databinding.ActivityAppBinding
import com.htetwill.portier.launcher.viewmodel.AppViewModel


class AppActivity : AppCompatActivity(), SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener{
    private lateinit var currentLayoutManagerType: LayoutManagerType
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var menuSearch: MenuItem
    private lateinit var searchView: SearchView
    enum class LayoutManagerType { LINEAR_LAYOUT_MANAGER }
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
        handleIntent(intent)

        binding.toolbarLayout.title = "title"
        binding.fab.setOnClickListener { view ->
            //Collapse the action view
                searchView.onActionViewCollapsed()
            //Collapse the search widget
                menuSearch.collapseActionView()

            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        binding.appListRecyclerview.adapter = CustomAdapter(viewModel.getApps(this.packageManager))
        setRecyclerViewLayout()

        binding.fab.setOnClickListener { finish() }

    }

    private fun setRecyclerViewLayout() {
            layoutManager = LinearLayoutManager(this)
            currentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER
            with(binding.appListRecyclerview) {
                layoutManager = this@AppActivity.layoutManager
            }
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let { handleIntent(it) }
    }

    private fun handleIntent(intent: Intent) {

        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            //use the query to search your data somehow

            Toast.makeText(this, "clicked Search", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_app, menu)
        // Associate searchable configuration with the SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        if (menu != null) {
            menuSearch = menu.findItem(R.id.search)
            menuSearch.setOnActionExpandListener(this)

            searchView = menu.findItem(R.id.search).actionView as SearchView

//            val closeButton = searchView.findViewById<View>(androidx.appcompat.R.id.search_close_btn)
//            closeButton.setOnClickListener{
//                val et = findViewById<View>(R.id.search_src_text) as EditText
                //Clear the text from EditText view
//                et.setText("")
                //Clear query
//                searchView.setQuery("", false)
                //Collapse the action view
//                searchView.onActionViewCollapsed()
                //Collapse the search widget
//                menuSearch.collapseActionView()
//            }

            (menu.findItem(R.id.search).actionView as SearchView).apply {
                setSearchableInfo(searchManager.getSearchableInfo(componentName))
                setOnQueryTextListener(this@AppActivity)
            }
        }

        return true
    }
    override fun onQueryTextSubmit(query: String?): Boolean {
        Toast.makeText(this, "onQueryTextSubmit "+query!!, Toast.LENGTH_SHORT).show()
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        Toast.makeText(this, "onQueryTextChange "+newText!!, Toast.LENGTH_SHORT).show()
        return false
    }

    override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
        Toast.makeText(this, "Expand", Toast.LENGTH_SHORT).show()
    return true
    }

    override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
        Toast.makeText(this, "Collapse", Toast.LENGTH_SHORT).show()
        return true
    }
}