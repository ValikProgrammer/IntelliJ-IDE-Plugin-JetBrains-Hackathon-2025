package com.github.valikprogrammer.intellijidepluginjetbrainshackathon2025

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

class CollectCodeAction : AnAction("REFRESH") {
    override fun actionPerformed(e: AnActionEvent) {
        val project: Project? = e.project
        if (project == null) {
            Messages.showErrorDialog("No project context.", "Error")
            return
        }

        // Try event editor, then fall back to the selected editor in FileEditorManager
        val editor = e.getData(CommonDataKeys.EDITOR)
            ?: FileEditorManager.getInstance(project).selectedTextEditor

        if (editor == null) {
            Messages.showMessageDialog(
                project,
                "No active editor found! Please open a file in the editor first.",
                "Error",
                Messages.getErrorIcon()
            )
            return
        }

        val document = editor.document
        val fileText = document.text

        val OPENAI_API_KEY = "api key not missing )))"
        val OPENAI_URL = "https://api.openai.com/v1/chat/completions"

        if (OPENAI_API_KEY.isBlank()) {
            Messages.showErrorDialog(project, "Missing API key.", "Configuration Error")
            return
        }

        ProgressManager.getInstance().run(object : Task.Backgroundable(project, "Analyzing Code...", true) {
            override fun run(indicator: ProgressIndicator) {
                indicator.isIndeterminate = true

                val openAIService = project.service<OpenAIService>()
                val result = runBlocking {
                    openAIService.sendCodeForAnalysis(OPENAI_API_KEY, OPENAI_URL, fileText)
                }

                ApplicationManager.getApplication().invokeLater {
                    Messages.showMessageDialog(project, result, "Analysis Result", Messages.getInformationIcon())
                }
            }
        })
    }
}
