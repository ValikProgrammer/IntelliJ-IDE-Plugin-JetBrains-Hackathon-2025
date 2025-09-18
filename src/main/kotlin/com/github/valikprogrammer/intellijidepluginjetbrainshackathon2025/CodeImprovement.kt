package com.github.valikprogrammer.intellijidepluginjetbrainshackathon2025

import com.github.valikprogrammer.intellijidepluginjetbrainshackathon2025.services.OpenAIService
import com.github.valikprogrammer.intellijidepluginjetbrainshackathon2025.codeImprovers.CodeImprovers
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
		val OPENAI_API_KEY = "something:)"
        val OPENAI_URL = "https://api.openai.com/v1/chat/completions"


		if (OPENAI_API_KEY.isBlank()) {
			Messages.showErrorDialog(project, "Missing API key.", "Configuration Error")
			return
		}

		val document = editor.document
		val fileText = document.text
		if (fileText.isBlank()) {
			Messages.showMessageDialog(project, "No code to improve!", "Warning", Messages.getWarningIcon())
		 return
		}

		ApplicationManager.getApplication().invokeLater {
			Messages.showMessageDialog(project, "Wait ... Magic is happening", "Improving Code", Messages.getInformationIcon())
		}

        println("===> BUILD PROMPT ${improvementType}")
		val prompt = CodeImprovers.buildPrompt(improvementType, fileText)

		ProgressManager.getInstance().run(object : Task.Backgroundable(project, "Improving Code...", true) {
			override fun run(indicator: ProgressIndicator) {
				indicator.isIndeterminate = true

                println("==> RUN openAIService and openAIService.sendPrompt")
				val openAIService = project.service<OpenAIService>()
				val result = runBlocking {
					openAIService.sendPrompt(OPENAI_API_KEY, OPENAI_URL, prompt,improvementType)
				}

                print("==> GOR RESULT and display it in user's window\n result = $result")
				ApplicationManager.getApplication().invokeLater {
					try {
						val json = Json.parseToJsonElement(result).jsonObject
						val content = json["choices"]?.jsonArray
							?.firstOrNull()?.jsonObject
							?.get("message")?.jsonObject
							?.get("content")?.jsonPrimitive?.content

						if (!content.isNullOrBlank() && !content.startsWith("Error:")) {
							WriteCommandAction.runWriteCommandAction(project) {
								document.setText(content)
							}
							Messages.showMessageDialog(project, "Code improved successfully!", "Success", Messages.getInformationIcon())
						} else {
							Messages.showErrorDialog(project, "Failed to improve code.", "Error")
						}
					} catch (e: Exception) {
						Messages.showErrorDialog(project, "Failed to parse response: ${e.message}", "Error")
					}
				}
			}
		})
	}
}