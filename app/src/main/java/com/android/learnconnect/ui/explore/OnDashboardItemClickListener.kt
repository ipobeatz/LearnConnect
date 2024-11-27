package com.android.learnconnect.ui.explore

import com.android.learnconnect.domain.entity.Course

interface OnDashboardItemClickListener {
    fun onItemClicked(item: Course)
}
