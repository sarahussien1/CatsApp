package com.swordhealth.catsapp.services

import com.swordhealth.catsapp.models.AddToFavRequest
import com.swordhealth.catsapp.models.AddToFavResponse
import com.swordhealth.catsapp.models.FavoriteItem
import com.swordhealth.catsapp.models.RemoveFromFavResponse
import com.swordhealth.catsapp.utils.Constants
import com.swordhealth.catsapp.utils.FavoriteItemQueryParameters
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FavoritesApiService {
    @GET(Constants.FAVORITES_URL)
    suspend fun getFavorites(
        @Query(FavoriteItemQueryParameters.SUB_ID)
        subId: String
    ): Response<List<FavoriteItem>>

    @POST(Constants.FAVORITES_URL)
    suspend fun addToFavorites(@Body favoriteRequest: AddToFavRequest): Response<AddToFavResponse>

    @DELETE("${Constants.FAVORITES_URL}/{${Constants.ID_PATH}}")
    suspend fun removeFromFavorites(@Path(Constants.ID_PATH) favoriteId: Long): Response<RemoveFromFavResponse>
}