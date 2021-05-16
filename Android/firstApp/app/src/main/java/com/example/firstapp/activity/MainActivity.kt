package com.example.firstapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.firstapp.R
import com.example.firstapp.data.ContentModel
import com.example.firstapp.databinding.ActivityMainBinding
import com.example.firstapp.network.ContentService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val TAG = this::class.java.simpleName

    lateinit var binding: ActivityMainBinding
    private lateinit var contentService : ContentService
    private var contentList: List<ContentModel> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        @Suppress("UNUSED_VARIABLE")
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setupNavigation()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    override fun onSupportNavigateUp() = navigateUp(findNavController(R.id.myNavHostFragment), binding.drawerLayout)

    private fun setupNavigation() {
        val navController = findNavController(R.id.myNavHostFragment)

        setSupportActionBar(binding.toolbar)

        setupActionBarWithNavController(navController, binding.drawerLayout)

        binding.navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination: NavDestination, _ ->
            val toolBar = supportActionBar ?: return@addOnDestinationChangedListener
            when(destination.id) {
                R.id.contentListFragment -> {
                    toolBar.setDisplayShowTitleEnabled(false)
                }
                else -> {
                    toolBar.setDisplayShowTitleEnabled(true)
                }
            }
        }
    }

    private fun getContents(){
        contentService.getContentList(
            {
                contentList = it

                getDetails()
            },
            { _, t ->
                Toast.makeText(this, "error: " + t.message, Toast.LENGTH_SHORT).show()
                Log.d(TAG, "error: " + t.message)
            })
    }

    private fun getDetails(){
        CoroutineScope(Dispatchers.IO).launch {
            contentList = contentService.getDetails(contentList)
            Log.d(TAG, contentList.toString())
        }

        setContents()
    }

    private fun setContents(){
//        //RecyclerView
//        val contentListAdapter = ContentListAdapter(this, contentList){ content ->
//            val intent = Intent(this, DetailActivity::class.java)
//            intent.putExtra("contentLink", content.contentLink)
//            startActivity(intent)
//        }
//        binding.contentListRecyclerView.adapter = contentListAdapter
    }
}