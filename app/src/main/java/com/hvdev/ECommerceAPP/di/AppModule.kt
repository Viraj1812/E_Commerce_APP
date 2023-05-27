package com.hvdev.ECommerceAPP.di

import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module //tell dagger hilt that this is a module
/*
    @InstallIn specify the life of dependencies inside the module
    we have different option in this
    @InstallIn(SingletonComponent::class) : all the dependencies inside this module will stay alive
    as long as the app is alive.
    @InstallIn(ActivityComponent::class) : this will only live as long as a specific activity dies
    @InstallIn(ServiceComponent::class) : this will just live with the service
    @InstallIn(FragmentComponent::class)
*/
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

}