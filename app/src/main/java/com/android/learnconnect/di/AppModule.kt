package com.android.learnconnect.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.android.learnconnect.data.category.CategoryDataSource
import com.android.learnconnect.data.category.CategoryRepository
import com.android.learnconnect.data.course.CourseDataSource
import com.android.learnconnect.data.course.CourseRepository
import com.android.learnconnect.data.locale.AppDatabase
import com.android.learnconnect.data.locale.CourseDao
import com.android.learnconnect.domain.mockdata.MockData
import com.android.learnconnect.domain.mockdata.fake.FakeAssetManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesNetworkJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun providesFakeAssetManager(
        @ApplicationContext context: Context,
    ): FakeAssetManager = FakeAssetManager(context.assets::open)

    @Provides
    @Singleton
    fun provideDatabase(application: Application, courseDao: Provider<CourseDao>): AppDatabase {
        val callback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                CoroutineScope(Dispatchers.IO).launch {
                    populateDatabase(courseDao.get())
                }
            }
        }

        return Room.databaseBuilder(application, AppDatabase::class.java, "app_database")
            .allowMainThreadQueries().fallbackToDestructiveMigration().addCallback(callback).build()
    }

    suspend fun populateDatabase(courseDao: CourseDao) {
        courseDao.insertAll(
            *MockData.mockData.toTypedArray()
        )
    }

    @Provides
    fun provideCourseDao(database: AppDatabase): CourseDao {
        return database.courseDao()
    }

    @Provides
    @Singleton
    fun provideCoursesDataSource(
        courseDao: CourseDao
    ): CourseRepository = CourseDataSource(courseDao)

    @Provides
    @Singleton
    fun provideCategoryDataSource(
        json: Json, fakeAssetManager: FakeAssetManager
    ): CategoryRepository = CategoryDataSource(json, fakeAssetManager)

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideGlideInstance(
        @ApplicationContext context: Context
    ): RequestManager {
        return Glide.with(context)
    }
}
