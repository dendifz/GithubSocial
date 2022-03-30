package com.dicoding.githubsocial.ui.detailuser

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.githubsocial.R
import com.dicoding.githubsocial.data.model.User
import com.dicoding.githubsocial.data.source.local.entity.FavoriteUserEntity
import com.dicoding.githubsocial.databinding.FragmentDetailUserBinding
import com.dicoding.githubsocial.ui.ViewModelFactory
import com.dicoding.githubsocial.ui.listfollow.SectionPageAdapter
import com.dicoding.githubsocial.ui.main.MainActivity
import com.dicoding.githubsocial.util.Status
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailUserFragment : Fragment() {

    private lateinit var detailViewModel: DetailUserViewModel

    private lateinit var _binding: FragmentDetailUserBinding
    private val binding get() = _binding
    private lateinit var username: String
    private lateinit var tempList: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initVM()

        username = if (arguments?.getString(LIST_USER_LIST) != null) {
            val data = arguments?.getString(LIST_USER_LIST)
            data!!
        } else {
            val data = arguments?.getParcelable<User>(LIST_USER_LIST) as User
            data.username.toString()
        }

        val sectionsPagerAdapter = SectionPageAdapter(this)
        val viewPager: ViewPager2 = binding.vpDetail
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabDetailUser
        sectionsPagerAdapter.username = username

        lifecycleScope.launch(Dispatchers.Default) {
            if (detailViewModel.cekFavorite(username))
                binding.fabFavorite.hide()
            else
                binding.fabFavorite.setOnClickListener {
                    setFavoriteData(tempList)
                }
        }
        detailViewModel.detailUser(username).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result.status) {
                    Status.LOADING -> showLoading()
                    Status.SUCCESS -> {
                        (activity as MainActivity?)?.supportActionBar?.title = result.data!!.name
                        tempList = result.data
                        Glide.with(this)
                            .load(result.data.avatar)
                            .circleCrop()
                            .into(binding.imgDetailUser)

                        binding.apply {
                            result.data.apply {
                                TabLayoutMediator(tabs, viewPager) { tab, position ->
                                    tab.text = StringBuilder(
                                        when (position) {
                                            0 -> "$followers "
                                            else -> "$following "
                                        }
                                    ).append(resources.getString(TAB_TITLES[position]))
                                }.attach()

                                tvDetailUsername.text = username
                                if (name != null) tvDetailNama.text = name
                                else tvDetailNama.text = StringBuilder("No name")
                                if (repository != 0) tvDetailRepository.text =
                                    StringBuilder("$repository Repository")
                                else tvDetailRepository.text = StringBuilder("No Repository")
                                if (company != null) tvDetailCompany.text = company
                                else tvDetailCompany.text = StringBuilder("Not Have Company")
                                if (location != null) tvDetailLocation.text = location
                                else tvDetailLocation.text = StringBuilder("No Location")
                            }
                        }
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.detail_menu, menu)
        val settingView = menu.findItem(R.id.setting)
        settingView.setOnMenuItemClickListener {
            NavHostFragment
                .findNavController(this)
                .navigate(R.id.action_detailUserFragment_to_settingFragment)
            return@setOnMenuItemClickListener true
        }

        val favoriteView = menu.findItem(R.id.favorite_menu)
        favoriteView.setOnMenuItemClickListener {
            NavHostFragment
                .findNavController(this)
                .navigate(R.id.action_detailUserFragment_to_favoriteFragment)
            return@setOnMenuItemClickListener true
        }

        super.onCreateOptionsMenu(menu, inflater)
    }


    private fun initVM() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val defineViewModel: DetailUserViewModel by viewModels { factory }
        detailViewModel = defineViewModel
    }

    private fun showLoading() {
        binding.apply {
            tabDetailUser.visibility = View.GONE
            vpDetail.visibility = View.GONE
            pbDetailUser.visibility = View.VISIBLE
        }
    }

    private fun hideLoading() {
        binding.apply {
            tabDetailUser.visibility = View.VISIBLE
            vpDetail.visibility = View.VISIBLE
            pbDetailUser.visibility = View.GONE
        }
    }

    private fun setFavoriteData(items: User) {
        val user = FavoriteUserEntity(
            items.id,
            items.name,
            items.username,
            items.avatar,
            items.type,
            true
        )
        detailViewModel.addFavorite(user)
        Toast.makeText(context, "${items.name} ditambahkan", Toast.LENGTH_SHORT).show()
        binding.fabFavorite.hide()

    }

    companion object {
        const val LIST_USER_LIST = "_list"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    init {
        setHasOptionsMenu(true)
    }
}