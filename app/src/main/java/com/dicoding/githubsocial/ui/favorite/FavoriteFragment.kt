package com.dicoding.githubsocial.ui.favorite

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubsocial.R
import com.dicoding.githubsocial.data.source.local.entity.FavoriteUserEntity
import com.dicoding.githubsocial.databinding.FragmentFavoriteBinding
import com.dicoding.githubsocial.ui.ViewModelFactory
import com.dicoding.githubsocial.ui.detailuser.DetailUserFragment
import com.dicoding.githubsocial.ui.main.MainActivity
import com.dicoding.githubsocial.util.Status

class FavoriteFragment : Fragment() {

    private lateinit var _binding: FragmentFavoriteBinding
    private val binding get() = _binding
    private lateinit var listFavAdapter: ListFavAdapter
    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initVM()

        getFavoriteData()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favorite_menu, menu)
        val settingView = menu.findItem(R.id.setting)
        settingView.setOnMenuItemClickListener {
            NavHostFragment
                .findNavController(this)
                .navigate(R.id.action_favoriteFragment_to_settingFragment)
            return@setOnMenuItemClickListener true
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun getFavoriteData() {
        showFollowingList()
        favoriteViewModel.getFavoriteUser().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result.status) {
                    Status.LOADING -> showLoading()
                    Status.SUCCESS -> {
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

    private fun showLoading() {
        binding.apply {
            rvFavorite.visibility = View.GONE
            pbFavorite.visibility = View.VISIBLE
        }
    }

    private fun hideLoading() {
        binding.apply {
            rvFavorite.visibility = View.VISIBLE
            pbFavorite.visibility = View.GONE
        }
    }

    private fun hapusSelectedUser(data: FavoriteUserEntity) {
        favoriteViewModel.deleteFavoriteUser(data)
        Toast.makeText(context, "${data.name} dihapus", Toast.LENGTH_SHORT).show()
        getFavoriteData()
    }

    private fun setSelectedUser(data: String) {
        val mBundle = Bundle()
        mBundle.putString(DetailUserFragment.LIST_USER_LIST, data)
        NavHostFragment
            .findNavController(this)
            .navigate(R.id.action_favoriteFragment_to_detailUserFragment, mBundle)
    }

    private fun showUserItems(usernameItems: List<FavoriteUserEntity>) {
        listFavAdapter.items = usernameItems
        listFavAdapter.setOnItemClickCallback(object : ListFavAdapter.OnItemClickCallback {
            override fun onItemClicked(data: FavoriteUserEntity) {
                data.username?.let { setSelectedUser(it) }
            }

            override fun hapusClicked(data: FavoriteUserEntity) {
                hapusSelectedUser(data)
            }
        })
    }

    private fun showFollowingList() {
        (activity as MainActivity?)?.supportActionBar?.title = "Favorite List"
        binding.rvFavorite.layoutManager = LinearLayoutManager(context)
        listFavAdapter = ListFavAdapter()
        binding.rvFavorite.adapter = listFavAdapter
        binding.rvFavorite.setHasFixedSize(true)
    }

    private fun initVM() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireContext())
        val defineViewModel: FavoriteViewModel by viewModels { factory }
        favoriteViewModel = defineViewModel
    }

    init {
        setHasOptionsMenu(true)
    }
}