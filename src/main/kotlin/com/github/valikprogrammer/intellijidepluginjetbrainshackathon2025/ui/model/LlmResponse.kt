package com.github.valikprogrammer.intellijidepluginjetbrainshackathon2025.ui.model

import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

data class LlmResponse(
    val language: String,
    val statistics: Statistics,
    val metrics: Map<String, Metric>,
    val overallScore: OverallScore,
    val feedback: String
) {
    companion object {
        fun createDummy(): LlmResponse {
            return LlmResponse(
                language = "Kotlin",
                statistics = Statistics(
                    linesOfCode = 0,
                    numberOfFunctions = 0,
                    averageFunctionLength = 0.0,
                    commentDensity = 0.0
                ),
                metrics = mapOf(
                    "Readability" to Metric(0, emptyList()),
                    "Complexity" to Metric(0, emptyList()),
                    "Maintainability" to Metric(0, emptyList()),
                    "Performance" to Metric(0, emptyList()),
                    "Security" to Metric(0, emptyList()),
                    "Consistency" to Metric(0, emptyList()),
                    "Reusability" to Metric(0, emptyList()),
                    "Testability" to Metric(0, emptyList()),
                    "Duplication" to Metric(0, emptyList())
                ),
                overallScore = OverallScore(0),
                feedback = ""
            )
        }

        fun fromStrictJson(strictJson: String): LlmResponse {
            val obj = kotlinx.serialization.json.Json.parseToJsonElement(strictJson).jsonObject
            val statsObj = obj["statistics"]!!.jsonObject
            val metricsObj = obj["metrics"]!!.jsonObject

            fun metric(name: String): Metric {
                val m = metricsObj[name.lowercase()]!!.jsonObject
                val score = m["score_100"]!!.jsonPrimitive.content.toInt()
                val can = m["can_be_improved"]!!.jsonArray.map { it.jsonPrimitive.content }
                return Metric(score, can)
            }

            val metrics = linkedMapOf(
                "Readability" to metric("readability"),
                "Complexity" to metric("complexity"),
                "Maintainability" to metric("maintainability"),
                "Performance" to metric("performance"),
                "Security" to metric("security"),
                "Consistency" to metric("consistency"),
                "Reusability" to metric("reusability"),
                "Testability" to metric("testability"),
                "Duplication" to metric("duplication")
            )

            return LlmResponse(
                language = obj["language"]!!.jsonPrimitive.content,
                statistics = Statistics(
                    linesOfCode = statsObj["lines_of_code"]!!.jsonPrimitive.content.toInt(),
                    numberOfFunctions = statsObj["number_of_functions"]!!.jsonPrimitive.content.toInt(),
                    averageFunctionLength = statsObj["average_function_length"]!!.jsonPrimitive.content.toDouble(),
                    commentDensity = statsObj["comment_density"]!!.jsonPrimitive.content.toDouble()
                ),
                metrics = metrics,
                overallScore = OverallScore(obj["overall_score"]!!.jsonObject["score_100"]!!.jsonPrimitive.content.toInt()),
                feedback = obj["feedback"]!!.jsonPrimitive.content
            )
        }
    }
}

data class Statistics(
    val linesOfCode: Int,
    val numberOfFunctions: Int,
    val averageFunctionLength: Double,
    val commentDensity: Double
)

data class Metric(
    val score100: Int,
    val canBeImproved: List<String>
)

data class OverallScore(
    val score100: Int
)
