package com.swordhealth.catsapp.localDataSources.cat

import android.content.Context
import com.swordhealth.catsapp.R
import com.swordhealth.catsapp.daos.CatDao
import com.swordhealth.catsapp.models.Cat
import com.swordhealth.catsapp.utils.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CatsLocalDataSource @Inject constructor(
    private val catsDao: CatDao,
    @ApplicationContext private val context: Context,
) : CatsLocalDataContract {
    override suspend fun upsertAll(cats: List<Cat>) {
        return try {
            catsDao.upsertCats(cats)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override suspend fun getCats(
        size: String?,
        hasBreeds: Boolean?,
        order: String?,
        page: Int,
        limit: Int
    ): Resource<List<Cat>> {
        return try {
            Resource.Success(catsDao.getAllCats())
        } catch (e: Exception) {
            Resource.DataError(context.getString(R.string.data_error))
        }
    }
}