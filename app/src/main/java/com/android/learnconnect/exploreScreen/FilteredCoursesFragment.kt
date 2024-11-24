package com.android.learnconnect.exploreScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.learnconnect.R
import com.android.learnconnect.exploreScreen.adapters.CoursesAdapter
import com.android.learnconnect.exploreScreen.models.Course
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FilteredCoursesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_filtered_courses, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Toolbar'ı bul ve başlık ayarla
        val toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar?.title = arguments?.getString("categoryName") ?: "Kategoriler"
        toolbar?.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        // Argümanlardan alınan JSON verisini parse et
        val filteredCoursesJson = arguments?.getString("filteredCourses") ?: "[]"
        val filteredCourses: List<Course> = Gson().fromJson(
            filteredCoursesJson,
            object : TypeToken<List<Course>>() {}.type
        )

        // RecyclerView için ayarlar
        val recyclerView = view.findViewById<RecyclerView>(R.id.filteredCoursesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = FilteredCoursesAdapter(filteredCourses) { course ->
            Toast.makeText(requireContext(), "Selected: ${course.name}", Toast.LENGTH_SHORT).show()
        }
    }
}
