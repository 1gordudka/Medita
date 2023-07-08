package com.igordudka.medita.dagger

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.igordudka.medita.data.AppPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

private const val APP_PREFERENCES = "app_preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = APP_PREFERENCES
)

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideAppPreferencesRepository(@ApplicationContext context: Context) : AppPreferencesRepository {
        return AppPreferencesRepository(context.dataStore)
    }
}