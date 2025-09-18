package com.github.valikprogrammer.intellijidepluginjetbrainshackathon2025.ui

import com.intellij.ui.JBColor
import java.awt.*
import javax.swing.*

class MetricPanel(
    metricName: String,
    private val score100: Int,
    private val canBeImproved: List<String>
) : JPanel() {

    init {
        layout = BorderLayout()
        background = JBColor(0xE0E0E0, 0x2B2B2B) // Light gray / Dark gray

        // ✅ Add margin outside + border + padding inside
        border = BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(8, 8, 8, 8), // outer margin
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(JBColor.WHITE, 2, true), // white border
                BorderFactory.createEmptyBorder(10, 12, 10, 12) // inner padding
            )
        )

        val titleLabel = JLabel(metricName.uppercase()).apply {
            font = font.deriveFont(Font.BOLD, 14f)
            foreground = JBColor(0x000000, 0xFFFFFF) // Black / White
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
            addActionListener { println("Improve clicked for $metricName") }
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
