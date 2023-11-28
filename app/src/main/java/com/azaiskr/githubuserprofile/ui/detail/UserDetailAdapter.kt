package com.azaiskr.githubuserprofile.ui.detail

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class UserDetailAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    var username: String = ""

    override fun createFragment(position: Int): Fragment {
        return FollowFragment.newInstance(position + 1, username)
    }

    override fun getItemCount(): Int {
        return 2
    }
}
