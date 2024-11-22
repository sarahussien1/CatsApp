package com.swordhealth.catsapp.remoteDataSources.cat

import com.swordhealth.catsapp.models.Cat
import com.swordhealth.catsapp.utils.Resource

interface CatsDataSource {
    suspend fun getCats(
        size: String?,
        hasBreeds: Boolean?,
        order: String?,
        page: Int,
        limit: Int
    ): Resource<List<Cat>>
}