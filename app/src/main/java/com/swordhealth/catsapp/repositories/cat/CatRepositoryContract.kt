package com.swordhealth.catsapp.repositories.cat

import com.swordhealth.catsapp.models.Cat
import com.swordhealth.catsapp.utils.Resource
import kotlinx.coroutines.flow.Flow

interface CatRepositoryContract {
    fun getCats(
        size: String?,
        hasBreeds: Boolean?,
        order: String?,
        page: Int,
        limit: Int
    ): Flow<Resource<List<Cat>>>
}