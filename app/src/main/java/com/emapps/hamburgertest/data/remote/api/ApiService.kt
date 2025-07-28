package com.emapps.hamburgertest.data.remote.api

import com.emapps.hamburgertest.data.remote.responses.RestaurantMenuResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface ApiService {

    @Headers("Accept: application/json")
    @GET("6882874df7e7a370d1ed6cc1/latest")
    suspend fun fetchMenu(
        @Header("X-Master-Key") authorization: String,
    ): Response<RestaurantMenuResponse>
}