package com.swordhealth.catsapp.utils

import okhttp3.Interceptor
import okhttp3.Response

class RetrofitInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        request
            .newBuilder()
            .addHeader("x-api-key", Constants.API_KEY)
            .build()
        val response = chain.proceed(request)
        return response
    }
}