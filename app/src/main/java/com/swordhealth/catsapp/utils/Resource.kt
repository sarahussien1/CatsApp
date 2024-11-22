package com.swordhealth.catsapp.utils

// A generic class that contains data and status about loading this data.
sealed class Resource<T>(
    private val data: T? = null,
    private val errorMessage: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class DataError<T>(errorMessage: String) : Resource<T>(null, errorMessage)

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is DataError -> "Error[exception=$errorMessage]"
            is Loading<T> -> "Loading"
        }
    }
}
