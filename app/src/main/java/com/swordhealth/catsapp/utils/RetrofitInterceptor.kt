package com.swordhealth.catsapp.utils

import okhttp3.Interceptor
import okhttp3.Response

class RetrofitInterceptor constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request
            .newBuilder()
            .addHeader("x-api-key", Constants.API_KEY)
            .build()
            val response = chain.proceed(request)
            return response
    }
}