package com.swordhealth.catsapp.repositories

import com.swordhealth.catsapp.localDataSources.favoriteItem.FavoritesLocalDataContract
import com.swordhealth.catsapp.models.AddToFavRequest
import com.swordhealth.catsapp.models.AddToFavResponse
import com.swordhealth.catsapp.models.FavoriteItem
import com.swordhealth.catsapp.models.RemoveFromFavResponse
import com.swordhealth.catsapp.remoteDataSources.favoriteItem.FavoritesRemoteDataContract
import com.swordhealth.catsapp.repositories.favoriteItem.FavoriteRepository
import com.swordhealth.catsapp.utils.Constants
import com.swordhealth.catsapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import java.util.Date

@ExperimentalCoroutinesApi
class FavoritesRepositoryTest {
    private lateinit var favoriteRepository: FavoriteRepository
    @Mock
    private lateinit var favoritesRemoteDataSource: FavoritesRemoteDataContract

    @Mock
    private lateinit var favoritesLocalDataSource: FavoritesLocalDataContract

    // Create a test dispatcher
    private val ioDispatcher = StandardTestDispatcher()



    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this) // Initialize Mockito
        Dispatchers.setMain(ioDispatcher)
        favoriteRepository =
            FavoriteRepository(favoritesRemoteDataSource, favoritesLocalDataSource, ioDispatcher) // Inject mock data sources
    }

    @Test
    fun `getFavorites returns list on successful response`() = runTest {
        val remoteFavs = listOf(FavoriteItem(232500303, subId = Constants.SUB_ID, imageId = "asf2", createdAt = Date()))
        val remoteResource = Resource.Success(data = remoteFavs)

        Mockito.`when`(favoritesRemoteDataSource.getFavorites(Constants.SUB_ID)).thenReturn(remoteResource)

        // Act: Collect flow emissions
        val emissions = favoriteRepository.getFavorites(Constants.SUB_ID).toList()

        // Assert: Verify it emits the remote result
        assertEquals(listOf(remoteResource) , emissions)

        // Verify upsert is called with remote data
        verify(favoritesLocalDataSource).upsertAll(remoteFavs)
    }

    @Test
    fun `getFavorites remote fallback to local returns favList`() = runTest {
        val localFavs = listOf(FavoriteItem(232500303, subId = Constants.SUB_ID, imageId = "asf2", createdAt = Date()))
        val localResource = Resource.Success(data = localFavs)
        Mockito.`when`(favoritesRemoteDataSource.getFavorites(Constants.SUB_ID)).thenReturn(
            Resource.DataError("Bad Request"))
        Mockito.`when`(favoritesLocalDataSource.getFavorites(Constants.SUB_ID)).thenReturn(localResource)

        // Act: Collect flow emissions
        val emissions = favoriteRepository.getFavorites(Constants.SUB_ID).toList()

        // Assert: Verify it emits the remote result
        assertEquals(listOf(localResource) , emissions)
    }
    @Test
    fun `getFavorites remote fallback to local fallback to send failure response`() = runTest {
        Mockito.`when`(favoritesRemoteDataSource.getFavorites(Constants.SUB_ID)).thenReturn(
            Resource.DataError("Bad Request"))
        Mockito.`when`(favoritesLocalDataSource.getFavorites(Constants.SUB_ID)).thenReturn(
            Resource.DataError("An Error has been occured"))

        // Act: Collect flow emissions
        val emissions = favoriteRepository.getFavorites(Constants.SUB_ID).toList()

        // Assert: Verify it emits the remote result
        assertNotNull(emissions.first().errorMessage)
    }

    @Test
    fun `addToFavorite remote success`() = runTest {
        val addToFavResponseMock = AddToFavResponse(message = "SUCCESS", id = 123456789)
        val mockResource = Resource.Success(data = addToFavResponseMock)
        val addToFavRequest = AddToFavRequest(imageId = "1234", Constants.SUB_ID)
        Mockito.`when`(favoritesRemoteDataSource.addToFavorite(addToFavRequest)).thenReturn(mockResource)
        val emissions = favoriteRepository.addToFavorite(addToFavRequest).toList()

        assertEquals(listOf(mockResource) , emissions)
        // Verify upsert is called with remote data
        verify(favoritesLocalDataSource).addToFavorite(123456789, addToFavRequest)
    }

    @Test
    fun `removeFromFavorite remote success`() = runTest {
        val removeFavResponseMock = RemoveFromFavResponse(message = "SUCCESS")
        val mockResource = Resource.Success(data = removeFavResponseMock)
        Mockito.`when`(favoritesRemoteDataSource.removeFromFavorites(123456789)).thenReturn(mockResource)
        val emissions = favoriteRepository.removeFromFavorites(123456789).toList()

        assertEquals(listOf(mockResource) , emissions)
        // Verify upsert is called with remote data
        verify(favoritesLocalDataSource).removeFromFavorites(123456789)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()              // Reset the Main dispatcher to the original dispatcher
    }
}