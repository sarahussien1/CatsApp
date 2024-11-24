package com.swordhealth.catsapp.repositories

import com.swordhealth.catsapp.localDataSources.cat.CatsLocalDataContract
import com.swordhealth.catsapp.models.Cat
import com.swordhealth.catsapp.remoteDataSources.cat.CatsRemoteDataContract
import com.swordhealth.catsapp.repositories.cat.CatRepository
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

@ExperimentalCoroutinesApi
class CatsRepositoryTest {
    private lateinit var catRepository: CatRepository
    @Mock
    private lateinit var catsRemoteDataSource: CatsRemoteDataContract

    @Mock
    private lateinit var catsLocalDataSource: CatsLocalDataContract

    // Create a test dispatcher
    private val ioDispatcher = StandardTestDispatcher()
    private val size = "med"
    private val hasBreeds = true
    private val order = "DESC"
    private val page = 0
    private val limit = 10


    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this) // Initialize Mockito
        Dispatchers.setMain(ioDispatcher)
        catRepository =
            CatRepository(catsRemoteDataSource, catsLocalDataSource, ioDispatcher) // Inject mock data sources
    }

    @Test
    fun `getCats returns catList on successful response`() = runTest {
        val remoteCats = listOf(Cat("1234", url = "http://www.vetstreet.com/cats/aegean-cat", breeds = listOf()))
        val remoteResource = Resource.Success(data = remoteCats)

        Mockito.`when`(catsRemoteDataSource.getCats(size, hasBreeds, order, page, limit)).thenReturn(remoteResource)

        // Act: Collect flow emissions
        val emissions = catRepository.getCats(size, hasBreeds, order, page, limit).toList()

        // Assert: Verify it emits the remote result
        assertEquals(listOf(remoteResource) , emissions)

        // Verify upsert is called with remote data
        verify(catsLocalDataSource).upsertAll(remoteCats)
    }

    @Test
    fun `getCats remote fallback to local returns catList`() = runTest {
        val localCats = listOf(Cat("1234", url = "http://www.vetstreet.com/cats/aegean-cat", breeds = listOf()))
        val localResource = Resource.Success(data = localCats)
        Mockito.`when`(catsRemoteDataSource.getCats(size, hasBreeds, order, page, limit)).thenReturn(Resource.DataError("Bad Request"))
        Mockito.`when`(catsLocalDataSource.getCats(size, hasBreeds, order, page, limit)).thenReturn(localResource)

        // Act: Collect flow emissions
        val emissions = catRepository.getCats(size, hasBreeds, order, page, limit).toList()

        // Assert: Verify it emits the remote result
        assertEquals(listOf(localResource) , emissions)
    }
    @Test
    fun `getCats remote fallback to local fallback to send failure response`() = runTest {
        Mockito.`when`(catsRemoteDataSource.getCats(size, hasBreeds, order, page, limit)).thenReturn(Resource.DataError("Bad Request"))
        Mockito.`when`(catsLocalDataSource.getCats(size, hasBreeds, order, page, limit)).thenReturn(Resource.DataError("An Error has been occured"))

        // Act: Collect flow emissions
        val emissions = catRepository.getCats(size, hasBreeds, order, page, limit).toList()

        // Assert: Verify it emits the remote result
        assertNotNull(emissions.first().errorMessage)
    }


    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()              // Reset the Main dispatcher to the original dispatcher
    }

}