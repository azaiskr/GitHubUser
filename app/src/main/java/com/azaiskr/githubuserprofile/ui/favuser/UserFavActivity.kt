package com.azaiskr.githubuserprofile.ui.favuser

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.azaiskr.githubuserprofile.R
import com.azaiskr.githubuserprofile.data.remote.response.ItemsItem
import com.azaiskr.githubuserprofile.databinding.ActivityFavUserBinding
import com.azaiskr.githubuserprofile.helper.ViewModelFactory
import com.azaiskr.githubuserprofile.ui.detail.UserDetailActivity
import com.azaiskr.githubuserprofile.ui.main.UserListAdapter

class UserFavActivity : AppCompatActivity() {

    private lateinit var userFavViewModel: UserFavViewModel
    private lateinit var binding: ActivityFavUserBinding
    private val adapter by lazy {
        UserListAdapter(object : UserListAdapter.OnItemClickListener {
            override fun onItemClick(username: String) {
                val intent = Intent(this@UserFavActivity, UserDetailActivity::class.java)
                intent.putExtra(UserDetailActivity.USERNAME, username)
                startActivity(intent)
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userFavViewModel = getViewModel(this@UserFavActivity)

        binding = ActivityFavUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val section = getString(R.string.favorite_user)
        supportActionBar?.title = section

        binding.rvFav.apply {
            layoutManager = LinearLayoutManager(this@UserFavActivity)
            setHasFixedSize(true)
            adapter = this@UserFavActivity.adapter
        }

        userFavViewModel.apply {
            getFavUser().observe(this@UserFavActivity) { favUserEntity ->
                val userItem = arrayListOf<ItemsItem>()
                favUserEntity.map {
                    val item = ItemsItem(id = it.id, login = it.username, avatarUrl = it.avatarUrl)
                    userItem.add(item)
                }
                adapter.submitList(userItem)
            }
            isLoading.observe(this@UserFavActivity) {
                if (it) {
                    binding.progressBar.visibility = View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }

    }

    private fun getViewModel(activity: AppCompatActivity): UserFavViewModel {
        val factory = ViewModelFactory.getInstanceViewModel(activity.application)
        return ViewModelProvider(activity, factory)[UserFavViewModel::class.java]
    }
}