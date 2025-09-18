package com.github.valikprogrammer.intellijidepluginjetbrainshackathon2025.services

import com.intellij.openapi.components.Service
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

@Service
class OpenAIService {

    @Serializable
    data class Message(val role: String, val content: String)

    @Serializable
    data class RequestBody(val model: String, val messages: List<Message>)

    fun sendCodeForAnalysis(apiKey: String, apiUrl: String, code: String): String {
        val prompt = """
System:
You are an expert software engineer specializing in static code analysis.
Your task is to provide a precise, unbiased evaluation of the given code snippet. 
Your response must strictly follow the required JSON format and contain no extra text, explanations, or markdown. 
If a metric cannot be computed, provide the best estimate based on static analysis.

Important:
- Each metric must have a score from 0 to 100.
- Each "can_be_improved" field must contain a list of concrete improvement ideas. 
  The number of ideas is flexible (min 0, max 3), depending on actual opportunities for improvement.

User:
Analyze the code below and respond with a single JSON object. 

Code:
$code

Required JSON format:
{
  "language": "string",
  "statistics": {
    "lines_of_code": "integer",
    "number_of_functions": "integer",
    "average_function_length": "float (lines per function)",
    "comment_density": "float (percentage of comment lines)"
  },
  "metrics": {
    "readability": {
      "score_100": "integer (0-100)",
      "can_be_improved": ["idea1", "idea2", "..."]
    },
    "complexity": {
      "score_100": "integer (0-100)",
      "can_be_improved": ["idea1", "..."]
    },
    "maintainability": {
      "score_100": "integer (0-100)",
      "can_be_improved": []
    },
    "performance": {
      "score_100": "integer (0-100)",
      "can_be_improved": ["idea1", "idea2"]
    },
    "security": {
      "score_100": "integer (0-100)",
      "can_be_improved": []
    },
    "consistency": {
      "score_100": "integer (0-100)",
      "can_be_improved": ["idea1"]
    },
    "reusability": {
      "score_100": "integer (0-100)",
      "can_be_improved": []
    },
    "testability": {
      "score_100": "integer (0-100)",
      "can_be_improved": ["idea1"]
    },
    "duplication": {
      "score_100": "integer (0-100)",
      "can_be_improved": []
    }
  },
  "overall_score": {
    "score_100": "integer (0-100)"
  },

  "feedback": "string (a single concise sentence summarizing the code quality)"
}
"""
        val body = RequestBody(
            model = "gpt-3.5-turbo",
            messages = listOf(Message("user", prompt))
        )
        val jsonBody = Json.encodeToString(body)

        return try {
            val url = URL(apiUrl)
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Authorization", "Bearer $apiKey")
            conn.setRequestProperty("Content-Type", "application/json")
            conn.doOutput = true

            OutputStreamWriter(conn.outputStream, Charsets.UTF_8).use { it.write(jsonBody) }

            val code = conn.responseCode

            if (code == 200) {
                conn.inputStream.bufferedReader().readText()
            }

            else {
                val errMsg = conn.errorStream?.bufferedReader()?.readText()
                "Error: Server returned HTTP response code: $code for URL: $apiUrl\n${errMsg ?: ""}"
            }

        }

        catch (e: Exception) {
            "Error: ${e.message}"
        }
    }

    fun sendPrompt(apiKey: String, apiUrl: String, prompt: String,improvementType: String): String {
        // TODO
        println("\n\n===>Sending prompt $improvementType")
        /*return """{
            "id": "chatcmpl-XYZ",
            "object": "chat.completion",
            "choices": [
                        {
                            "index": 0,
                            "finish_reason": "stop",
                            "message": {
                                    "role": "assistant",
                                    "content": "Improved code that we got from AI for $improvementType\n\n"
                            }
                        }
            ],
            "usage": {
                    "prompt_tokens": 123,
                    "completion_tokens": 456,
                    "total_tokens": 579
            }
        }
        """*/

        val body = RequestBody(
            model = "gpt-3.5-turbo",
            messages = listOf(Message("user", prompt))
        )
        val jsonBody = Json.encodeToString(body)

        return try {
            val url = URL(apiUrl)
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Authorization", "Bearer $apiKey")
            conn.setRequestProperty("Content-Type", "application/json")
            conn.doOutput = true

            OutputStreamWriter(conn.outputStream, Charsets.UTF_8).use { it.write(jsonBody) }

            val code = conn.responseCode
            if (code == 200) conn.inputStream.bufferedReader().readText()
            else {
                val errMsg = conn.errorStream?.bufferedReader()?.readText()
                "Error: Server returned HTTP response code: $code for URL: $apiUrl\n${errMsg ?: ""}"
            }
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }
}