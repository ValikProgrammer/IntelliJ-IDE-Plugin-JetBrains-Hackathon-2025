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
import com.intellij.openapi.diagnostic.Logger


class TestAction {
    val OPENAI_API_KEY=""
    val OPENAI_URL=""
//    val logger = Logger.getInstance("Test Action")



    fun actionPerformed(e: AnActionEvent, codeFromUser: String ) {
        println("Hello world!")
//        logger.info("Hello from IntelliJ logger")
        val project: Project = e.project ?: return
//        val dir = project.basePath ?: "./"

//        val dotenv = dotenv {
//            directory = dir
//            ignoreIfMissing = true
//        }


        val apiKey = OPENAI_API_KEY //dotenv["OPENAI_API_KEY"]
        val apiUrl = OPENAI_URL // dotenv["OPENAI_URL"]

        if (apiKey.isBlank() || apiUrl.isBlank()) {
            Messages.showErrorDialog(project, "API key or URL not found in .env file", "Configuration Error")
            return
        }

        println("Api key found $apiKey $apiUrl")
//
        val openAIService = service<OpenAIService>()
        val result = runBlocking {
            openAIService.sendTestRequest(apiKey, apiUrl, codeFromUser)
        }
        println("Resieved result $result")
//        Messages.showMessageDialog(project, result, "API Test Result", Messages.getInformationIcon())

//        ProgressManager.getInstance().run(object : Task.Backgroundable(project, "Testing API...", true) {
//            override fun run(indicator: ProgressIndicator) {
//                indicator.isIndeterminate = true
//
//                val openAIService = service<OpenAIService>()
//                val result = runBlocking {
//                    openAIService.sendTestRequest(apiKey, apiUrl,codeFromUser)
//                }
//
//                println("Resieved result $result")
//
//                ApplicationManager.getApplication().invokeLater {
//                    Messages.showMessageDialog(project, result, "API Test Result", Messages.getInformationIcon())
//                }
//            }
//        })
    }
}