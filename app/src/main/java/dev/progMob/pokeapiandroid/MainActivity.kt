package dev.progMob.pokeapiandroid

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import dagger.hilt.android.AndroidEntryPoint
import dev.progMob.pokeapiandroid.databinding.ActivityMainBinding
import dev.progMob.pokeapiandroid.model.UserGlobal


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val permissionRequestCode = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.mainToolbar)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val navController = navHostFragment.navController
        removeToolbarFromCertainFragments(navController)
        setupActionBar(navController)
        profileClickListener()
        requestPermission()

        binding.mainToolbar.setNavigationOnClickListener {
            navController.navigateUp()
        }

    }

    private fun profileClickListener() {
        binding.btnProfile.setOnClickListener{
            val navController = Navigation.findNavController(this, R.id.nav_host)
            navController.navigate(R.id.profileFragment)
        }
    }

    private fun removeToolbarFromCertainFragments(navController: NavController) {
        navController.addOnDestinationChangedListener { _: NavController, navDestination: NavDestination, _: Bundle? ->
            when (navDestination.id) {
                R.id.loginFragment -> {
                    binding.mainToolbar.visibility = View.INVISIBLE
                    binding.mainToolbar.setBackgroundResource(R.color.green)
                }
                R.id.registerFragment -> {
                    binding.btnProfile.visibility = View.INVISIBLE
                    binding.mainToolbar.visibility = View.VISIBLE
                }
                else -> {
                    binding.mainToolbar.visibility = View.VISIBLE
                }
            }
            if(navDestination.id == R.id.pokemonListFragment) {
                binding.btnProfile.visibility = View.VISIBLE
                binding.mainToolbar.setBackgroundResource(R.color.green)
                val bmp = BitmapFactory.decodeByteArray(UserGlobal._photo, 0, UserGlobal._photo.size)
                binding.btnProfile.setImageBitmap(bmp)
            } else if (navDestination.id == R.id.profileFragment){
                binding.btnProfile.visibility = View.INVISIBLE
                binding.mainToolbar.setBackgroundResource(R.color.green)
                binding.mainToolbar.title = ""
            }
        }
    }


    private fun setupActionBar(navController: NavController) {
        NavigationUI.setupActionBarWithNavController(this, navController, binding.drawerLayout)
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }


    private fun requestPermission() {
        if(!checkPermission()){
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CAMERA),
                permissionRequestCode
            )
        }
    }
}