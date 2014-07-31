package com.andy.view

import java.awt.Color
import javax.swing.event.{TableModelListener, TableModelEvent}

import com.andy.model.file.StockMngXMLHelper
import com.andy.service.NetAssetStockPriceHelper

import scala.swing._
import scala.swing.event._

/**
 * Created by wb-zhangwei01 on 2014/7/30.
 */
object StockMngGUI extends SimpleSwingApplication {
  def top = new MainFrame {
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
      contents += new FlowPanel {
        contents += testLable
      }
    }

    def updateStockUnits(ticker : String, units : Int) = {
     val changeStockMap = StockMngXMLHelper.getTickersAndUnits.updated(ticker,units)
     StockMngXMLHelper save changeStockMap
    }


    val myModelListener = new TableModelListener {
      def tableChanged(e: TableModelEvent) = {
        e.getType match {
          case TableModelEvent.UPDATE =>
            updateStockUnits(valuesTable(e.getLastRow,e.getColumn-1).toString,valuesTable(e.getLastRow,e.getColumn).toString.toInt)
        }

      }
    }

    valuesTable.model.addTableModelListener(myModelListener)

  }
}
