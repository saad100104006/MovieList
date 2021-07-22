package com.tanvir.xxnetwork.ui

import android.os.Bundle
import android.view.Menu
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.tanvir.xxnetwork.R
import com.tanvir.xxnetwork.databinding.ActivityBottomMainBinding
import dagger.hilt.android.AndroidEntryPoint
import me.ibrahimsn.lib.OnItemSelectedListener

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityBottomMainBinding
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = ActivityBottomMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = Navigation.findNavController(this@MainActivity, R.id.fragment)

        binding.smoothBar.onItemSelectedListener= object : OnItemSelectedListener {
            override fun onItemSelect(pos: Int): Boolean {
                when (pos) {
                    0 -> {
                        navController.navigate(R.id.home2)
                        return true
                    }
                    1 -> {
                        navController.navigate(R.id.favorite)
                        return true
                    }
                }
                return false
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.bottom_nav_menu, menu)
        val navController = Navigation.findNavController(this, R.id.fragment)
        binding.smoothBar.setupWithNavController(menu!!, navController)
        return true
    }
}