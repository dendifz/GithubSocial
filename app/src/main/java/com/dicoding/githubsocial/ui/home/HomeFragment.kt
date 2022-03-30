package com.dicoding.githubsocial.ui.home

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubsocial.R
import com.dicoding.githubsocial.data.model.User
import com.dicoding.githubsocial.databinding.FragmentHomeBinding
import com.dicoding.githubsocial.ui.ViewModelFactory
import com.dicoding.githubsocial.ui.detailuser.DetailUserFragment.Companion.LIST_USER_LIST
import com.dicoding.githubsocial.ui.main.MainActivity
import com.dicoding.githubsocial.util.Status

class HomeFragment : Fragment() {

    private lateinit var searchUserAdapter: SearchUserAdapter
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var _binding: FragmentHomeBinding

    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVM()
        (activity as MainActivity?)?.supportActionBar?.title =
            resources.getString(R.string.app_name)
        homeViewModel.checkUser().observe(viewLifecycleOwner) {
            homeViewModel.getListUser(it).observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result.status) {
                        Status.LOADING -> showLoading()
                        Status.SUCCESS -> {
                            showSearchList()
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)

        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        val settingView = menu.findItem(R.id.setting)
        settingView.setOnMenuItemClickListener {
            NavHostFragment
                .findNavController(this)
                .navigate(R.id.action_homeFragment_to_settingFragment)
            return@setOnMenuItemClickListener true
        }

        val favoriteView = menu.findItem(R.id.favorite_menu)
        favoriteView.setOnMenuItemClickListener {
            NavHostFragment
                .findNavController(this)
                .navigate(R.id.action_homeFragment_to_favoriteFragment)
            return@setOnMenuItemClickListener true
        }
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isNotEmpty()) {
                    homeViewModel.setQuery(query)
                    hideKeyboard()
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun initVM() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val defineViewModel: HomeViewModel by viewModels { factory }
        homeViewModel = defineViewModel
    }


    private fun showUserItems(usernameItems: List<User>) {
        searchUserAdapter.items = usernameItems
        when {
            usernameItems.isNotEmpty() -> binding.lblHomeAtas.text =
                StringBuilder(getString(R.string.user_found)).append(" @${usernameItems[0].username} : ${usernameItems.size}")
            usernameItems.isEmpty() -> {
                binding.lblHomeAtas.text = USER_NOT_FOUND
                Toast.makeText(context, USER_NOT_FOUND, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showSearchList() {
        binding.rvUserGithub.layoutManager = LinearLayoutManager(context)
        searchUserAdapter = SearchUserAdapter()
        binding.rvUserGithub.adapter = searchUserAdapter
        binding.rvUserGithub.setHasFixedSize(true)
        searchUserAdapter.setOnItemClickCallback(object : SearchUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                setSelectedUser(data)
            }
        })
    }

    private fun setSelectedUser(data: User) {
        val mBundle = Bundle()
        mBundle.putParcelable(LIST_USER_LIST, data)
        NavHostFragment
            .findNavController(this)
            .navigate(R.id.action_homeFragment_to_detailUserFragment, mBundle)
        hideKeyboard()
    }


    private fun showLoading() {
        binding.apply {
            rvUserGithub.visibility = View.GONE
            pbHome.visibility = View.VISIBLE
        }
    }

    private fun hideLoading() {
        binding.apply {
            rvUserGithub.visibility = View.VISIBLE
            pbHome.visibility = View.GONE
        }
    }

    private fun hideKeyboard() {
        val view: View? = activity?.currentFocus
        if (view != null) {
            val imm: InputMethodManager =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    companion object {
        const val USER_NOT_FOUND = "User Not Found"
    }

    init {
        setHasOptionsMenu(true)
    }

}
