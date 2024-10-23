package com.example.githubviewer.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.FragmentScoped
import io.noties.markwon.Markwon

@Module
@InstallIn(FragmentComponent::class)
object MarkwonModule {

    @FragmentScoped
    @Provides
    fun providesMarkwon(@ApplicationContext context: Context): Markwon {
        return Markwon.create(context)
    }
}