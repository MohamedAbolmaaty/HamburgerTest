package com.emapps.hamburgertest.presentation

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emapps.hamburgertest.R
import com.emapps.hamburgertest.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val menuViewModel: MenuViewModel by viewModels()
    private var lastNetworkState: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupMealsList()
        displayMealsState()
        setFilterBtn()
    }

    private fun setupMealsList() {
        binding.rvMenu.layoutManager = LinearLayoutManager(this)
        binding.rvMenu.adapter = MenuAdapter()
        val spacing = resources.getDimensionPixelSize(R.dimen.spacing_16dp)
        binding.rvMenu.addItemDecoration(MenuItemDecoration(spacing))
        binding.rvMenu.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 && binding.btnFilter.isShown) {
                    binding.btnFilter.hide()
                } else if (dy < 0 && !binding.btnFilter.isShown) {
                    binding.btnFilter.show()
                }
            }
        })
    }

    private fun displayMealsState() {
        lifecycleScope.launch {
            menuViewModel.state.collectLatest { state ->
                (binding.rvMenu.adapter as MenuAdapter).submitList(state.meals)

                if (lastNetworkState != state.isNetworkAvailable) {
                    if (state.isNetworkAvailable == true) {
                        Snackbar.make(binding.root, "Connection Restored", Snackbar.LENGTH_LONG)
                            .setAction("Retry") {
                                menuViewModel.retryFetch()
                            }
                            .show()
                    } else {
                        Snackbar.make(binding.root, "No internet connection", Snackbar.LENGTH_LONG)
                            .show()
                    }
                    lastNetworkState = state.isNetworkAvailable
                }
            }
        }
    }

    private fun setFilterBtn() {
        binding.btnFilter.setOnClickListener {
            val dialog = SortFilterDialogFragment.newInstance()
            dialog.onApply = { sortOptions ->
                Log.d("sortOptions", sortOptions.size.toString())
                menuViewModel.applySortOptions(sortOptions)
            }
            dialog.show(supportFragmentManager, "SortFilterDialog")
        }
    }
}