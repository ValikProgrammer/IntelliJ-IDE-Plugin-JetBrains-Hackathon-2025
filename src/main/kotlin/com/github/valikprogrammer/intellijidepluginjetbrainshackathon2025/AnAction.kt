package com.github.valikprogrammer.intellijidepluginjetbrainshackathon2025

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages

class MyAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        // Your action code here
        Messages.showInfoMessage("Hello, World!", "My Plugin")
    }
}