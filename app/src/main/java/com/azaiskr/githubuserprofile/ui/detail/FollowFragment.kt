package com.azaiskr.githubuserprofile.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.azaiskr.githubuserprofile.data.remote.response.ItemsItem
import com.azaiskr.githubuserprofile.databinding.FragmentFollowBinding
import com.azaiskr.githubuserprofile.ui.main.UserListAdapter

class FollowFragment : Fragment() {

    private var position: Int = 0
    private var username: String? = ""
    private var binding: FragmentFollowBinding? = null

    private val viewModel by activityViewModels<UserDetailViewModel>()
    private val adapter by lazy {
        UserListAdapter(object : UserListAdapter.OnItemClickListener {
            override fun onItemClick(username: String) {
                val intent = Intent(requireContext(), UserDetailActivity::class.java)
                intent.putExtra(UserDetailActivity.USERNAME, username)
                startActivity(intent)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.rvFollow?.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(true)
            adapter = this@FollowFragment.adapter
        }

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }

        if (position == 1) {
            username?.let { viewModel.getFollowers(it) }
            viewModel.apply {
                userFollowers.observe(viewLifecycleOwner, this@FollowFragment::setLists)
                isLoading.observe(viewLifecycleOwner, this@FollowFragment::loadIndicator)
                errorMessage.observe(viewLifecycleOwner) { errorMessage ->
                    errorMessage?.let { showToast(requireContext(), it) }
                }
            }

        } else {
            username?.let { viewModel.getFollowing(it) }
            viewModel.apply {
                userFollowing.observe(viewLifecycleOwner, this@FollowFragment::setLists)
                isLoading.observe(viewLifecycleOwner, this@FollowFragment::loadIndicator)
                errorMessage.observe(viewLifecycleOwner) { errorMessage ->
                    errorMessage?.let { showToast(requireContext(), it) }
                }
            }

        }
    }

    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private fun setLists(data: List<ItemsItem?>) {
        adapter.submitList(data)
        binding?.rvFollow?.adapter = adapter
    }

    private fun loadIndicator(isLoading: Boolean) {
        binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"

        fun newInstance(position: Int, username: String) = FollowFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_POSITION, position)
                putString(ARG_USERNAME, username)
            }
        }
    }


}
