package com.example.githubviewer.repository.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import com.example.githubviewer.R
import com.example.githubviewer.databinding.MainActivityBinding
import com.example.githubviewer.domain.model.UserAuthStatus
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        splashScreen.setKeepOnScreenCondition {
            val userAuthStatus = viewModel.userAuthStatus.value
            if (userAuthStatus == null) {
                true
            } else {
                setGraphStartDestination(userAuthStatus)
                false
            }
        }
    }

    private fun setGraphStartDestination(userAuthStatus: UserAuthStatus) {
        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment
        ) as NavHostFragment

        val graph = navHostFragment.navController.navInflater.inflate(R.navigation.navigation_graph)
        when (userAuthStatus) {
            is UserAuthStatus.Authorized -> graph.setStartDestination(R.id.repositoriesListFragment)
            is UserAuthStatus.NotAuthorized -> graph.setStartDestination(R.id.authFragment)
        }
        navHostFragment.navController.graph = graph
    }
}