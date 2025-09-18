package com.github.valikprogrammer.intellijidepluginjetbrainshackathon2025.ui

import com.intellij.ide.DataManager
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.ex.ActionUtil
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.ui.JBColor
import java.awt.*
import javax.swing.*

fun invokeAction(actionId: String, source: Component) {
    val action = ActionManager.getInstance().getAction(actionId) ?: return
    val dataContext = DataManager.getInstance().getDataContext(source)
    val project = CommonDataKeys.PROJECT.getData(dataContext)
    val editorComponent = project?.let { FileEditorManager.getInstance(it).selectedTextEditor?.contentComponent }
    val sourceForContext = editorComponent ?: source

    ActionUtil.invokeAction(action, sourceForContext, ActionPlaces.UNKNOWN, null, null)
}

class MetricPanel(
    metricName: String,
    private val score100: Int,
    private val canBeImproved: List<String>
) : JPanel() {

    init {
        layout = BorderLayout()
        background = JBColor(0xE0E0E0, 0x2B2B2B)

        border = BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(8, 8, 8, 8),
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(JBColor.WHITE, 2, true),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
            )
        )

        val titleLabel = JLabel(metricName.uppercase()).apply {
            font = font.deriveFont(Font.BOLD, 14f)
            foreground = JBColor(0x000000, 0xFFFFFF)
        }

        val scoreLabel = JLabel("Score: $score100/100").apply {
            font = font.deriveFont(Font.PLAIN, 13f)
            foreground = when {
                score100 < 40 -> JBColor.RED
                score100 < 70 -> JBColor.ORANGE
                else -> JBColor.GREEN
            }
        }

        val improvementsText = if (canBeImproved.isEmpty()) {
            "No suggested improvements"
        } else {
            canBeImproved.joinToString("\n") { "- $it" }
        }

        val improvementsLabel = JTextArea(improvementsText).apply {
            isEditable = false
            isOpaque = false
            foreground = JBColor(0x000000, 0xFFFFFF)
            lineWrap = true
            wrapStyleWord = true
            border = null
        }

        val improveButton = JButton("IMPROVE").apply {
            background = JBColor.GREEN
            foreground = JBColor.BLACK
            font = font.deriveFont(Font.BOLD, 12f)
            addActionListener {
                val actionIdClass = when (metricName) {
                    "Readability"      -> "OpenAITest.ImproveReadabilityAction"
                    "Effectiveness"    -> "OpenAITest.ImproveEffectivenessAction"
                    "Maintainability"  -> "OpenAITest.ImproveMaintainabilityAction"
                    "Performance"      -> "OpenAITest.ImprovePerformanceAction"
                    "Complexity"       -> "OpenAITest.ImproveComplexityAction"
                    "Security"         -> "OpenAITest.ImproveSecurityAction"
                    "Consistency"      -> "OpenAITest.ImproveConsistencyAction"
                    "Reusability"      -> "OpenAITest.ImproveReusabilityAction"
                    "Testability"      -> "OpenAITest.ImproveTestabilityAction"
                    "Duplication"      -> "OpenAITest.ImproveDuplicationAction"
                    else               -> return@addActionListener
                }
                invokeAction(actionIdClass, this)
            }
        }

        val topPanel = JPanel(BorderLayout()).apply {
            isOpaque = false
            add(titleLabel, BorderLayout.WEST)
            add(scoreLabel, BorderLayout.EAST)
        }

        add(topPanel, BorderLayout.NORTH)
        add(JScrollPane(improvementsLabel).apply {
            border = null
            isOpaque = false
        }, BorderLayout.CENTER)
        add(improveButton, BorderLayout.EAST)
    }
}
