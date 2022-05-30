package crabster.rudakov.sberschoollesson33hwk.data

import crabster.rudakov.sberschoollesson33hwk.data.repository.FakeRepositoryAnnotation
import crabster.rudakov.sberschoollesson33hwk.data.repository.FakeWeatherRepositoryImpl
import crabster.rudakov.sberschoollesson33hwk.data.repository.WeatherRepository
import crabster.rudakov.sberschoollesson33hwk.data.repository.WeatherRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class Module {

    @Provides
    @Singleton
    fun provideRepository(): WeatherRepository {
        return WeatherRepositoryImpl()
    }

    @Provides
    @Singleton
    @FakeRepositoryAnnotation
    fun provideFakeRepository(): WeatherRepository {
        return FakeWeatherRepositoryImpl()
    }

}