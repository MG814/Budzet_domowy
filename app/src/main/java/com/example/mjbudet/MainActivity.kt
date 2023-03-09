package com.example.mjbudet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.mjbudet.databinding.ActivityMainBinding
import com.example.mjbudet.fragments.NewTrFragment
import com.example.mjbudet.fragments.charts.SummaryChartFragment


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.fragmentContainerView.id) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val myFrag = supportFragmentManager.findFragmentByTag("tag")
        if(item.itemId == R.id.add){
            if(myFrag == null)
                repleceFragment(NewTrFragment())
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        else if(item.itemId == R.id.chart){
            if(myFrag == null)
                repleceFragment(SummaryChartFragment())
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        else if(item.itemId == android.R.id.home){
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    fun setBottomNavVisibility(bool: Boolean) {
        binding.bottomNavigationView.isVisible = bool
    }

    private fun repleceFragment(fragment: Fragment){
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            addToBackStack("INIT_COMMIT")
            replace(R.id.fragmentContainerView, fragment, "tag")
        }
    }
}


