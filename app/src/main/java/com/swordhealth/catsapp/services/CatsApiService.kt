package com.swordhealth.catsapp.services

import com.swordhealth.catsapp.utils.Constants
import com.swordhealth.catsapp.utils.CatQueryParameters
import com.swordhealth.catsapp.models.Cat
import retrofit2.http.GET
import retrofit2.http.Query

interface CatsApiService {

    @GET(Constants.CATS_URL)
    suspend fun getCats(@Query(CatQueryParameters.SIZE) size: String?,
                        @Query(CatQueryParameters.HAS_BREEDS) hasBreeds: Boolean?,
                        @Query(CatQueryParameters.ORDER) order: String?,
                        @Query(CatQueryParameters.PAGE) page: Int,
                        @Query(CatQueryParameters.LIMIT) limit: Int): List<Cat>

}