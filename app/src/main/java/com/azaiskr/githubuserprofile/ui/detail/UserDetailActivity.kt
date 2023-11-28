package com.azaiskr.githubuserprofile.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.azaiskr.githubuserprofile.R
import com.azaiskr.githubuserprofile.data.local.FavUserEntity
import com.azaiskr.githubuserprofile.data.remote.response.UserDetailResponse
import com.azaiskr.githubuserprofile.databinding.ActivityUserDetailBinding
import com.azaiskr.githubuserprofile.helper.ViewModelFactory
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UserDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var userDetailViewModel: UserDetailViewModel
    private lateinit var btnShare: FloatingActionButton
    private lateinit var btnFav: FloatingActionButton
    private lateinit var userDetailAdapter: UserDetailAdapter
    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        userDetailViewModel = getViewModel(this@UserDetailActivity)

        val username = intent.getStringExtra(USERNAME)
        username?.let {
            userDetailViewModel.getDetailUser(it)
        }

        userDetailAdapter = UserDetailAdapter(this@UserDetailActivity)
        userDetailAdapter.username = username.toString()

        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = userDetailAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        userDetailViewModel.apply {
            userData.observe(this@UserDetailActivity) { userDetailResponse ->
                setDetailUser(userDetailResponse)
            }
            detailLoading.observe(this@UserDetailActivity) {
                showLoading(it)
            }
            errorMessage.observe(this@UserDetailActivity) { errorMessage ->
                errorMessage?.let { showToast(this@UserDetailActivity, it) }
            }
            getFavUser(username.toString()).observe(this@UserDetailActivity) { favUserEntity ->
                btnFav = binding.btnFav
                if (favUserEntity != null) {
                    isFavorite = true
                    btnFav.setImageResource(R.drawable.favorite_fill1)
                    btnOnClick(isFavorite, favUserEntity)
                } else {
                    isFavorite = false
                    btnFav.setImageResource(R.drawable.heart_plus_fill0)
                    btnOnClick(isFavorite, null)
                }
            }
        }

        btnShare = binding.btnShare
        btnShare.setOnClickListener {
            try {
                val login = userDetailViewModel.userData.value?.login
                if (login != null) {
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "text/plain"
                    intent.putExtra(
                        Intent.EXTRA_TEXT,
                        "Hey! Check out this GitHub user profile!\nhttps://github.com/$login"
                    )
                    startActivity(Intent.createChooser(intent, "Share Using"))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        binding.btnFav.setOnClickListener {

        }
    }

    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setDetailUser(data: UserDetailResponse) {
        with(binding) {
            Glide.with(root.context).load(data.avatarUrl).into(ivUser)
            name.text = data.name
            userNameID.text =
                resources.getString(R.string.username_userID, data.login, data.id.toString())
            FollowingCount.text = data.following.toString()
            FollowersCount.text = data.followers.toString()
            repoCount.text = data.publicRepos.toString()
        }
    }

    private fun btnOnClick(isFav: Boolean, data: FavUserEntity?) {
        btnFav = binding.btnFav
        btnFav.setOnClickListener {
            if (isFav) {
                userDetailViewModel.deleteFavUser(data)
                btnFav.setImageResource(R.drawable.heart_plus_fill0)
                showToast(
                    this@UserDetailActivity,
                    getString(R.string.removed_from_favorite)
                )
            } else {
                val userDataResponse = userDetailViewModel.userData.value
                val favUser = FavUserEntity(
                    id = userDataResponse?.id,
                    username = userDataResponse?.login,
                    avatarUrl = userDataResponse?.avatarUrl,
                )
                userDetailViewModel.addFavUser(favUser)
                btnFav.setImageResource(R.drawable.favorite_fill1)
                showToast(this@UserDetailActivity, getString(R.string.added_to_favorite))
            }
        }
    }

    private fun getViewModel(activity: AppCompatActivity): UserDetailViewModel {
        val factory = ViewModelFactory.getInstanceViewModel(activity.application)
        return ViewModelProvider(activity, factory)[UserDetailViewModel::class.java]
    }

    companion object {
        const val USERNAME = "username"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.title_followers,
            R.string.title_following
        )
    }
}

