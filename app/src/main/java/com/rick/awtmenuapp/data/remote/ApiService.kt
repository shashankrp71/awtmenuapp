package com.rick.awtmenuapp.data.remote

import com.rick.awtmenuapp.data.remote.model.NavigationResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET(ApiConstants.ENDPOINT)
    suspend fun getNavigationDetails(
        @Query("restApi") restApi: String = "Sesapi",
        @Query("sesapi_platform") platform: Int = 1,
        @Query("auth_token") token: String = ApiConstants.AUTH_TOKEN
    ): NavigationResponse
}