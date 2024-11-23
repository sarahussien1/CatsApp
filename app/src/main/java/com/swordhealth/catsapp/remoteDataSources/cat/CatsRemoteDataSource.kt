package com.swordhealth.catsapp.remoteDataSources.cat

import android.content.Context
import com.swordhealth.catsapp.R
import com.swordhealth.catsapp.models.Cat
import com.swordhealth.catsapp.services.CatsApiService
import com.swordhealth.catsapp.utils.NetworkConnectivity
import com.swordhealth.catsapp.utils.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CatsRemoteDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
    private val networkConnectivity: NetworkConnectivity,
    private val catsApiService: CatsApiService
) : CatsRemoteDataContract {
    override suspend fun getCats(
        size: String?,
        hasBreeds: Boolean?,
        order: String?,
        page: Int,
        limit: Int
    ): Resource<List<Cat>> {
        if (!networkConnectivity.isConnected()) {
            return Resource.DataError(context.getString(R.string.no_internet_error))
        }
        val response = catsApiService.getCats(size, hasBreeds, order, page, limit)
        return if (response.isSuccessful) {
            Resource.Success(response.body() as List<Cat>)
        } else {
            Resource.DataError(response.message() ?: context.getString(R.string.data_error))
        }
    }
}