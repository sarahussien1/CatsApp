

package com.swordhealth.catsapp.localDataSources

import android.content.Context
import com.swordhealth.catsapp.R
import com.swordhealth.catsapp.daos.CatDao
import com.swordhealth.catsapp.localDataSources.cat.CatsLocalDataSource
import com.swordhealth.catsapp.models.Cat
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response

class CatsLocalDataSourceTest {
    private lateinit var localDataSource: CatsLocalDataSource
    @Mock
    private lateinit var context: Context
    @Mock
    private lateinit var catDao: CatDao

    private val size = "med"
    private val hasBreeds = true
    private val order = "DESC"
    private val page = 0
    private val limit = 10
    private val dataError = "Error has been occurred"

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this) // Initialize Mockito
        localDataSource =
            CatsLocalDataSource(catDao, context) // Inject mock dao
    }

    @Test
    fun `getCats returns catList on successful response`() = runTest {
        // Given
        val mockCats =
            listOf(Cat("1234", url = "http://www.vetstreet.com/cats/aegean-cat", breeds = listOf()))
        Mockito.`when`(catDao.getAllCats())
            .thenReturn(mockCats)
        // When
        val result = localDataSource.getCats(size, hasBreeds, order, page, limit)
        // Then
        assertEquals(mockCats, result.data)
    }

    @Test
    fun `getCats returns failure response`() = runTest {
        // Given
        Mockito.`when`(catDao.getAllCats()).then { throw Exception() }
        Mockito.`when`(context.getString(R.string.data_error)).thenReturn(dataError)
        // When
        val result = localDataSource.getCats(size, hasBreeds, order, page, limit)
        // Then
        assertEquals(result.errorMessage, dataError)
    }
}