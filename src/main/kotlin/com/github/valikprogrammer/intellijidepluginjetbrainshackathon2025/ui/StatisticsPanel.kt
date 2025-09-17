package com.github.valikprogrammer.intellijidepluginjetbrainshackathon2025.ui

import com.github.valikprogrammer.intellijidepluginjetbrainshackathon2025.ui.model.Statistics
import com.intellij.ui.JBColor
import java.awt.Color
import java.awt.GridLayout
import javax.swing.BorderFactory
import javax.swing.JLabel
import javax.swing.JPanel

class StatisticsPanel(language: String, statistics: Statistics) : JPanel() {

    init {
        layout = GridLayout(0, 1, 6, 6)
        isOpaque = true
        background = JBColor(0x000000, 0xFFFFFF) // Black / White
        foreground = JBColor(0xFFFFFF, 0x000000) // White / Black

        border = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 2, true),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        )

        fun label(text: String) = JLabel(text).apply {
            foreground = JBColor(0x000000, 0xFFFFFF) // Black / White
            isOpaque = false
        }

        add(label("Language: $language"))
        add(label("Lines of Code: ${statistics.linesOfCode}"))
        add(label("Number of Functions: ${statistics.numberOfFunctions}"))
        add(label("Average Function Length: ${statistics.averageFunctionLength}"))
        add(label("Comment Density: ${statistics.commentDensity}%"))
    }
}