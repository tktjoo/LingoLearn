package com.linguaflow.app.data.remote.emailjs

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EmailJsRequest(
    val service_id: String,
    val template_id: String,
    val user_id: String,
    val template_params: Map<String, String>
)
