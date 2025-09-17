package com.github.valikprogrammer.intellijidepluginjetbrainshackathon2025

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages

class ImproveEffectivenessAction : AnAction("Improve Effectiveness") {
    override fun actionPerformed(e: AnActionEvent) {
        val project: Project? = e.project
        val editor: Editor? = e.getData(CommonDataKeys.EDITOR)
        
        if (editor != null && project != null) {
            val codeImprovement = CodeImprovement()
            codeImprovement.sendCodeToImpoveToLLM(project, editor, "effectiveness")
        } else {
            Messages.showMessageDialog(
                project,
                "No active editor found! Please open a file in the editor first.",
                "Error",
                Messages.getErrorIcon()
            )
        }
    }
}
