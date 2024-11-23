package com.swordhealth.catsapp.localDataSources.cat

import com.swordhealth.catsapp.models.Cat
import com.swordhealth.catsapp.utils.Resource

interface CatsLocalDataContract {
    suspend fun upsertAll(cats: List<Cat>)
    suspend fun getCats(
        size: String?,
        hasBreeds: Boolean?,
        order: String?,
        page: Int,
        limit: Int
    ): Resource<List<Cat>>
}