package com.android.learnconnect.di

import android.content.Context
import com.android.learnconnect.data.LearnConnectDataSource
import com.android.learnconnect.data.LearnConnectRepository
import com.android.learnconnect.domain.mockdata.fake.FakeAssetManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
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
    fun provideCoursesDataSource(json: Json, fakeAssetManager: FakeAssetManager): LearnConnectRepository =
        LearnConnectDataSource(json, fakeAssetManager)

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth =
        FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideGlideInstance(
        @ApplicationContext context: Context
    ): RequestManager {
        return Glide.with(context)
    }
}
