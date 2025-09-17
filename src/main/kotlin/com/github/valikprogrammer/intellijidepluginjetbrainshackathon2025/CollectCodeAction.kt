package com.github.valikprogrammer.intellijidepluginjetbrainshackathon2025

import com.github.valikprogrammer.intellijidepluginjetbrainshackathon2025.services.OpenAIService
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.components.service
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.runBlocking
import com.intellij.openapi.application.ApplicationManager

class CollectCodeAction : AnAction("Collect Code") {
    override fun actionPerformed(e: AnActionEvent) {

        val OPENAI_API_KEY=""
        val OPENAI_URL="https://api.openai.com/v1/chat/completions"

        val project: Project? = e.project
        val editor: Editor? = e.getData(CommonDataKeys.EDITOR)

        if (editor != null && project != null) {
            val document = editor.document
            val fileText = document.text

            val dotenv = dotenv {
                directory = project.basePath ?: "./"
                ignoreIfMissing = true
            }
            val apiKey = OPENAI_API_KEY
            val apiUrl = OPENAI_URL

            if (apiKey == null || apiUrl == null) {
                Messages.showErrorDialog(project, "API key or URL not found in .env file", "Configuration Error")
                return
            }

            ProgressManager.getInstance().run(object : Task.Backgroundable(project, "Analyzing Code...", true) {
                override fun run(indicator: ProgressIndicator) {
                    indicator.isIndeterminate = true

                    val openAIService = project.service<OpenAIService>()
                    val result = runBlocking {
                        openAIService.sendCodeForAnalysis(apiKey, apiUrl, fileText)
                    }

                    // Result log with pop-up
                    ApplicationManager.getApplication().invokeLater {
                        Messages.showMessageDialog(project, result, "Analysis Result", Messages.getInformationIcon())
                    }

                    // ToDo: result parcing from JSON
                    //

                }
            })
        }

        else {
            Messages.showMessageDialog(
                project,
                "No active editor found!",
                "Error",
                Messages.getErrorIcon()
            )
        }
    }
}
