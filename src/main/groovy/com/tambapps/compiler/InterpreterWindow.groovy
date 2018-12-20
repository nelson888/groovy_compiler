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
frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

JToolBar toolBar = new JToolBar()
frame.getContentPane().add(toolBar, BorderLayout.NORTH)

JButton btnRun = new JButton("Execute")
toolBar.add(btnRun)

JSplitPane splitPane = new JSplitPane()
splitPane.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null))
splitPane.setOneTouchExpandable(true)
splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT)
frame.getContentPane().add(splitPane, BorderLayout.CENTER)

editorCode = new JTextPane()
editorCode.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null))
editorCode.setMinimumSize(new Dimension(0, 500))
editorCode.setBackground(new Color(40,40,40))
editorCode.setFont(new Font(Font.SANS_SERIF,Font.BOLD,16))

scroll = new JScrollPane(editorCode,
    ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER)
scroll.setPreferredSize(new Dimension(0, 500))
splitPane.setLeftComponent(scroll)

console = new JTextPane()
console.setEditable(false)
console.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null))
splitPane.setRightComponent(console)

Interpreter interpreter = new Interpreter()
btnRun.addActionListener({
  e ->
    console.setText("")
    interpreter.interpret(editorCode.getText(),
        {o -> console.setText(console.getText() + o + "\n" )})
})

frame.setVisible(true)