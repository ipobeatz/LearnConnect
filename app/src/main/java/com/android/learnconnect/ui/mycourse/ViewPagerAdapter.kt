package com.android.learnconnect.ui.mycourse

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> RegisteredCoursesFragment()
            1 -> FavoritesFragment()
            else -> RegisteredCoursesFragment()
        }
    }
}