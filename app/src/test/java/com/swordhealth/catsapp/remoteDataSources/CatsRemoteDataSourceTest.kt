package com.swordhealth.catsapp.remoteDataSources

import android.content.Context
import com.swordhealth.catsapp.R
import com.swordhealth.catsapp.models.Cat
import com.swordhealth.catsapp.remoteDataSources.cat.CatsRemoteDataSource
import com.swordhealth.catsapp.services.CatsApiService
import com.swordhealth.catsapp.utils.NetworkConnectivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response
import org.junit.Assert.*

@ExperimentalCoroutinesApi
class CatsRemoteDataSourceTest {
    @Mock
    private lateinit var apiService: CatsApiService

    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var networkConnectivity: NetworkConnectivity
    private lateinit var remoteDataSource: CatsRemoteDataSource
    private val size = "med"
    private val hasBreeds = true
    private val order = "DESC"
    private val page = 0
    private val limit = 10

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this) // Initialize Mockito
        remoteDataSource =
            CatsRemoteDataSource(context, networkConnectivity, apiService) // Inject mock apiService
    }

    @Test
    fun `getCats returns catList on successful response`() = runTest {
        // Given
        val mockCats =
            listOf(Cat("1234", url = "http://www.vetstreet.com/cats/aegean-cat", breeds = listOf()))
        Mockito.`when`(apiService.getCats(size, hasBreeds, order, page, limit))
            .thenReturn(Response.success(mockCats))
        Mockito.`when`(networkConnectivity.isConnected()).thenReturn(true)
        // When
        val result = remoteDataSource.getCats(size, hasBreeds, order, page, limit)
        // Then
        assertEquals(mockCats, result.data)
    }

    @Test
    fun `getCats returns message on no internet connection`() = runTest {
        // Given
        val connectionError = "No internet connection"
        Mockito.`when`(networkConnectivity.isConnected()).thenReturn(false)
        Mockito.`when`(context.getString(R.string.no_internet_error)).thenReturn(connectionError)
        // When
        val result = remoteDataSource.getCats(size, hasBreeds, order, page, limit)
        // Then
        assertEquals(result.errorMessage, connectionError)
    }

    @Test
    fun `getCats returns message on failed transaction`() = runTest {
        // Given
        Mockito.`when`(apiService.getCats(size, hasBreeds, order, page, limit))
            .thenReturn(Response.error(400, okhttp3.ResponseBody.create(null, "Bad Request")))
        Mockito.`when`(networkConnectivity.isConnected()).thenReturn(true)
        // When
        val result = remoteDataSource.getCats(size, hasBreeds, order, page, limit)

        // Then
        assertNotNull(result.errorMessage)
    }
}