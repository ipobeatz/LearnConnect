package com.android.learnconnect.ui.mycourse

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2 // Tab sayısı: Kayıtlı Derslerim ve Favorilerim

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> RegisteredCoursesFragment() // Kayıtlı Derslerim Fragment
            1 -> FavoritesFragment() // Favorilerim Fragment
            else -> RegisteredCoursesFragment()
        }
    }
}