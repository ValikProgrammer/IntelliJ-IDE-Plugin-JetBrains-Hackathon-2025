package com.github.valikprogrammer.intellijidepluginjetbrainshackathon2025

import com.github.valikprogrammer.intellijidepluginjetbrainshackathon2025.services.OpenAIService
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.components.service
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.WriteCommandAction
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class CodeImprovement {
    
    fun sendCodeToImpoveToLLM(project: Project, editor: Editor, improvementType: String) {
        val OPENAI_API_KEY = ""
        val OPENAI_URL = "https://api.openai.com/v1/chat/completions"
        
        val document = editor.document
        val fileText = document.text
        
        if (fileText.isBlank()) {
            Messages.showMessageDialog(project, "No code to improve!", "Warning", Messages.getWarningIcon())
            return
        }
        
        // Show waiting dialog
        ApplicationManager.getApplication().invokeLater {
            Messages.showMessageDialog(project, "Wait ... Magic is happening", "Improving Code", Messages.getInformationIcon())
        }
        
        ProgressManager.getInstance().run(object : Task.Backgroundable(project, "Improving Code...", true) {
            override fun run(indicator: ProgressIndicator) {
                indicator.isIndeterminate = true

                println("\n\n=======\nproject.service<OpenAIService>()\n\n=====")
                val openAIService = project.service<OpenAIService>()
                println("\n\n=======\nsendCodeForImprovement\n\n=====")
                val result = runBlocking {
                    openAIService.sendCodeForImprovement(OPENAI_API_KEY, OPENAI_URL, fileText, improvementType)
                }
                println("\n\n=======\nGOING TO:  Parse the response and update the editor \n\n=====")
                // Parse the response and update the editor
                ApplicationManager.getApplication().invokeLater {
                    try {
                        val json = Json.parseToJsonElement(result)
                        val choices = json.jsonObject["choices"]?.jsonArray
                        val firstChoice = choices?.get(0)?.jsonObject
                        val message = firstChoice?.get("message")?.jsonObject
                        val content = message?.get("content")?.jsonPrimitive?.content
                        
                        if (content != null && !content.startsWith("Error:")) {
                            println("\n\n=======\nUpdate the editor with improved code\n\n=====")
                            // Update the editor with improved code
                            WriteCommandAction.runWriteCommandAction(project) {
                                document.setText(content)
                            }
                            Messages.showMessageDialog(project, "Code improved successfully!", "Success", Messages.getInformationIcon())
                        } else {
                            Messages.showErrorDialog(project, "Failed to improve code: $content", "Error")
                        }
                    } catch (e: Exception) {
                        Messages.showErrorDialog(project, "Failed to parse response: ${e.message}", "Error")
                    }
                }
            }
        })
    }
}
