package com.android.learnconnect.ui.mycourse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.android.learnconnect.R
import com.android.learnconnect.databinding.FragmentMyCourseBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyCourseFragment : Fragment(), CourseNavigationListener {
    private var _binding: FragmentMyCourseBinding? = null
    private val binding get() = _binding!!
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyCourseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabLayout = view.findViewById(R.id.tab_layout)
        viewPager = view.findViewById(R.id.view_pager)

        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            val customView =
                LayoutInflater.from(requireContext()).inflate(R.layout.custom_tab, null)

            val tabIcon = customView.findViewById<ImageView>(R.id.tab_icon)
            val tabText = customView.findViewById<TextView>(R.id.tab_text)

            when (position) {
                0 -> {
                    tabIcon.setImageResource(R.drawable.saved)
                    tabText.text = getString(R.string.my_saved_courses)
                }

                1 -> {
                    tabIcon.setImageResource(R.drawable.favorite_icon)
                    tabText.text = getString(R.string.my_favorites)
                }
            }

            tab.customView = customView
        }.attach()
    }


    override fun onNavigateToCourseDetail(courseId: String) {
        val action =
            MyCourseFragmentDirections.actionMyCourseFragmentToCourseDetailFragment(courseId)
        findNavController().navigate(action)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
