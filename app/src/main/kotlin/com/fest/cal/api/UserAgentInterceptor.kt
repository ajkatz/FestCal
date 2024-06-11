package com.fest.cal.api

import okhttp3.Interceptor
import okhttp3.Response

class UserAgentInterceptor : Interceptor  {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("User-Agent", "FestCalTest/1.0 ( ajkatz09@gmail.com )")
            .build()
        return chain.proceed(request)
    }
}