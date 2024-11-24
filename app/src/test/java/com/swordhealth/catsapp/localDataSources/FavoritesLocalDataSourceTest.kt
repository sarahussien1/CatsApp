package com.swordhealth.catsapp.localDataSources

import android.content.Context
import com.swordhealth.catsapp.R
import com.swordhealth.catsapp.daos.FavoriteItemDao
import com.swordhealth.catsapp.localDataSources.favoriteItem.FavoritesLocalDataSource
import com.swordhealth.catsapp.models.AddToFavRequest
import com.swordhealth.catsapp.models.FavoriteItem
import com.swordhealth.catsapp.utils.Constants
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.Date

class FavoritesLocalDataSourceTest {
    private lateinit var localDataSource: FavoritesLocalDataSource
    @Mock
    private lateinit var context: Context
    @Mock
    private lateinit var favoritesDao: FavoriteItemDao

    private val dataError = "Error has been occurred"
    private val subId = Constants.SUB_ID
    private val favoritesList = listOf(FavoriteItem(232500303, subId = subId, imageId = "asf2", createdAt = Date()))
    private val addToFavRequest = AddToFavRequest(imageId = "1234", subId)

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this) // Initialize Mockito
        localDataSource =
            FavoritesLocalDataSource(favoritesDao, context) // Inject mock dao
    }

    @Test
    fun `getFavorites returns favoritesList on successful response`() = runTest {
        // Given
        val mockFavorites =
            listOf(FavoriteItem(232500303, subId = subId, imageId = "asf2", createdAt = Date()))
        Mockito.`when`(favoritesDao.getAllFavorites(subId))
            .thenReturn(mockFavorites)
        // When
        val result = localDataSource.getFavorites(subId)
        // Then
        assertEquals(mockFavorites, result.data)
    }

    @Test
    fun `getFavorites returns failure response`() = runTest {
        // Given
        Mockito.`when`(favoritesDao.getAllFavorites(subId)).then { throw Exception() }
        Mockito.`when`(context.getString(R.string.data_error)).thenReturn(dataError)
        // When
        val result = localDataSource.getFavorites(subId)
        // Then
        assertEquals(result.errorMessage, dataError)
    }

    @Test
    fun `upsertFavorites returns successful response`() = runTest {
        // When
        val result = localDataSource.upsertAll(favoritesList)
        // Then
        assertEquals(true, result.data)
    }

    @Test
    fun `upsertFavorites returns failure response`() = runTest {
        // Given
        Mockito.`when`(favoritesDao.upsertFavorites(favoritesList)).then { throw Exception() }
        Mockito.`when`(context.getString(R.string.data_error)).thenReturn(dataError)
        // When
        val result = localDataSource.upsertAll(favoritesList)
        // Then
        assertEquals(result.errorMessage, dataError)
    }

    @Test
    fun `addToFavorite returns successful response`() = runTest {
        // When
        val result = localDataSource.addToFavorite(123456789, addToFavRequest)
        // Then
        assertEquals("SUCCESS", result.data?.message)
    }

    @Test
    fun `removeFromFavorites returns success response`() = runTest {
        // When
        val result = localDataSource.removeFromFavorites(123456789)
        // Then
        assertEquals("SUCCESS", result.data?.message)
    }

    @Test
    fun `removeFromFavorites returns failure response`() = runTest {
        // Given
        Mockito.`when`(favoritesDao.removeFromFavorite(123456789)).then { throw Exception()}
        Mockito.`when`(context.getString(R.string.data_error)).thenReturn(dataError)
        // When
        val result = localDataSource.removeFromFavorites(123456789)
        // Then
        assertEquals(result.errorMessage, dataError)
    }


}