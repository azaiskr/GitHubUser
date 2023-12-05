package com.azaiskr.githubuserprofile.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.azaiskr.githubuserprofile.R
import com.azaiskr.githubuserprofile.data.remote.response.ItemsItem
import com.azaiskr.githubuserprofile.databinding.ActivityMainBinding
import com.azaiskr.githubuserprofile.domain.SettingPreferences
import com.azaiskr.githubuserprofile.domain.dataStore
import com.azaiskr.githubuserprofile.helper.VmFactoryPref
import com.azaiskr.githubuserprofile.ui.detail.UserDetailActivity
import com.azaiskr.githubuserprofile.ui.favuser.UserFavActivity
import com.azaiskr.githubuserprofile.ui.setting.SettingActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val adapter = UserListAdapter(object : UserListAdapter.OnItemClickListener {
        override fun onItemClick(username: String) {
            val intent = Intent(this@MainActivity, UserDetailActivity::class.java)
            intent.putExtra(UserDetailActivity.USERNAME, username)
            startActivity(intent)
        }

    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Hide ActionBar
        supportActionBar?.hide()

        // Initialize ViewModel
        val preferences = SettingPreferences.getInstance(application.dataStore)
        val userListViewModel = initUserListViewModel(preferences)

        // Set up search functionality
        setUpSearchFunctionality(userListViewModel)

        // Set up RecyclerView
        setUpRecyclerView()

        // Observe ViewModel LiveData
        observeViewModel(userListViewModel)
    }

    private fun initUserListViewModel(preferences: SettingPreferences): UserListViewModel {
        return ViewModelProvider(this, VmFactoryPref(preferences))[UserListViewModel::class.java]
    }

    private fun setUpSearchFunctionality(userListViewModel: UserListViewModel) {
        with(binding) {
            searchBar.inflateMenu(R.menu.appmenu)
            searchBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.favUser -> {
                        startActivity(Intent(this@MainActivity, UserFavActivity::class.java))
                        false
                    }

                    R.id.setting -> {
                        startActivity(Intent(this@MainActivity, SettingActivity::class.java))
                        false
                    }

                    else -> false
                }
            }

            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { _, actionId, _ ->
                handleSearchAction(actionId, userListViewModel)
            }
        }
    }

    private fun handleSearchAction(actionId: Int, userListViewModel: UserListViewModel): Boolean {
        val searchText = binding.searchView.text.toString()
        return if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            if (searchText.isNotEmpty()) {
                binding.searchBar.textView.text = searchText
                userListViewModel.getUserList(searchText)
            } else {
                binding.searchBar.textView.text = searchText
                userListViewModel.getUserList()
            }
            binding.searchView.hide()
            true
        } else {
            false
        }
    }

    private fun setUpRecyclerView() {
        binding.rvUser.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
        }
    }

    private fun observeViewModel(userListViewModel: UserListViewModel) {
        userListViewModel.apply {
            listUser.observe(this@MainActivity) { itemsItem ->
                itemsItem?.let { setUserListData(it) }
            }
            isLoading.observe(this@MainActivity) { showLoading(it) }
            errorMessage.observe(this@MainActivity) { errorMessage ->
                errorMessage?.let { showToast(this@MainActivity, it) }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setUserListData(userData: List<ItemsItem?>) {
        adapter.submitList(userData)
        binding.rvUser.adapter = adapter
    }

    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }


}