package com.android.learnconnect.domain.entity

sealed class DashboardItem {
    data class HorizontalList(val items: List<Course>) : DashboardItem()
    data class CourseRow(val course: Course) : DashboardItem()
}
