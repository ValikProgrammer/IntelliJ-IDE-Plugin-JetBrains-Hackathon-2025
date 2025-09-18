package com.github.valikprogrammer.intellijidepluginjetbrainshackathon2025.codeImprovers

import com.github.valikprogrammer.intellijidepluginjetbrainshackathon2025.CodeImprovement
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ui.Messages

class ImproveByTypeAction : AnAction() {
	override fun actionPerformed(e: AnActionEvent) {
		val project = e.project
		val editor = e.getData(CommonDataKeys.EDITOR)
		val id = ActionManager.getInstance().getId(this) ?: ""

		val type = when {
			id.endsWith(".ImproveReadabilityAction")     -> "readability"
			id.endsWith(".ImproveEffectivenessAction")   -> "effectiveness"
			id.endsWith(".ImproveMaintainabilityAction") -> "maintainability"
			id.endsWith(".ImprovePerformanceAction")     -> "performance"
			id.endsWith(".ImproveComplexityAction")      -> "complexity"
			id.endsWith(".ImproveSecurityAction")        -> "security"
			id.endsWith(".ImproveConsistencyAction")     -> "consistency"
			id.endsWith(".ImproveReusabilityAction")     -> "reusability"
			id.endsWith(".ImproveTestabilityAction")     -> "testability"
			id.endsWith(".ImproveDuplicationAction")     -> "duplication"
			else -> "readability"
		}

		if (project != null && editor != null) {
			CodeImprovement().sendCodeToImpoveToLLM(project, editor, type)
		} else {
			Messages.showErrorDialog(project, "No active editor found!", "Error")
		}
	}
}
