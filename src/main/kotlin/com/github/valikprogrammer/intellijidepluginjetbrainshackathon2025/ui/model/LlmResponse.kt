package com.github.valikprogrammer.intellijidepluginjetbrainshackathon2025.ui.model


import com.github.valikprogrammer.intellijidepluginjetbrainshackathon2025.services.OpenAIService
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.components.service
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import kotlinx.coroutines.runBlocking
import com.intellij.openapi.application.ApplicationManager

data class LlmResponse(
    val language: String,
    val statistics: Statistics,
    val metrics: Map<String, Metric>,
    val overallScore: OverallScore,
    val feedback: String
) {
    companion object {
        fun createDummy(): LlmResponse {

            val OPENAI_API_KEY = "something:)"
            val OPENAI_URL = "https://api.openai.com/v1/chat/completions"

            /*if (OPENAI_API_KEY.isBlank()) {
                Messages.showErrorDialog(project, "Missing API key.", "Configuration Error")
                return
            }*/

            //ProgressManager.getInstance().run(object : Task.Backgroundable(project, "Analyzing Code...", true) {
            //    override fun run(indicator: ProgressIndicator) {
            //        indicator.isIndeterminate = true

                    val openAIService = project.service<OpenAIService>()
                    val result = runBlocking {
                        openAIService.sendCodeForAnalysis(OPENAI_API_KEY, OPENAI_URL, fileText)
                    }

                    println(result)
                    return result
            //    }

            /*return LlmResponse(
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
            )*/
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
