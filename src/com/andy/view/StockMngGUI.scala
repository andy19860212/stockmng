package com.andy.view

import java.awt.Color

import com.andy.service.NetAssetStockPriceHelper

import scala.swing._
import scala.swing.event.{EditDone, ButtonClicked}

/**
 * Created by wb-zhangwei01 on 2014/7/30.
 */
object StockMngGUI extends SimpleSwingApplication{
   def top = new MainFrame{
     title = "您所持有的股票"
     val testLable = new Label("-----")

     val valuesTable = new Table(
       NetAssetStockPriceHelper.getStockTableValues,
       Array("股票名称", "持股数量")) {
       showGrid = true
       gridColor = Color.BLACK
     }


     contents = new BoxPanel(Orientation.Vertical) {
       contents += valuesTable
       contents += new ScrollPane(valuesTable)
       contents += new FlowPanel{
         contents += testLable
       }
     }


     listenTo(valuesTable)

     reactions += {
       case EditDone(valuesTable) =>
         testLable.text = "11111"
     }
   }
}
