package com.github.valikprogrammer.intellijidepluginjetbrainshackathon2025.ui.model

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
                    linesOfCode = 120,
                    numberOfFunctions = 8,
                    averageFunctionLength = 15.0,
                    commentDensity = 12.5
                ),
                metrics = mapOf(
                    "Readability" to Metric(72, listOf("Use clearer variable names", "Split long functions")),
                    "Complexity" to Metric(65, listOf("Reduce nested loops", "Extract helper methods", "Lorem ipsum dolor sit amet blah blah blah blah blah blah")),
                    "Maintainability" to Metric(80, emptyList()),
                    "Performance" to Metric(58, listOf("Optimize algorithm for sorting", "Use caching")),
                    "Security" to Metric(90, emptyList()),
                    "Consistency" to Metric(70, listOf("Follow consistent naming conventions")),
                    "Reusability" to Metric(77, emptyList()),
                    "Testability" to Metric(68, listOf("Add more unit tests")),
                    "Duplication" to Metric(85, emptyList())
                ),
                overallScore = OverallScore(74),
                feedback = "The codebase is generally good, but readability and performance can be improved."
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
