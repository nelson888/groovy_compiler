package com.tambapps.compiler

import com.tambapps.compiler.eval.Interpreter

import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JScrollPane
import javax.swing.JSplitPane
import javax.swing.JTextPane
import javax.swing.JToolBar
import javax.swing.ScrollPaneConstants
import javax.swing.border.BevelBorder
import javax.swing.border.EtchedBorder
import javax.swing.border.SoftBevelBorder
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.Font

frame = new JFrame()
frame.setBounds(100, 50, 900, 700)
frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

JToolBar toolBar = new JToolBar()
frame.getContentPane().add(toolBar, BorderLayout.NORTH)

JButton btnRun = new JButton("Execute")
toolBar.add(btnRun)

JSplitPane splitPane = new JSplitPane()
splitPane.border = new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null)
splitPane.oneTouchExpandable = true
splitPane.orientation = JSplitPane.VERTICAL_SPLIT
frame.getContentPane().add(splitPane, BorderLayout.CENTER)

editor = new JTextPane()
editor.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null))
editor.minimumSize = new Dimension(0, 500)
editor.background = new Color(40, 40, 40)
editor.font = new Font(Font.SANS_SERIF, Font.BOLD, 16)
editor.foreground = Color.WHITE

scroll = new JScrollPane(editor,
    ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER)
scroll.preferredSize = new Dimension(0, 500)
splitPane.leftComponent = scroll

console = new JTextPane()
console.editable = false
console.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null))
splitPane.rightComponent = console

Interpreter interpreter = new Interpreter()
btnRun.addActionListener({
  e ->
    console.text = ""
    interpreter.interpret(editor.text,
        { o -> console.text  += o + "\n" })
})

editor.text = "main() {\n\n}"
editor.caretPosition = (9)
editor.caretColor = Color.WHITE
frame.visible = true
editor.requestFocus()