import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class GptApiClient(
    private val apiKey: String,
    var model: String = "gpt-3.5-turbo",
    var apiUrl: String = "https://api.openai.com/v1/chat/completions",
    var temperature: Double = 0.5,
    var systemPrompt: String = "You are a helpful assistant.",
    responseTimeoutMinutes: Long = 5L,
) {
    private val objectMapper = ObjectMapper()
    private val client = OkHttpClient.Builder()
        .callTimeout(responseTimeoutMinutes, java.util.concurrent.TimeUnit.MINUTES)
        .build()

    fun request(prompt: String): String {
        val messages = listOf(
            mapOf("role" to "system", "content" to systemPrompt),
            mapOf("role" to "user", "content" to prompt)
        )

        val parameters = mapOf(
            "model" to model,
            "messages" to messages,
            "temperature" to temperature
        )

        val jsonBody = objectMapper.writeValueAsString(parameters)
        val request = Request.Builder()
            .url(apiUrl)
            .addHeader("Authorization", "Bearer $apiKey")
            .post(jsonBody.toRequestBody("application/json".toMediaType()))
            .build()

        val response = client.newCall(request).execute()

        return if (response.isSuccessful) {
            val responseBody = response.body.string()
            parseResponse(responseBody)
        } else {
            "{}"
        }
    }

    private fun parseResponse(jsonResponse: String): String {
        return objectMapper.readTree(jsonResponse)
            ?.path("choices")
            ?.get(0)
            ?.path("message")
            ?.path("content")
            ?.asText("") ?: ""
    }

    private fun parseTokens(jsonResponse: String): String {
        return ObjectMapper().readTree(jsonResponse)
            ?.path("usage")
            ?.path("total_tokens")
            ?.asText("") ?: ""
    }
}
