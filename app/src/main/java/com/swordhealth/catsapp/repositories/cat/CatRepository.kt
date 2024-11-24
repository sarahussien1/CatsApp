package com.swordhealth.catsapp.repositories.cat

import com.swordhealth.catsapp.localDataSources.cat.CatsLocalDataContract
import com.swordhealth.catsapp.models.Cat
import com.swordhealth.catsapp.remoteDataSources.cat.CatsRemoteDataContract
import com.swordhealth.catsapp.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class CatRepository @Inject constructor(
    private val catsRemoteDataSource: CatsRemoteDataContract,
    private val catsLocalDataSource: CatsLocalDataContract,
    private val ioDispatcher: CoroutineContext
): CatRepositoryContract {
    override fun getCats(
        size: String?,
        hasBreeds: Boolean?,
        order: String?,
        page: Int,
        limit: Int
    ): Flow<Resource<List<Cat>>> {
        return flow {
            val remoteResult = catsRemoteDataSource.getCats(size,hasBreeds,order,page,limit)
            if (remoteResult.errorMessage != null) { // fallback to local
                val localResult = catsLocalDataSource.getCats(size,hasBreeds,order,page,limit)
                if (localResult.data is List<Cat>) {
                    emit(localResult)
                }
            }
            emit(remoteResult)
            if (!remoteResult.data.isNullOrEmpty()) {
                catsLocalDataSource.upsertAll(remoteResult.data)
            }
        }.flowOn(ioDispatcher)
    }

}