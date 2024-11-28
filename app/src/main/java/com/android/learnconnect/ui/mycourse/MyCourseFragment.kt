package com.android.learnconnect.ui.mycourse

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.android.learnconnect.R
import com.android.learnconnect.ui.profile.ProfileViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyCourseFragment : Fragment(), CourseNavigationListener {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_course, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabLayout = view.findViewById(R.id.tab_layout)
        viewPager = view.findViewById(R.id.view_pager)

        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            val customView = LayoutInflater.from(requireContext())
                .inflate(R.layout.custom_tab, null)

            val tabIcon = customView.findViewById<ImageView>(R.id.tab_icon)
            val tabText = customView.findViewById<TextView>(R.id.tab_text)

            when (position) {
                0 -> {
                    tabIcon.setImageResource(R.drawable.saved) // Icon for "Kayıtlı Derslerim"
                    tabText.text = getString(R.string.my_saved_courses)
                }
                1 -> {
                    tabIcon.setImageResource(R.drawable.favorite_icon) // Icon for "Favorilerim"
                    tabText.text = getString(R.string.my_favorites)
                }
            }

            tab.customView = customView
        }.attach()
    }

    override fun onNavigateToCourseDetail(courseId: String) {
        val action = MyCourseFragmentDirections
            .actionMyCourseFragmentToCourseDetailFragment(courseId)
        findNavController().navigate(action)
    }
}
