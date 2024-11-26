package com.android.learnconnect.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.android.learnconnect.data.CategoryDataSource
import com.android.learnconnect.data.CategoryRepository
import com.android.learnconnect.data.CourseDataSource
import com.android.learnconnect.data.CourseRepository
import com.android.learnconnect.data.locale.AppDatabase
import com.android.learnconnect.data.locale.CourseDao
import com.android.learnconnect.domain.entity.Course
import com.android.learnconnect.domain.entity.VideoItem
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
            Course(
                "1",
                "Introduction to Design",
                "Learn the basics of design and improve your skills.",
                "https://via.placeholder.com/150",
                false,
                2.5f,
                49.99f,
                "Tasarım ve Yaratıcılık",
                listOf(
                    VideoItem(
                        "1",
                        "ChatGpt Log in",
                        "00:47",
                        "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"
                    ),
                    VideoItem(
                        "2",
                        "Çalışma Kitabı ve Çalışma Sayfası Ekle",
                        "10:04",
                        "https://example.com/video2.mp4"
                    ),
                    VideoItem(
                        "3",
                        "Çalışma Sayfalarını Yönetelim",
                        "04:58",
                        "https://example.com/video3.mp4"
                    ),
                    VideoItem(
                        "4",
                        "Hücreleri Yönetelim",
                        "04:25",
                        "https://example.com/video4.mp4"
                    ),
                    VideoItem("5", "Yapı Oluşturalım", "07:09", "https://example.com/video5.mp4"),
                    VideoItem(
                        "6",
                        "Gerçek Hayata Dair Problemlerimizi Çözelim",
                        "06:48",
                        "https://example.com/video6.mp4"
                    )
                )
            ), Course(
                "2",
                "Advanced Java Programming",
                "Master Java programming concepts and techniques.",
                "https://via.placeholder.com/150",
                false,
                4.0f,
                39.99f,
                "Yazılım Geliştirme",
                listOf(
                    VideoItem(
                        "1",
                        "ChatGpt Log in",
                        "00:47",
                        "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"
                    ),
                    VideoItem(
                        "2",
                        "Çalışma Kitabı ve Çalışma Sayfası Ekle",
                        "10:04",
                        "https://example.com/video2.mp4"
                    ),
                    VideoItem(
                        "3",
                        "Çalışma Sayfalarını Yönetelim",
                        "04:58",
                        "https://example.com/video3.mp4"
                    ),
                    VideoItem(
                        "4",
                        "Hücreleri Yönetelim",
                        "04:25",
                        "https://example.com/video4.mp4"
                    ),
                    VideoItem("5", "Yapı Oluşturalım", "07:09", "https://example.com/video5.mp4"),
                    VideoItem(
                        "6",
                        "Gerçek Hayata Dair Problemlerimizi Çözelim",
                        "06:48",
                        "https://example.com/video6.mp4"
                    )
                )
            ), Course(
                "3",
                "Data Science Essentials",
                "An essential guide to the field of data science.",
                "https://via.placeholder.com/150",
                false,
                4.8f,
                59.99f,
                "BT ve Yazılım",
                listOf(
                    VideoItem(
                        "1",
                        "ChatGpt Log in",
                        "00:47",
                        "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"
                    ),
                    VideoItem(
                        "2",
                        "Çalışma Kitabı ve Çalışma Sayfası Ekle",
                        "10:04",
                        "https://example.com/video2.mp4"
                    ),
                    VideoItem(
                        "3",
                        "Çalışma Sayfalarını Yönetelim",
                        "04:58",
                        "https://example.com/video3.mp4"
                    ),
                    VideoItem(
                        "4",
                        "Hücreleri Yönetelim",
                        "04:25",
                        "https://example.com/video4.mp4"
                    ),
                    VideoItem("5", "Yapı Oluşturalım", "07:09", "https://example.com/video5.mp4"),
                    VideoItem(
                        "6",
                        "Gerçek Hayata Dair Problemlerimizi Çözelim",
                        "06:48",
                        "https://example.com/video6.mp4"
                    )
                )
            ), Course(
                "4",
                "Marketing Mastery",
                "Learn advanced marketing strategies.",
                "https://via.placeholder.com/150",
                false,
                4.2f,
                44.99f,
                "Pazarlama",
                listOf(
                    VideoItem(
                        "1",
                        "ChatGpt Log in",
                        "00:47",
                        "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"
                    ),
                    VideoItem(
                        "2",
                        "Çalışma Kitabı ve Çalışma Sayfası Ekle",
                        "10:04",
                        "https://example.com/video2.mp4"
                    ),
                    VideoItem(
                        "3",
                        "Çalışma Sayfalarını Yönetelim",
                        "04:58",
                        "https://example.com/video3.mp4"
                    ),
                    VideoItem(
                        "4",
                        "Hücreleri Yönetelim",
                        "04:25",
                        "https://example.com/video4.mp4"
                    ),
                    VideoItem("5", "Yapı Oluşturalım", "07:09", "https://example.com/video5.mp4"),
                    VideoItem(
                        "6",
                        "Gerçek Hayata Dair Problemlerimizi Çözelim",
                        "06:48",
                        "https://example.com/video6.mp4"
                    )
                )
            ), Course(
                "5",
                "Photography 101",
                "Capture stunning photos with this beginner's guide.",
                "https://via.placeholder.com/150",
                false,
                4.3f,
                29.99f,
                "Tasarım ve Yaratıcılık",
                listOf(
                    VideoItem(
                        "1",
                        "ChatGpt Log in",
                        "00:47",
                        "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"
                    ),
                    VideoItem(
                        "2",
                        "Çalışma Kitabı ve Çalışma Sayfası Ekle",
                        "10:04",
                        "https://example.com/video2.mp4"
                    ),
                    VideoItem(
                        "3",
                        "Çalışma Sayfalarını Yönetelim",
                        "04:58",
                        "https://example.com/video3.mp4"
                    ),
                    VideoItem(
                        "4",
                        "Hücreleri Yönetelim",
                        "04:25",
                        "https://example.com/video4.mp4"
                    ),
                    VideoItem("5", "Yapı Oluşturalım", "07:09", "https://example.com/video5.mp4"),
                    VideoItem(
                        "6",
                        "Gerçek Hayata Dair Problemlerimizi Çözelim",
                        "06:48",
                        "https://example.com/video6.mp4"
                    )
                )
            ), Course(
                "6",
                "UI/UX Design Fundamentals",
                "Understand the principles of effective UI/UX design.",
                "https://via.placeholder.com/150",
                false,
                4.7f,
                54.99f,
                "Tasarım ve Yaratıcılık",
                listOf(
                    VideoItem(
                        "1",
                        "ChatGpt Log in",
                        "00:47",
                        "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"
                    ),
                    VideoItem(
                        "2",
                        "Çalışma Kitabı ve Çalışma Sayfası Ekle",
                        "10:04",
                        "https://example.com/video2.mp4"
                    ),
                    VideoItem(
                        "3",
                        "Çalışma Sayfalarını Yönetelim",
                        "04:58",
                        "https://example.com/video3.mp4"
                    ),
                    VideoItem(
                        "4",
                        "Hücreleri Yönetelim",
                        "04:25",
                        "https://example.com/video4.mp4"
                    ),
                    VideoItem("5", "Yapı Oluşturalım", "07:09", "https://example.com/video5.mp4"),
                    VideoItem(
                        "6",
                        "Gerçek Hayata Dair Problemlerimizi Çözelim",
                        "06:48",
                        "https://example.com/video6.mp4"
                    )
                )
            ), Course(
                "7",
                "Web Development Bootcamp",
                "Build websites with HTML, CSS, and JavaScript.",
                "https://via.placeholder.com/150",
                false,
                4.9f,
                79.99f,
                "Yazılım Geliştirme",
                listOf(
                    VideoItem(
                        "1",
                        "ChatGpt Log in",
                        "00:47",
                        "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"
                    ),
                    VideoItem(
                        "2",
                        "Çalışma Kitabı ve Çalışma Sayfası Ekle",
                        "10:04",
                        "https://example.com/video2.mp4"
                    ),
                    VideoItem(
                        "3",
                        "Çalışma Sayfalarını Yönetelim",
                        "04:58",
                        "https://example.com/video3.mp4"
                    ),
                    VideoItem(
                        "4",
                        "Hücreleri Yönetelim",
                        "04:25",
                        "https://example.com/video4.mp4"
                    ),
                    VideoItem("5", "Yapı Oluşturalım", "07:09", "https://example.com/video5.mp4"),
                    VideoItem(
                        "6",
                        "Gerçek Hayata Dair Problemlerimizi Çözelim",
                        "06:48",
                        "https://example.com/video6.mp4"
                    )
                )
            ), Course(
                "8",
                "Artificial Intelligence Basics",
                "Explore the fundamentals of AI and machine learning.",
                "https://via.placeholder.com/150",
                false,
                4.6f,
                64.99f,
                "BT ve Yazılım",
                listOf(
                    VideoItem(
                        "1",
                        "ChatGpt Log in",
                        "00:47",
                        "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"
                    ),
                    VideoItem(
                        "2",
                        "Çalışma Kitabı ve Çalışma Sayfası Ekle",
                        "10:04",
                        "https://example.com/video2.mp4"
                    ),
                    VideoItem(
                        "3",
                        "Çalışma Sayfalarını Yönetelim",
                        "04:58",
                        "https://example.com/video3.mp4"
                    ),
                    VideoItem(
                        "4",
                        "Hücreleri Yönetelim",
                        "04:25",
                        "https://example.com/video4.mp4"
                    ),
                    VideoItem("5", "Yapı Oluşturalım", "07:09", "https://example.com/video5.mp4"),
                    VideoItem(
                        "6",
                        "Gerçek Hayata Dair Problemlerimizi Çözelim",
                        "06:48",
                        "https://example.com/video6.mp4"
                    )
                )
            ), Course(
                "9",
                "Mobile App Development with Kotlin",
                "Create mobile apps using Kotlin for Android.",
                "https://via.placeholder.com/150",
                false,
                4.4f,
                59.99f,
                "Yazılım Geliştirme",
                listOf(
                    VideoItem(
                        "1",
                        "ChatGpt Log in",
                        "00:47",
                        "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"
                    ),
                    VideoItem(
                        "2",
                        "Çalışma Kitabı ve Çalışma Sayfası Ekle",
                        "10:04",
                        "https://example.com/video2.mp4"
                    ),
                    VideoItem(
                        "3",
                        "Çalışma Sayfalarını Yönetelim",
                        "04:58",
                        "https://example.com/video3.mp4"
                    ),
                    VideoItem(
                        "4",
                        "Hücreleri Yönetelim",
                        "04:25",
                        "https://example.com/video4.mp4"
                    ),
                    VideoItem("5", "Yapı Oluşturalım", "07:09", "https://example.com/video5.mp4"),
                    VideoItem(
                        "6",
                        "Gerçek Hayata Dair Problemlerimizi Çözelim",
                        "06:48",
                        "https://example.com/video6.mp4"
                    )
                )
            ), Course(
                "10",
                "Cybersecurity Essentials",
                "Understand the basics of cybersecurity and how to protect systems.",
                "https://via.placeholder.com/150",
                false,
                4.1f,
                34.99f,
                "BT ve Yazılım",
                listOf(
                    VideoItem(
                        "1",
                        "ChatGpt Log in",
                        "00:47",
                        "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"
                    ),
                    VideoItem(
                        "2",
                        "Çalışma Kitabı ve Çalışma Sayfası Ekle",
                        "10:04",
                        "https://example.com/video2.mp4"
                    ),
                    VideoItem(
                        "3",
                        "Çalışma Sayfalarını Yönetelim",
                        "04:58",
                        "https://example.com/video3.mp4"
                    ),
                    VideoItem(
                        "4",
                        "Hücreleri Yönetelim",
                        "04:25",
                        "https://example.com/video4.mp4"
                    ),
                    VideoItem("5", "Yapı Oluşturalım", "07:09", "https://example.com/video5.mp4"),
                    VideoItem(
                        "6",
                        "Gerçek Hayata Dair Problemlerimizi Çözelim",
                        "06:48",
                        "https://example.com/video6.mp4"
                    )
                )
            ), Course(
                "11",
                "Time Management Strategies",
                "Improve productivity with time management techniques.",
                "https://via.placeholder.com/150",
                false,
                4.5f,
                24.99f,
                "Kişisel Gelişim",
                listOf(
                    VideoItem(
                        "1",
                        "ChatGpt Log in",
                        "00:47",
                        "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"
                    ),
                    VideoItem(
                        "2",
                        "Çalışma Kitabı ve Çalışma Sayfası Ekle",
                        "10:04",
                        "https://example.com/video2.mp4"
                    ),
                    VideoItem(
                        "3",
                        "Çalışma Sayfalarını Yönetelim",
                        "04:58",
                        "https://example.com/video3.mp4"
                    ),
                    VideoItem(
                        "4",
                        "Hücreleri Yönetelim",
                        "04:25",
                        "https://example.com/video4.mp4"
                    ),
                    VideoItem("5", "Yapı Oluşturalım", "07:09", "https://example.com/video5.mp4"),
                    VideoItem(
                        "6",
                        "Gerçek Hayata Dair Problemlerimizi Çözelim",
                        "06:48",
                        "https://example.com/video6.mp4"
                    )
                )
            ), Course(
                "12",
                "Leadership in the Workplace",
                "Learn how to lead and manage teams effectively.",
                "https://via.placeholder.com/150",
                false,
                4.3f,
                49.99f,
                "Kişisel Gelişim",
                listOf(
                    VideoItem(
                        "1",
                        "ChatGpt Log in",
                        "00:47",
                        "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"
                    ),
                    VideoItem(
                        "2",
                        "Çalışma Kitabı ve Çalışma Sayfası Ekle",
                        "10:04",
                        "https://example.com/video2.mp4"
                    ),
                    VideoItem(
                        "3",
                        "Çalışma Sayfalarını Yönetelim",
                        "04:58",
                        "https://example.com/video3.mp4"
                    ),
                    VideoItem(
                        "4",
                        "Hücreleri Yönetelim",
                        "04:25",
                        "https://example.com/video4.mp4"
                    ),
                    VideoItem("5", "Yapı Oluşturalım", "07:09", "https://example.com/video5.mp4"),
                    VideoItem(
                        "6",
                        "Gerçek Hayata Dair Problemlerimizi Çözelim",
                        "06:48",
                        "https://example.com/video6.mp4"
                    )
                )
            ), Course(
                "13",
                "Strategic Financial Planning",
                "Master financial planning techniques.",
                "https://via.placeholder.com/150",
                false,
                4.7f,
                74.99f,
                "Finans ve Muhasebe",
                listOf(
                    VideoItem(
                        "1",
                        "ChatGpt Log in",
                        "00:47",
                        "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"
                    ),
                    VideoItem(
                        "2",
                        "Çalışma Kitabı ve Çalışma Sayfası Ekle",
                        "10:04",
                        "https://example.com/video2.mp4"
                    ),
                    VideoItem(
                        "3",
                        "Çalışma Sayfalarını Yönetelim",
                        "04:58",
                        "https://example.com/video3.mp4"
                    ),
                    VideoItem(
                        "4",
                        "Hücreleri Yönetelim",
                        "04:25",
                        "https://example.com/video4.mp4"
                    ),
                    VideoItem("5", "Yapı Oluşturalım", "07:09", "https://example.com/video5.mp4"),
                    VideoItem(
                        "6",
                        "Gerçek Hayata Dair Problemlerimizi Çözelim",
                        "06:48",
                        "https://example.com/video6.mp4"
                    )
                )
            ), Course(
                "14",
                "Corporate Accounting Basics",
                "Understand accounting practices for businesses.",
                "https://via.placeholder.com/150",
                false,
                3.9f,
                29.99f,
                "Finans ve Muhasebe",
                listOf(
                    VideoItem(
                        "1",
                        "ChatGpt Log in",
                        "00:47",
                        "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"
                    ),
                    VideoItem(
                        "2",
                        "Çalışma Kitabı ve Çalışma Sayfası Ekle",
                        "10:04",
                        "https://example.com/video2.mp4"
                    ),
                    VideoItem(
                        "3",
                        "Çalışma Sayfalarını Yönetelim",
                        "04:58",
                        "https://example.com/video3.mp4"
                    ),
                    VideoItem(
                        "4",
                        "Hücreleri Yönetelim",
                        "04:25",
                        "https://example.com/video4.mp4"
                    ),
                    VideoItem("5", "Yapı Oluşturalım", "07:09", "https://example.com/video5.mp4"),
                    VideoItem(
                        "6",
                        "Gerçek Hayata Dair Problemlerimizi Çözelim",
                        "06:48",
                        "https://example.com/video6.mp4"
                    )
                )
            ), Course(
                "15",
                "Excel for Productivity",
                "Boost productivity with Excel tips and tricks.",
                "https://via.placeholder.com/150",
                false,
                4.4f,
                19.99f,
                "Ofiste Verimlilik",
                listOf(
                    VideoItem(
                        "1",
                        "ChatGpt Log in",
                        "00:47",
                        "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"
                    ),
                    VideoItem(
                        "2",
                        "Çalışma Kitabı ve Çalışma Sayfası Ekle",
                        "10:04",
                        "https://example.com/video2.mp4"
                    ),
                    VideoItem(
                        "3",
                        "Çalışma Sayfalarını Yönetelim",
                        "04:58",
                        "https://example.com/video3.mp4"
                    ),
                    VideoItem(
                        "4",
                        "Hücreleri Yönetelim",
                        "04:25",
                        "https://example.com/video4.mp4"
                    ),
                    VideoItem("5", "Yapı Oluşturalım", "07:09", "https://example.com/video5.mp4"),
                    VideoItem(
                        "6",
                        "Gerçek Hayata Dair Problemlerimizi Çözelim",
                        "06:48",
                        "https://example.com/video6.mp4"
                    )
                )
            ), Course(
                "16",
                "Effective Email Communication",
                "Learn how to write effective emails.",
                "https://via.placeholder.com/150",
                false,
                4.1f,
                14.99f,
                "Ofiste Verimlilik",
                listOf(
                    VideoItem(
                        "1",
                        "ChatGpt Log in",
                        "00:47",
                        "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"
                    ),
                    VideoItem(
                        "2",
                        "Çalışma Kitabı ve Çalışma Sayfası Ekle",
                        "10:04",
                        "https://example.com/video2.mp4"
                    ),
                    VideoItem(
                        "3",
                        "Çalışma Sayfalarını Yönetelim",
                        "04:58",
                        "https://example.com/video3.mp4"
                    ),
                    VideoItem(
                        "4",
                        "Hücreleri Yönetelim",
                        "04:25",
                        "https://example.com/video4.mp4"
                    ),
                    VideoItem("5", "Yapı Oluşturalım", "07:09", "https://example.com/video5.mp4"),
                    VideoItem(
                        "6",
                        "Gerçek Hayata Dair Problemlerimizi Çözelim",
                        "06:48",
                        "https://example.com/video6.mp4"
                    )
                )
            ), Course(
                "17",
                "E-commerce Store Basics",
                "Start and manage an online store successfully.",
                "https://via.placeholder.com/150",
                false,
                4.8f,
                89.99f,
                "E-ticaret Stratejileri",
                listOf(
                    VideoItem(
                        "1",
                        "ChatGpt Log in",
                        "00:47",
                        "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"
                    ),
                    VideoItem(
                        "2",
                        "Çalışma Kitabı ve Çalışma Sayfası Ekle",
                        "10:04",
                        "https://example.com/video2.mp4"
                    ),
                    VideoItem(
                        "3",
                        "Çalışma Sayfalarını Yönetelim",
                        "04:58",
                        "https://example.com/video3.mp4"
                    ),
                    VideoItem(
                        "4",
                        "Hücreleri Yönetelim",
                        "04:25",
                        "https://example.com/video4.mp4"
                    ),
                    VideoItem("5", "Yapı Oluşturalım", "07:09", "https://example.com/video5.mp4"),
                    VideoItem(
                        "6",
                        "Gerçek Hayata Dair Problemlerimizi Çözelim",
                        "06:48",
                        "https://example.com/video6.mp4"
                    )
                )
            ), Course(
                "18",
                "Advanced Photoshop Techniques",
                "Create amazing designs with Photoshop.",
                "https://via.placeholder.com/150",
                false,
                4.7f,
                64.99f,
                "Tasarım ve Yaratıcılık",
                listOf(
                    VideoItem(
                        "1",
                        "ChatGpt Log in",
                        "00:47",
                        "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"
                    ),
                    VideoItem(
                        "2",
                        "Çalışma Kitabı ve Çalışma Sayfası Ekle",
                        "10:04",
                        "https://example.com/video2.mp4"
                    ),
                    VideoItem(
                        "3",
                        "Çalışma Sayfalarını Yönetelim",
                        "04:58",
                        "https://example.com/video3.mp4"
                    ),
                    VideoItem(
                        "4",
                        "Hücreleri Yönetelim",
                        "04:25",
                        "https://example.com/video4.mp4"
                    ),
                    VideoItem("5", "Yapı Oluşturalım", "07:09", "https://example.com/video5.mp4"),
                    VideoItem(
                        "6",
                        "Gerçek Hayata Dair Problemlerimizi Çözelim",
                        "06:48",
                        "https://example.com/video6.mp4"
                    )
                )
            ), Course(
                "19",
                "Introduction to Python",
                "Learn the basics of Python programming.",
                "https://via.placeholder.com/150",
                false,
                4.9f,
                49.99f,
                "BT ve Yazılım",
                listOf(
                    VideoItem(
                        "1",
                        "ChatGpt Log in",
                        "00:47",
                        "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"
                    ),
                    VideoItem(
                        "2",
                        "Çalışma Kitabı ve Çalışma Sayfası Ekle",
                        "10:04",
                        "https://example.com/video2.mp4"
                    ),
                    VideoItem(
                        "3",
                        "Çalışma Sayfalarını Yönetelim",
                        "04:58",
                        "https://example.com/video3.mp4"
                    ),
                    VideoItem(
                        "4",
                        "Hücreleri Yönetelim",
                        "04:25",
                        "https://example.com/video4.mp4"
                    ),
                    VideoItem("5", "Yapı Oluşturalım", "07:09", "https://example.com/video5.mp4"),
                    VideoItem(
                        "6",
                        "Gerçek Hayata Dair Problemlerimizi Çözelim",
                        "06:48",
                        "https://example.com/video6.mp4"
                    )
                )
            ), Course(
                "20",
                "Public Speaking Skills",
                "Enhance your public speaking confidence and skills.",
                "https://via.placeholder.com/150",
                false,
                4.2f,
                29.99f,
                "Kişisel Gelişim",
                listOf(
                    VideoItem(
                        "1",
                        "ChatGpt Log in",
                        "00:47",
                        "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"
                    ),
                    VideoItem(
                        "2",
                        "Çalışma Kitabı ve Çalışma Sayfası Ekle",
                        "10:04",
                        "https://example.com/video2.mp4"
                    ),
                    VideoItem(
                        "3",
                        "Çalışma Sayfalarını Yönetelim",
                        "04:58",
                        "https://example.com/video3.mp4"
                    ),
                    VideoItem(
                        "4",
                        "Hücreleri Yönetelim",
                        "04:25",
                        "https://example.com/video4.mp4"
                    ),
                    VideoItem("5", "Yapı Oluşturalım", "07:09", "https://example.com/video5.mp4"),
                    VideoItem(
                        "6",
                        "Gerçek Hayata Dair Problemlerimizi Çözelim",
                        "06:48",
                        "https://example.com/video6.mp4"
                    )
                )
            ), Course(
                "21",
                "Digital Marketing Fundamentals",
                "Understand the basics of digital marketing.",
                "https://via.placeholder.com/150",
                false,
                4.8f,
                79.99f,
                "Pazarlama",
                listOf(
                    VideoItem(
                        "1",
                        "ChatGpt Log in",
                        "00:47",
                        "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"
                    ),
                    VideoItem(
                        "2",
                        "Çalışma Kitabı ve Çalışma Sayfası Ekle",
                        "10:04",
                        "https://example.com/video2.mp4"
                    ),
                    VideoItem(
                        "3",
                        "Çalışma Sayfalarını Yönetelim",
                        "04:58",
                        "https://example.com/video3.mp4"
                    ),
                    VideoItem(
                        "4",
                        "Hücreleri Yönetelim",
                        "04:25",
                        "https://example.com/video4.mp4"
                    ),
                    VideoItem("5", "Yapı Oluşturalım", "07:09", "https://example.com/video5.mp4"),
                    VideoItem(
                        "6",
                        "Gerçek Hayata Dair Problemlerimizi Çözelim",
                        "06:48",
                        "https://example.com/video6.mp4"
                    )
                )
            ), Course(
                "22",
                "Search Engine Optimization (SEO)",
                "Improve website ranking with SEO techniques.",
                "https://via.placeholder.com/150",
                false,
                4.6f,
                39.99f,
                "Pazarlama",
                listOf(
                    VideoItem(
                        "1",
                        "ChatGpt Log in",
                        "00:47",
                        "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"
                    ),
                    VideoItem(
                        "2",
                        "Çalışma Kitabı ve Çalışma Sayfası Ekle",
                        "10:04",
                        "https://example.com/video2.mp4"
                    ),
                    VideoItem(
                        "3",
                        "Çalışma Sayfalarını Yönetelim",
                        "04:58",
                        "https://example.com/video3.mp4"
                    ),
                    VideoItem(
                        "4",
                        "Hücreleri Yönetelim",
                        "04:25",
                        "https://example.com/video4.mp4"
                    ),
                    VideoItem("5", "Yapı Oluşturalım", "07:09", "https://example.com/video5.mp4"),
                    VideoItem(
                        "6",
                        "Gerçek Hayata Dair Problemlerimizi Çözelim",
                        "06:48",
                        "https://example.com/video6.mp4"
                    )
                )
            ), Course(
                "23",
                "Blockchain Basics",
                "Learn the fundamentals of blockchain technology.",
                "https://via.placeholder.com/150",
                false,
                4.5f,
                69.99f,
                "BT ve Yazılım",
                listOf(
                    VideoItem(
                        "1",
                        "ChatGpt Log in",
                        "00:47",
                        "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"
                    ),
                    VideoItem(
                        "2",
                        "Çalışma Kitabı ve Çalışma Sayfası Ekle",
                        "10:04",
                        "https://example.com/video2.mp4"
                    ),
                    VideoItem(
                        "3",
                        "Çalışma Sayfalarını Yönetelim",
                        "04:58",
                        "https://example.com/video3.mp4"
                    ),
                    VideoItem(
                        "4",
                        "Hücreleri Yönetelim",
                        "04:25",
                        "https://example.com/video4.mp4"
                    ),
                    VideoItem("5", "Yapı Oluşturalım", "07:09", "https://example.com/video5.mp4"),
                    VideoItem(
                        "6",
                        "Gerçek Hayata Dair Problemlerimizi Çözelim",
                        "06:48",
                        "https://example.com/video6.mp4"
                    )
                )
            ), Course(
                "24",
                "Introduction to Cryptocurrencies",
                "Understand how cryptocurrencies work.",
                "https://via.placeholder.com/150",
                false,
                4.3f,
                34.99f,
                "BT ve Yazılım",
                listOf(
                    VideoItem(
                        "1",
                        "ChatGpt Log in",
                        "00:47",
                        "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"
                    ),
                    VideoItem(
                        "2",
                        "Çalışma Kitabı ve Çalışma Sayfası Ekle",
                        "10:04",
                        "https://example.com/video2.mp4"
                    ),
                    VideoItem(
                        "3",
                        "Çalışma Sayfalarını Yönetelim",
                        "04:58",
                        "https://example.com/video3.mp4"
                    ),
                    VideoItem(
                        "4",
                        "Hücreleri Yönetelim",
                        "04:25",
                        "https://example.com/video4.mp4"
                    ),
                    VideoItem("5", "Yapı Oluşturalım", "07:09", "https://example.com/video5.mp4"),
                    VideoItem(
                        "6",
                        "Gerçek Hayata Dair Problemlerimizi Çözelim",
                        "06:48",
                        "https://example.com/video6.mp4"
                    )
                )
            ), Course(
                "25",
                "Graphic Design for Beginners",
                "Learn how to create stunning graphics.",
                "https://via.placeholder.com/150",
                false,
                4.4f,
                29.99f,
                "Tasarım ve Yaratıcılık",
                listOf(
                    VideoItem(
                        "1",
                        "ChatGpt Log in",
                        "00:47",
                        "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"
                    ),
                    VideoItem(
                        "2",
                        "Çalışma Kitabı ve Çalışma Sayfası Ekle",
                        "10:04",
                        "https://example.com/video2.mp4"
                    ),
                    VideoItem(
                        "3",
                        "Çalışma Sayfalarını Yönetelim",
                        "04:58",
                        "https://example.com/video3.mp4"
                    ),
                    VideoItem(
                        "4",
                        "Hücreleri Yönetelim",
                        "04:25",
                        "https://example.com/video4.mp4"
                    ),
                    VideoItem("5", "Yapı Oluşturalım", "07:09", "https://example.com/video5.mp4"),
                    VideoItem(
                        "6",
                        "Gerçek Hayata Dair Problemlerimizi Çözelim",
                        "06:48",
                        "https://example.com/video6.mp4"
                    )
                )
            ), Course(
                "26",
                "Social Media Marketing",
                "Grow your brand with social media strategies.",
                "https://via.placeholder.com/150",
                false,
                4.9f,
                49.99f,
                "Pazarlama",
                listOf(
                    VideoItem(
                        "1",
                        "ChatGpt Log in",
                        "00:47",
                        "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"
                    ),
                    VideoItem(
                        "2",
                        "Çalışma Kitabı ve Çalışma Sayfası Ekle",
                        "10:04",
                        "https://example.com/video2.mp4"
                    ),
                    VideoItem(
                        "3",
                        "Çalışma Sayfalarını Yönetelim",
                        "04:58",
                        "https://example.com/video3.mp4"
                    ),
                    VideoItem(
                        "4",
                        "Hücreleri Yönetelim",
                        "04:25",
                        "https://example.com/video4.mp4"
                    ),
                    VideoItem("5", "Yapı Oluşturalım", "07:09", "https://example.com/video5.mp4"),
                    VideoItem(
                        "6",
                        "Gerçek Hayata Dair Problemlerimizi Çözelim",
                        "06:48",
                        "https://example.com/video6.mp4"
                    )
                )
            ), Course(
                "27",
                "Introduction to SQL",
                "Master database management with SQL.",
                "https://via.placeholder.com/150",
                false,
                4.2f,
                19.99f,
                "BT ve Yazılım",
                listOf(
                    VideoItem(
                        "1",
                        "ChatGpt Log in",
                        "00:47",
                        "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"
                    ),
                    VideoItem(
                        "2",
                        "Çalışma Kitabı ve Çalışma Sayfası Ekle",
                        "10:04",
                        "https://example.com/video2.mp4"
                    ),
                    VideoItem(
                        "3",
                        "Çalışma Sayfalarını Yönetelim",
                        "04:58",
                        "https://example.com/video3.mp4"
                    ),
                    VideoItem(
                        "4",
                        "Hücreleri Yönetelim",
                        "04:25",
                        "https://example.com/video4.mp4"
                    ),
                    VideoItem("5", "Yapı Oluşturalım", "07:09", "https://example.com/video5.mp4"),
                    VideoItem(
                        "6",
                        "Gerçek Hayata Dair Problemlerimizi Çözelim",
                        "06:48",
                        "https://example.com/video6.mp4"
                    )
                )
            ), Course(
                "28",
                "Personal Finance Management",
                "Control your finances with this guide.",
                "https://via.placeholder.com/150",
                false,
                4.8f,
                29.99f,
                "Finans ve Muhasebe",
                listOf(
                    VideoItem(
                        "1",
                        "ChatGpt Log in",
                        "00:47",
                        "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"
                    ),
                    VideoItem(
                        "2",
                        "Çalışma Kitabı ve Çalışma Sayfası Ekle",
                        "10:04",
                        "https://example.com/video2.mp4"
                    ),
                    VideoItem(
                        "3",
                        "Çalışma Sayfalarını Yönetelim",
                        "04:58",
                        "https://example.com/video3.mp4"
                    ),
                    VideoItem(
                        "4",
                        "Hücreleri Yönetelim",
                        "04:25",
                        "https://example.com/video4.mp4"
                    ),
                    VideoItem("5", "Yapı Oluşturalım", "07:09", "https://example.com/video5.mp4"),
                    VideoItem(
                        "6",
                        "Gerçek Hayata Dair Problemlerimizi Çözelim",
                        "06:48",
                        "https://example.com/video6.mp4"
                    )
                )
            ), Course(
                "29",
                "Career Development Essentials",
                "Plan and achieve your career goals.",
                "https://via.placeholder.com/150",
                false,
                4.4f,
                39.99f,
                "Kişisel Gelişim",
                listOf(
                    VideoItem(
                        "1",
                        "ChatGpt Log in",
                        "00:47",
                        "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"
                    ),
                    VideoItem(
                        "2",
                        "Çalışma Kitabı ve Çalışma Sayfası Ekle",
                        "10:04",
                        "https://example.com/video2.mp4"
                    ),
                    VideoItem(
                        "3",
                        "Çalışma Sayfalarını Yönetelim",
                        "04:58",
                        "https://example.com/video3.mp4"
                    ),
                    VideoItem(
                        "4",
                        "Hücreleri Yönetelim",
                        "04:25",
                        "https://example.com/video4.mp4"
                    ),
                    VideoItem("5", "Yapı Oluşturalım", "07:09", "https://example.com/video5.mp4"),
                    VideoItem(
                        "6",
                        "Gerçek Hayata Dair Problemlerimizi Çözelim",
                        "06:48",
                        "https://example.com/video6.mp4"
                    )
                )
            ), Course(
                "30",
                "Stress Management Techniques",
                "Learn techniques to manage and reduce stress.",
                "https://via.placeholder.com/150",
                false,
                4.6f,
                19.99f,
                "Kişisel Gelişim",
                listOf(
                    VideoItem(
                        "1",
                        "ChatGpt Log in",
                        "00:47",
                        "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"
                    ),
                    VideoItem(
                        "2",
                        "Çalışma Kitabı ve Çalışma Sayfası Ekle",
                        "10:04",
                        "https://example.com/video2.mp4"
                    ),
                    VideoItem(
                        "3",
                        "Çalışma Sayfalarını Yönetelim",
                        "04:58",
                        "https://example.com/video3.mp4"
                    ),
                    VideoItem(
                        "4",
                        "Hücreleri Yönetelim",
                        "04:25",
                        "https://example.com/video4.mp4"
                    ),
                    VideoItem("5", "Yapı Oluşturalım", "07:09", "https://example.com/video5.mp4"),
                    VideoItem(
                        "6",
                        "Gerçek Hayata Dair Problemlerimizi Çözelim",
                        "06:48",
                        "https://example.com/video6.mp4"
                    )
                )
            ), Course(
                "31",
                "Effective Business Communication",
                "Enhance your communication skills for the workplace.",
                "https://via.placeholder.com/150",
                false,
                4.7f,
                29.99f,
                "İş",
                listOf(
                    VideoItem(
                        "1",
                        "ChatGpt Log in",
                        "00:47",
                        "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"
                    ),
                    VideoItem(
                        "2",
                        "Çalışma Kitabı ve Çalışma Sayfası Ekle",
                        "10:04",
                        "https://example.com/video2.mp4"
                    ),
                    VideoItem(
                        "3",
                        "Çalışma Sayfalarını Yönetelim",
                        "04:58",
                        "https://example.com/video3.mp4"
                    ),
                    VideoItem(
                        "4",
                        "Hücreleri Yönetelim",
                        "04:25",
                        "https://example.com/video4.mp4"
                    ),
                    VideoItem("5", "Yapı Oluşturalım", "07:09", "https://example.com/video5.mp4"),
                    VideoItem(
                        "6",
                        "Gerçek Hayata Dair Problemlerimizi Çözelim",
                        "06:48",
                        "https://example.com/video6.mp4"
                    )
                )
            )
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
