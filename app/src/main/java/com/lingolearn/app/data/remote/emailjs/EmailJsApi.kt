package com.lingolearn.app.data.remote.emailjs

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface EmailJsApi {
    @Headers("Content-Type: application/json")
    @POST("send")
    suspend fun sendEmail(@Body request: EmailJsRequest)
}
