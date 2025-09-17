package com.github.valikprogrammer.intellijidepluginjetbrainshackathon2025

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.TextRange
import com.github.valikprogrammer.intellijidepluginjetbrainshackathon2025.TestAction



class CollectCodeAction : AnAction("Collect Code") {
    override fun actionPerformed(e: AnActionEvent) {
        val project: Project? = e.project
        val editor: Editor? = e.getData(CommonDataKeys.EDITOR)

        if (editor != null && project != null) {
            val document = editor.document
            val fileText = document.text

            println("FileText: $fileText")
            var test_action = TestAction()
            test_action.actionPerformed(e, fileText)
//            val parser = Parser()
//            val splited_code = fileText.split("\n")
//            println(splited_code)
//            parser.parse(fileText.split("\n"))

          // SHOWS code in a window
//            Messages.showMessageDialog(
//                project,
//                fileText,
//                "Collected Code",
//                Messages.getInformationIcon()
//            )


        } else {
            Messages.showMessageDialog(
                project,
                "No active editor found!",
                "Error",
                Messages.getErrorIcon()
            )
        }
    }
}

