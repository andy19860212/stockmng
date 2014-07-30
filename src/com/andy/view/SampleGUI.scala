package com.andy.view

import scala.swing._
import scala.swing.event.ButtonClicked

/**
 * Created by wb-zhangwei01 on 2014/7/30.
 */
object SampleGUI extends SimpleSwingApplication {
  def top = new MainFrame {
    title = "A Simple Scala Swing GUI"

    val label = new Label {
      text = "------------------------"
    }
    val button = new Button {
      text = "Click me"
    }

    contents = new FlowPanel {
      contents += label
      contents += button
    }

    listenTo(button)

    reactions += {
      case ButtonClicked(button) =>
        label.text = "You clicked!"
    }
  }
}
