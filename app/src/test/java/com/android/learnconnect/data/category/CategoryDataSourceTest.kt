package com.android.learnconnect.data.category

import com.android.learnconnect.domain.entity.ResultData
import com.android.learnconnect.domain.mockdata.fake.FakeAssetManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
class CategoryDataSourceTest {

    private lateinit var categoryDataSource: CategoryDataSource
    private val assets: FakeAssetManager = mock()

    @Before
    fun setUp() {
        val jsonConfig = Json {
            ignoreUnknownKeys = true
            isLenient = true
            allowSpecialFloatingPointValues = true
            useArrayPolymorphism = true
        }
        categoryDataSource = CategoryDataSource(jsonConfig, assets)
    }


    @Test
    fun `getCategoryList emits Error when json parsing fails`() = runTest {
        val exception = RuntimeException("Failed to parse")
        `when`(assets.open(CategoryDataSource.CATEGORIES_JSON)).thenThrow(exception)

        val results = categoryDataSource.getCategoryList().toList()

        assert(results[0] is ResultData.Error)
        assert((results[0] as ResultData.Error).exception.message == "Error during parse json: ${exception.message}")
    }
}
