package com.github.valikprogrammer.intellijidepluginjetbrainshackathon2025.toolWindow

import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.ex.ActionUtil
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import com.github.valikprogrammer.intellijidepluginjetbrainshackathon2025.services.MyProjectService

import com.github.valikprogrammer.intellijidepluginjetbrainshackathon2025.ui.MetricPanel
import com.github.valikprogrammer.intellijidepluginjetbrainshackathon2025.ui.StatisticsPanel
import com.github.valikprogrammer.intellijidepluginjetbrainshackathon2025.ui.invokeAction
import com.github.valikprogrammer.intellijidepluginjetbrainshackathon2025.ui.model.LlmResponse
import com.github.valikprogrammer.intellijidepluginjetbrainshackathon2025.CollectCodeAction
import com.github.valikprogrammer.intellijidepluginjetbrainshackathon2025.services.EvaluationUiService

import com.intellij.ui.components.JBScrollPane
import javax.swing.BoxLayout
import javax.swing.JPanel
import java.awt.BorderLayout
import java.awt.*
import javax.swing.*
import com.intellij.ide.DataManager
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.fileEditor.FileEditorManager
import java.awt.Component
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.actionSystem.ActionManager
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.text.startsWith


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
        private val toolWindowRef: ToolWindow = toolWindow
        private val service = toolWindow.project.service<MyProjectService>()

        fun getContent(): JPanel {

            val panel = JPanel(BorderLayout())

            // === REFRESH button at top ===
            val refreshButton = JButton("REFRESH").apply {
                font = Font("Arial", Font.BOLD, 18)
                preferredSize = Dimension(200, 50)
                background = Color(0, 123, 255)
                foreground = Color.WHITE
                isFocusPainted = false
                isOpaque = true
                border = BorderFactory.createEmptyBorder(10, 20, 10, 20)

                addActionListener {
                    invokeAction("OpenAITest.TestAction", this)
                }
            }

            val buttonPanel = JPanel().apply {
                layout = BoxLayout(this, BoxLayout.X_AXIS)
                add(Box.createHorizontalGlue())
                add(refreshButton)
                add(Box.createHorizontalGlue())
            }

            panel.add(buttonPanel, BorderLayout.NORTH)


//            val action = ActionManager.getInstance().getAction("OpenAITest.TestAction")

            // Dummy JSON response (replace later with backend data)

            //val dummyResponse = actionPerformed(action) //
            val evaluation = toolWindowRef.project.service<EvaluationUiService>().latestEvaluation
            val dummyResponse = evaluation ?: LlmResponse.createDummy()

            // Create one big vertical container for metrics + statistics
            val mainContent = JPanel().apply {
                layout = BoxLayout(this, BoxLayout.Y_AXIS)

                // Add all metrics
                dummyResponse.metrics.forEach { (name, metric) ->
                    add(MetricPanel(name, metric.score100, metric.canBeImproved))

                  
//                   =======
//             return JPanel().apply {
//                 layout = BorderLayout()
//                 border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
//                 background = Color(248, 249, 250) // Светло-серый фон

//                 // Главная вертикальная панель
//                 val mainPanel = JPanel().apply {
//                     layout = BoxLayout(this, BoxLayout.Y_AXIS)
//                     alignmentX = Component.CENTER_ALIGNMENT
//                     background = Color(248, 249, 250)
//                 }

//                 // REFRESH кнопка
//                 val refreshButton = JButton("REFRESH").apply {
//                     background = Color(0, 123, 255) // Синий цвет
//                     foreground = Color.WHITE
//                     font = Font("Arial", Font.BOLD, 16)
//                     preferredSize = Dimension(Int.MAX_VALUE, 50)
//                     maximumSize = Dimension(Int.MAX_VALUE, 50)
//                     isOpaque = true
//                     border = BorderFactory.createEmptyBorder(15, 20, 15, 20)
//                     isFocusPainted = false
//                     isContentAreaFilled = true
//                     setUI(createCustomButtonUI())

//                     addActionListener {
//                         invokeAction("OpenAITest.TestAction", this)
//                     }
//                 }

//                 // Панель для кнопок улучшения
//                 val improvementPanel = JPanel().apply {
//                     layout = BoxLayout(this, BoxLayout.Y_AXIS)
//                     border = BorderFactory.createEmptyBorder(20, 0, 0, 0)
//                     alignmentX = Component.CENTER_ALIGNMENT
//                     background = Color(248, 249, 250)
//                 }

                // Readability секция
//                val readabilityPanel = createImprovementSection(
//                    labelText = "Readability:",
//                    buttonText = "Improve",
//                    buttonColor = Color(40, 167, 69) // Зеленый цвет
//                ) { source ->
//                    invokeAction("OpenAITest.ImproveReadabilityAction", source)
//                }
//
//                // Effectiveness секция
//                val effectivenessPanel = createImprovementSection(
//                    labelText = "Effectiveness:",
//                    buttonText = "Improve",
//                    buttonColor = Color(40, 167, 69) // Зеленый цвет
//                ) { source ->
//                    invokeAction("OpenAITest.ImproveEffectivenessAction", source)
//                }

//                 val readabilityPanel = createImprovementSection(
//                     "Readability:",
//                     "Improve",
//                     Color(40, 167, 69)
//                 ) { source ->
//                     invokeAction("OpenAITest.ImproveReadabilityAction", source)
//                 }

//                 val effectivenessPanel = createImprovementSection(
//                     "Effectiveness:",
//                     "Improve",
//                     Color(40, 167, 69)
//                 ) { source ->
//                     invokeAction("OpenAITest.ImproveEffectivenessAction", source)
//                 }



//                 improvementPanel.add(readabilityPanel)
//                 improvementPanel.add(Box.createVerticalStrut(20))
//                 improvementPanel.add(effectivenessPanel)

//                 mainPanel.add(refreshButton)
//                 mainPanel.add(Box.createVerticalStrut(20))
//                 mainPanel.add(improvementPanel)

//                 add(mainPanel, BorderLayout.CENTER)
//             }
//         }

        /**
         * Создаёт секцию с заголовком и кнопкой. В колбек onClick передаётся сама кнопка
         * как источник DataContext для корректного извлечения PROJECT/EDITOR.
         */
//         private fun createImprovementSection(
//             labelText: String,
//             buttonText: String,
//             buttonColor: Color,
//             onClick: (Component) -> Unit
//         ): JPanel {
//             return JPanel().apply {
//                 layout = BoxLayout(this, BoxLayout.Y_AXIS)
//                 alignmentX = Component.CENTER_ALIGNMENT
//                 background = Color(248, 249, 250)

//                 val label = JLabel(labelText).apply {
//                     font = Font("Arial", Font.BOLD, 14)
//                     foreground = Color(51, 51, 51)
//                     alignmentX = Component.CENTER_ALIGNMENT
//                 }

//                 val button = JButton(buttonText).apply {
//                     background = buttonColor
//                     foreground = Color.WHITE
//                     font = Font("Arial", Font.BOLD, 14)
//                     preferredSize = Dimension(140, 45)
//                     maximumSize = Dimension(140, 45)
//                     isOpaque = true
//                     border = BorderFactory.createEmptyBorder(12, 20, 12, 20)
//                     isFocusPainted = false
//                     isContentAreaFilled = true
//                     alignmentX = Component.CENTER_ALIGNMENT
//                     setUI(createCustomButtonUI())

//                     addActionListener { onClick(this) }
//                 }

//                 add(label)
//                 add(Box.createVerticalStrut(8))
//                 add(button)
//             }
//         }

//         private fun createCustomButtonUI(): javax.swing.plaf.ButtonUI {
//             return object : javax.swing.plaf.basic.BasicButtonUI() {
//                 override fun paint(g: Graphics, c: JComponent) {
//                     val b = c as JButton
//                     val g2 = g as Graphics2D
                    
//                     // Включаем сглаживание
//                     g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
                    
//                     // Рисуем фон кнопки
//                     g2.color = b.background
//                     g2.fillRoundRect(0, 0, b.width, b.height, 8, 8)
                    
//                     // Рисуем текст
//                     g2.color = b.foreground
//                     g2.font = b.font
//                     val fm = g2.fontMetrics
//                     val text = b.text
//                     val x = (b.width - fm.stringWidth(text)) / 2
//                     val y = (b.height - fm.height) / 2 + fm.ascent
//                     g2.drawString(text, x, y)
// >>>>>>> main
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
