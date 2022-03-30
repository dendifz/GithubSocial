package com.dicoding.githubsocial.ui.listfollow

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.githubsocial.ui.listfollow.DetailFollowFragment.Companion.EXTRA_INDEX
import com.dicoding.githubsocial.ui.listfollow.DetailFollowFragment.Companion.EXTRA_USERNAME

class SectionPageAdapter(activity: Fragment) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2
    lateinit var username: String

    override fun createFragment(position: Int): Fragment {

        val fragment: Fragment = DetailFollowFragment()

        fragment.arguments = Bundle().apply {
            putString(EXTRA_USERNAME, username)
            putInt(EXTRA_INDEX, position)
        }

        return fragment

    }

}