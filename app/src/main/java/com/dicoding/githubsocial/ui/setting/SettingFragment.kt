package com.dicoding.githubsocial.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dicoding.githubsocial.databinding.FragmentSettingBinding
import com.dicoding.githubsocial.ui.ViewModelFactory
import com.dicoding.githubsocial.ui.main.MainActivity


class SettingFragment : Fragment() {


    private lateinit var settingViewModel: SettingViewModel
    private lateinit var _binding: FragmentSettingBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVM()
        (activity as MainActivity?)!!.supportActionBar?.title = "Setting"

        binding.modeDark.setOnCheckedChangeListener { _, isChecked ->
            settingViewModel.saveTheme(isChecked)
        }

        settingViewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.modeDark.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.modeDark.isChecked = false
            }
        }

    }

    private fun initVM() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val defineViewModel: SettingViewModel by viewModels { factory }
        settingViewModel = defineViewModel
    }
}