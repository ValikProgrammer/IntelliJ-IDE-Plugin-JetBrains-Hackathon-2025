package com.github.valikprogrammer.intellijidepluginjetbrainshackathon2025

import com.github.valikprogrammer.intellijidepluginjetbrainshackathon2025.services.OpenAIService
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.runBlocking
import com.intellij.openapi.application.ApplicationManager

class TestAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val project: Project = e.project ?: return

        val dotenv = dotenv {
            directory = project.basePath ?: "./"
            ignoreIfMissing = true
        }
        val apiKey = dotenv["OPENAI_API_KEY"]
        val apiUrl = dotenv["OPENAI_URL"]

        if (apiKey == null || apiUrl == null) {
            Messages.showErrorDialog(project, "API key or URL not found in .env file", "Configuration Error")
            return
        }

        ProgressManager.getInstance().run(object : Task.Backgroundable(project, "Testing API...", true) {
            override fun run(indicator: ProgressIndicator) {
                indicator.isIndeterminate = true

                val openAIService = service<OpenAIService>()
                val result = runBlocking {
                    openAIService.sendTestRequest(apiKey, apiUrl)
                }

                ApplicationManager.getApplication().invokeLater {
                    Messages.showMessageDialog(project, result, "API Test Result", Messages.getInformationIcon())
                }
            }
        })
    }
}