package com.github.valikprogrammer.intellijidepluginjetbrainshackathon2025.services

import com.intellij.openapi.components.Service
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Service
class OpenAIService {

    @Serializable data class OpenAIRequest(val model: String, val messages: List<Message>)
    @Serializable data class Message(val role: String, val content: String)
    @Serializable data class OpenAIResponse(val choices: List<Choice>)
    @Serializable data class Choice(val message: Message)

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
        engine { requestTimeout = 30_000 }
    }

    suspend fun sendTestRequest(apiKey: String, apiUrl: String): String {
        val testPrompt = "Evaluate my code: class Solution {\n" +
                "public:\n" +
                "    vector<int> sumZero(int n) {\n" +
                "\n" +
                "        vector<int> ans(n);\n" +
                "\n" +
                "        for (int i = 1; i < n; i += 2) {\n" +
                "            ans[i] = i;\n" +
                "            ans[i-1] = -i;\n" +
                "        }\n" +
                "\n" +
                "        return ans;\n" +
                "        \n" +
                "    }\n" +
                "};"

        return try {
            val response: OpenAIResponse = client.post(apiUrl) {
                header(HttpHeaders.Authorization, "Bearer $apiKey")
                contentType(ContentType.Application.Json)
                setBody(OpenAIRequest("gpt-3.5-turbo", listOf(Message("user", testPrompt))))
            }.body()
            response.choices.firstOrNull()?.message?.content ?: "No response from AI"
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }
}