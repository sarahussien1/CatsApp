package com.swordhealth.catsapp.remoteDataSources

import android.content.Context
import com.swordhealth.catsapp.R
import com.swordhealth.catsapp.models.AddToFavRequest
import com.swordhealth.catsapp.models.AddToFavResponse
import com.swordhealth.catsapp.models.FavoriteItem
import com.swordhealth.catsapp.models.RemoveFromFavResponse
import com.swordhealth.catsapp.remoteDataSources.favoriteItem.FavoritesRemoteDataSource
import com.swordhealth.catsapp.services.FavoritesApiService
import com.swordhealth.catsapp.utils.NetworkConnectivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response
import java.util.Date

@ExperimentalCoroutinesApi
class FavoritesRemoteDataSourceTest {
    @Mock
    private lateinit var apiService: FavoritesApiService

    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var networkConnectivity: NetworkConnectivity
    private lateinit var remoteDataSource: FavoritesRemoteDataSource
    private val subId = "my-user-1234"
    private val addToFavRequest = AddToFavRequest(imageId = "1234", subId)
    private val favoriteID = 123456789L

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this) // Initialize Mockito
        remoteDataSource =
            FavoritesRemoteDataSource(
                context,
                networkConnectivity,
                apiService
            ) // Inject mock apiService
    }

    @Test
    fun `getFavorites returns catList on successful response`() = runTest {
        // Given
        val mockFavorites =
            listOf(FavoriteItem(232500303, subId = subId, imageId = "asf2", createdAt = Date()))
        Mockito.`when`(apiService.getFavorites(subId))
            .thenReturn(Response.success(mockFavorites))
        Mockito.`when`(networkConnectivity.isConnected()).thenReturn(true)
        // When
        val result = remoteDataSource.getFavorites(subId)
        // Then
        assertEquals(mockFavorites, result.data)
    }

    @Test
    fun `getFavorites returns message on no internet connection`() = runTest {
        // Given
        val connectionError = "No internet connection"
        Mockito.`when`(networkConnectivity.isConnected()).thenReturn(false)
        Mockito.`when`(context.getString(R.string.no_internet_error)).thenReturn(connectionError)
        // When
        val result = remoteDataSource.getFavorites(subId)
        // Then
        assertEquals(result.errorMessage, connectionError)
    }

    @Test
    fun `getFavorites returns message on failed transaction`() = runTest {
        // Given
        Mockito.`when`(apiService.getFavorites(subId))
            .thenReturn(Response.error(400, okhttp3.ResponseBody.create(null, "Bad Request")))
        Mockito.`when`(networkConnectivity.isConnected()).thenReturn(true)
        // When
        val result = remoteDataSource.getFavorites(subId)

        // Then
        assertNotNull(result.errorMessage)
    }

    @Test
    fun `addToFavorites returns catList on successful response`() = runTest {
        // Given
        val mockAddToFavResponse =
            AddToFavResponse(message = "SUCCESS", id = 123456789)
        Mockito.`when`(apiService.addToFavorites(addToFavRequest))
            .thenReturn(Response.success(mockAddToFavResponse))
        Mockito.`when`(networkConnectivity.isConnected()).thenReturn(true)
        // When
        val result = remoteDataSource.addToFavorite(addToFavRequest)
        // Then
        assertEquals(mockAddToFavResponse, result.data)
    }

    @Test
    fun `addToFavorites returns message on no internet connection`() = runTest {
        // Given
        val connectionError = "No internet connection"
        Mockito.`when`(networkConnectivity.isConnected()).thenReturn(false)
        Mockito.`when`(context.getString(R.string.no_internet_error)).thenReturn(connectionError)
        // When
        val result = remoteDataSource.addToFavorite(addToFavRequest)
        // Then
        assertEquals(result.errorMessage, connectionError)
    }

    @Test
    fun `addToFavorites returns message on failed transaction`() = runTest {
        // Given
        Mockito.`when`(apiService.addToFavorites(addToFavRequest))
            .thenReturn(Response.error(400, okhttp3.ResponseBody.create(null, "Bad Request")))
        Mockito.`when`(networkConnectivity.isConnected()).thenReturn(true)
        // When
        val result = remoteDataSource.addToFavorite(addToFavRequest)
        // Then
        assertNotNull(result.errorMessage)
    }
    @Test
    fun `removeFromFavorites returns catList on successful response`() = runTest {
        // Given
        val mockRemoveFromFavResponse =
            RemoveFromFavResponse(message = "SUCCESS")
        Mockito.`when`(apiService.removeFromFavorites(favoriteID))
            .thenReturn(Response.success(mockRemoveFromFavResponse))
        Mockito.`when`(networkConnectivity.isConnected()).thenReturn(true)
        // When
        val result = remoteDataSource.removeFromFavorites(favoriteID)
        // Then
        assertEquals(mockRemoveFromFavResponse, result.data)
    }

    @Test
    fun `removeFromFavorites returns message on no internet connection`() = runTest {
        // Given
        val connectionError = "No internet connection"
        Mockito.`when`(networkConnectivity.isConnected()).thenReturn(false)
        Mockito.`when`(context.getString(R.string.no_internet_error)).thenReturn(connectionError)
        // When
        val result = remoteDataSource.removeFromFavorites(favoriteID)
        // Then
        assertEquals(result.errorMessage, connectionError)
    }

    @Test
    fun `removeFromFavorites returns message on failed transaction`() = runTest {
        // Given
        Mockito.`when`(apiService.removeFromFavorites(favoriteID))
            .thenReturn(Response.error(400, okhttp3.ResponseBody.create(null, "Bad Request")))
        Mockito.`when`(networkConnectivity.isConnected()).thenReturn(true)
        // When
        val result = remoteDataSource.removeFromFavorites(favoriteID)
        // Then
        assertNotNull(result.errorMessage)
    }
}