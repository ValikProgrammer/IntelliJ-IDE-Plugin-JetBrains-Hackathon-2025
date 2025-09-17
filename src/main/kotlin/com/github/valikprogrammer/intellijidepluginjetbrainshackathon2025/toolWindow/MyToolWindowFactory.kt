package com.github.valikprogrammer.intellijidepluginjetbrainshackathon2025.toolWindow

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import com.github.valikprogrammer.intellijidepluginjetbrainshackathon2025.services.MyProjectService
import com.github.valikprogrammer.intellijidepluginjetbrainshackathon2025.ui.MetricPanel
import com.github.valikprogrammer.intellijidepluginjetbrainshackathon2025.ui.StatisticsPanel
import com.github.valikprogrammer.intellijidepluginjetbrainshackathon2025.ui.model.LlmResponse
import com.intellij.ui.components.JBScrollPane
import javax.swing.BoxLayout
import javax.swing.JPanel
import java.awt.BorderLayout


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
        private val service = toolWindow.project.service<MyProjectService>()

        fun getContent(): JPanel {
            val panel = JPanel(BorderLayout())

            // Toolbar at top (stays fixed)
            val action = ActionManager.getInstance().getAction("OpenAITest.TestAction")
            if (action != null) {
                val group = DefaultActionGroup().apply { add(action) }
                val toolbar = ActionManager
                    .getInstance()
                    .createActionToolbar("MyToolWindowToolbar", group, true)
                panel.add(toolbar.component, BorderLayout.NORTH)
            }

            // Dummy JSON response (replace later with backend data)
            val dummyResponse = LlmResponse.createDummy()

            // Create one big vertical container for metrics + statistics
            val mainContent = JPanel().apply {
                layout = BoxLayout(this, BoxLayout.Y_AXIS)

                // Add all metrics
                dummyResponse.metrics.forEach { (name, metric) ->
                    add(MetricPanel(name, metric.score100, metric.canBeImproved))
                }

                // Add statistics at the bottom
                add(StatisticsPanel(dummyResponse.language, dummyResponse.statistics))
            }

            // Wrap everything in JBScrollPane so it scrolls together
            val scrollPane = JBScrollPane(mainContent)
            panel.add(scrollPane, BorderLayout.CENTER)

            return panel
        }
    }
}
