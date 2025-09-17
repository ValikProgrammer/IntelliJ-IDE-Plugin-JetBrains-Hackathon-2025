package com.github.valikprogrammer.intellijidepluginjetbrainshackathon2025.toolWindow

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import com.github.valikprogrammer.intellijidepluginjetbrainshackathon2025.services.MyProjectService
import java.awt.BorderLayout
import javax.swing.JPanel

class MyToolWindowFactory : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val myWindow = MyToolWindow(toolWindow)
        val contentPanel = myWindow.getContent()
        val content = ContentFactory.getInstance()
            .createContent(contentPanel, /*displayName*/ null, /*isLockable*/ false)
        toolWindow.contentManager.addContent(content)
    }

    override fun shouldBeAvailable(project: Project) = true

    private class MyToolWindow(toolWindow: ToolWindow) {
        // Dan4life comments: service currently not used
        private val service = toolWindow.project.service<MyProjectService>()

        fun getContent(): JPanel {
            // Создаём панель с BorderLayout
            return JPanel(BorderLayout()).apply {

                // Получаем AnAction из ActionManager
                val action = ActionManager
                    .getInstance()
                    .getAction("OpenAITest.TestAction")

                // Если экшн зарегистрирован, превращаем его в тулбар
                if (action != null) {
                    val group = DefaultActionGroup().apply { add(action) }
                    val toolbar = ActionManager
                        .getInstance()
                        .createActionToolbar("MyToolWindowToolbar", group, /*horizontal*/ true)
                    // Добавляем тулбар наверх панели
                    add(toolbar.component, BorderLayout.NORTH)
                }
            }
        }
    }
}
