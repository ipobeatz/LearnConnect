package com.android.learnconnect.exploreScreen.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.learnconnect.R
import com.android.learnconnect.exploreScreen.models.Category
import com.android.learnconnect.exploreScreen.adapters.CategoriesListAdapter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

class ShowAllCategoriesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_show_all_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Toolbar geri butonunu ayarla
        val toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        // JSON verisini al ve parse et
        val categories = getCategoriesFromJson()

        // RecyclerView ayarları
        val recyclerView = view.findViewById<RecyclerView>(R.id.allCategoriesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = CategoriesListAdapter(categories) { category ->
            Toast.makeText(requireContext(), "Selected: ${category.title}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCategoriesFromJson(): List<Category> {
        val inputStream = requireContext().resources.openRawResource(R.raw.categories)
        val reader = InputStreamReader(inputStream)
        val type = object : TypeToken<List<Category>>() {}.type
        return Gson().fromJson(reader, type)
    }
}

