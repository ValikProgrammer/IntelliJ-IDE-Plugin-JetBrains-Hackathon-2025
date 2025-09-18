package com.github.valikprogrammer.intellijidepluginjetbrainshackathon2025.services

import com.github.valikprogrammer.intellijidepluginjetbrainshackathon2025.toolWindow.MyToolWindowFactory
import com.github.valikprogrammer.intellijidepluginjetbrainshackathon2025.ui.model.LlmResponse
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowManager
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Service(Service.Level.PROJECT)
class EvaluationUiService(private val project: Project) {

    @Volatile
    var latestEvaluation: LlmResponse? = null
        private set

    fun updateFromChatCompletionJson(chatCompletionResponse: String) {
        val content = runCatching {
            val root = Json.parseToJsonElement(chatCompletionResponse).jsonObject
            root["choices"]?.jsonArray?.firstOrNull()?.jsonObject
                ?.get("message")?.jsonObject
                ?.get("content")?.jsonPrimitive?.content
        }.getOrNull()

        if (!content.isNullOrBlank() && !content.startsWith("Error:")) {
            latestEvaluation = runCatching { LlmResponse.fromStrictJson(content) }.getOrNull()
        }
    }

    fun refreshToolWindow() {
        val toolWindow = ToolWindowManager.getInstance(project).getToolWindow("MyToolWindow") ?: return
        val contentManager = toolWindow.contentManager
        contentManager.removeAllContents(true)
        MyToolWindowFactory().createToolWindowContent(project, toolWindow)
    }
}


