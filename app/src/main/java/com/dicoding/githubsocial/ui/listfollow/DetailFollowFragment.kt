package com.dicoding.githubsocial.ui.listfollow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubsocial.data.model.ListUser
import com.dicoding.githubsocial.databinding.FragmentDetailFollowBinding
import com.dicoding.githubsocial.ui.ViewModelFactory
import com.dicoding.githubsocial.util.Status


class DetailFollowFragment : Fragment() {

    private lateinit var detailFollowViewModel: DetailFollowViewModel
    private lateinit var listFollowAdapter: ListFollowAdapter
    private lateinit var username: String
    private var index: Int = 0

    private lateinit var _bindingFollow: FragmentDetailFollowBinding
    private val bindingFollow get() = _bindingFollow

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bindingFollow = FragmentDetailFollowBinding.inflate(layoutInflater, container, false)
        return _bindingFollow.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initVM()

        username = arguments?.getString(EXTRA_USERNAME).toString()
        index = arguments?.getInt(EXTRA_INDEX)!!


        when (index) {
            0 -> detailFollowViewModel.getFollowersList(username)
                .observe(viewLifecycleOwner) { result ->
                    if (result != null) {
                        when (result.status) {
                            Status.LOADING -> showLoading()
                            Status.SUCCESS -> {
                                showFollowingList()
                                showUserItems(result.data!!)
                                hideLoading()
                            }
                            Status.ERROR -> {
                                Toast.makeText(
                                    context,
                                    "Terjadi kesalahan ${result.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                hideLoading()
                            }
                        }
                    }
                }
            1 -> detailFollowViewModel.getFollowingList(username)
                .observe(viewLifecycleOwner) { result ->
                    if (result != null) {
                        when (result.status) {
                            Status.LOADING -> showLoading()
                            Status.SUCCESS -> {
                                showFollowingList()
                                showUserItems(result.data!!)
                                hideLoading()
                            }
                            Status.ERROR -> {
                                Toast.makeText(
                                    context,
                                    "Terjadi kesalahan ${result.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                hideLoading()
                            }
                        }
                    }
                }
        }
    }

    private fun initVM() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val defineViewModel: DetailFollowViewModel by viewModels { factory }
        detailFollowViewModel = defineViewModel
    }

    private fun showUserItems(usernameItems: List<ListUser>) {
        listFollowAdapter.items = usernameItems
    }

    private fun showFollowingList() {
        bindingFollow.rvUserFollowers.layoutManager = LinearLayoutManager(context)
        listFollowAdapter = ListFollowAdapter()
        bindingFollow.rvUserFollowers.adapter = listFollowAdapter
        bindingFollow.rvUserFollowers.setHasFixedSize(true)
    }

    private fun showLoading() {
        bindingFollow.apply {
            rvUserFollowers.visibility = View.GONE
            pbUserFollower.visibility = View.VISIBLE
        }
    }

    private fun hideLoading() {
        bindingFollow.apply {
            rvUserFollowers.visibility = View.VISIBLE
            pbUserFollower.visibility = View.GONE
        }
    }

    companion object {
        var EXTRA_USERNAME = "username"
        var EXTRA_INDEX = "index_pager"
    }

}