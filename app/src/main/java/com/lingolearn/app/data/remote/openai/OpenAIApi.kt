package com.lingolearn.app.data.remote.openai

import com.lingolearn.app.data.remote.openai.model.ChatCompletionRequest
import com.lingolearn.app.data.remote.openai.model.ChatCompletionResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenAIApi {
    @Headers("Content-Type: application/json")
    @POST("v1/chat/completions")
    suspend fun getChatCompletion(
        @Body request: ChatCompletionRequest
    ): ChatCompletionResponse
}
