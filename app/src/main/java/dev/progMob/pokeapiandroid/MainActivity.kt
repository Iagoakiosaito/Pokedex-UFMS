package dev.progMob.pokeapiandroid

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import dagger.hilt.android.AndroidEntryPoint
import dev.progMob.pokeapiandroid.databinding.ActivityMainBinding


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
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
        setupSideNavigationMenu(navController)
        setupActionBar(navController)
        if (!checkPermission()) {
            requestPermission()
        }
        binding.mainToolbar.setNavigationOnClickListener {
            navController.navigateUp()
        }
        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId) {

                R.id.menu_profile -> {
                    Toast.makeText(applicationContext, "Home", Toast.LENGTH_SHORT).show()
                }
                R.id.menu_logout -> {
                    Toast.makeText(applicationContext, "Logout", Toast.LENGTH_SHORT).show()
                }
            }

            true
        }

    }

    private fun setupActionBar(navController: NavController) {
        NavigationUI.setupActionBarWithNavController(this, navController, binding.drawerLayout)
    }

    private fun setupSideNavigationMenu(navController: NavController) {
        binding.navView.let {
            NavigationUI.setupWithNavController(it, navController)
        }
    }

    private fun removeToolbarFromCertainFragments(navController: NavController) {
        navController.addOnDestinationChangedListener { _: NavController, navDestination: NavDestination, _: Bundle? ->
            if (navDestination.id == R.id.loginFragment || navDestination.id == R.id.registerFragment) {
                binding.mainToolbar.visibility = View.INVISIBLE
            } else {
                binding.mainToolbar.visibility = View.VISIBLE
            }
            if(navDestination.id == R.id.pokemonListFragment) {
                binding.mainToolbar.setBackgroundResource(R.color.green)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = Navigation.findNavController(this, R.id.nav_host)
        val navigated = NavigationUI.onNavDestinationSelected(item, navController)
        return navigated || super.onOptionsItemSelected(item)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.CAMERA),
            permissionRequestCode
        )
    }
}