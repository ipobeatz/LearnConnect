package com.android.learnconnect.exploreScreen.fragments

import com.android.learnconnect.exploreScreen.adapters.CategoriesAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.learnconnect.R
import com.android.learnconnect.exploreScreen.FilteredCoursesFragmentDirections.Companion.actionExploreFragmentToFilteredCoursesFragment
import com.android.learnconnect.exploreScreen.models.Category
import com.android.learnconnect.exploreScreen.models.Course
import com.android.learnconnect.exploreScreen.adapters.CoursesAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

class ExploreFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_explore, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // JSON'dan kategoriler verisini al
        val categories = getCategoriesFromJson()

        // Kategoriler RecyclerView
        val categoriesRecyclerView = view.findViewById<RecyclerView>(R.id.categoriesRecyclerView)
        categoriesRecyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
        categoriesRecyclerView.adapter = CategoriesAdapter(categories) { category ->
            Toast.makeText(requireContext(), "Selected: ${category.title}", Toast.LENGTH_SHORT).show()
        }

        // Tüm Kategoriler Buttonu
        val showAllButton = view.findViewById<TextView>(R.id.showAllCategories)
        showAllButton.setOnClickListener {
            findNavController().navigate(R.id.action_exploreFragment_to_showAllCategoriesFragment)
        }

        // Kursları JSON verisinden al
        val courses = getCoursesFromJson()

        // Popüler Kurslar RecyclerView
        val coursesRecyclerView = view.findViewById<RecyclerView>(R.id.popularCoursesRecyclerView)
        coursesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        coursesRecyclerView.adapter = CoursesAdapter(courses) { course ->
            val action = ExploreFragmentDirections.actionExploreFragmentToCourseDetailFragment(
                courseName = course.name,
                courseDescription = course.description,
                courseImageUrl = course.imageUrl,
                coursePrice = "$${course.coursePrice}"
            )
            findNavController().navigate(action)
        }
        categoriesRecyclerView.adapter = CategoriesAdapter(categories) { category ->
            // JSON'dan alınan kursları filtrele
            val filteredCourses = getCoursesFromJson().filter { it.category == category.title }

            // Geçiş için action tanımı
            val action = ExploreFragmentDirections.actionExploreFragmentToFilteredCoursesFragment(
                categoryName = category.title,
                filteredCourses = Gson().toJson(filteredCourses) // JSON'a çevirerek gönder
            )
            findNavController().navigate(action)
        }
    }

    private fun getCategoriesFromJson(): List<Category> {
        val inputStream = requireContext().resources.openRawResource(R.raw.categories)
        val reader = InputStreamReader(inputStream)
        val type = object : TypeToken<List<Category>>() {}.type
        return Gson().fromJson(reader, type)
    }

    private fun getCoursesFromJson(): List<Course> {
        val inputStream = requireContext().resources.openRawResource(R.raw.courses)
        val reader = InputStreamReader(inputStream)
        val type = object : TypeToken<List<Course>>() {}.type
        return Gson().fromJson(reader, type)
    }
}
