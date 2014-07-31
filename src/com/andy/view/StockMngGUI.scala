package com.andy.view

import java.awt.Color
import javax.swing.ListSelectionModel
import javax.swing.event.{TableModelEvent, TableModelListener}

import com.andy.model.file.StockMngXMLHelper
import com.andy.service.NetAssetStockPriceHelper

import scala.swing._
import scala.swing.event.{TableStructureChanged, TableUpdated, TableChanged, ButtonClicked}

/**
 * Created by wb-zhangwei01 on 2014/7/30.
 */
object StockMngGUI extends SimpleSwingApplication {
  def top = new MainFrame {
    title = "您所持有的股票"
    val msgLable = new Label("")

    val addButton = new Button {
      text = "增加"
    }

    val deletButton = new Button {
      text = "删除"
    }

    val tableColName = Array("股票名称", "持股数量")
    val valuesTable = new Table(
      NetAssetStockPriceHelper.getStockTableValues,
      tableColName) {
      showGrid = true
      gridColor = Color.BLACK
      peer.setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
    }


    contents = new BoxPanel(Orientation.Vertical) {
      contents += valuesTable
      contents += new ScrollPane(valuesTable)
      contents += new FlowPanel {
        contents += msgLable
        contents += addButton
        contents += deletButton
      }
    }

    /**
     * 更新持股信息
     * @param ticker
     * @param units
     */
    def updateStockUnits(ticker: String, units: Int) = {
      val changeStockMap = StockMngXMLHelper.getTickersAndUnits.updated(ticker, units)
      StockMngXMLHelper save changeStockMap
    }

     listenTo(addButton)
     listenTo(valuesTable)
    //  listenTo(deletButton)

    /**
     * 添加表修改监听事件
     */
  /*  valuesTable.model.addTableModelListener(new TableModelListener {
      override def tableChanged(e: TableModelEvent): Unit = {
        e.getType match {
          case TableModelEvent.UPDATE =>
            if (valuesTable.selection.columns.last == 1) {
              updateStockUnits(valuesTable(e.getLastRow, e.getColumn - 1).toString, valuesTable(e.getLastRow, e.getColumn).toString.toInt)
              msgLable.foreground = Color.BLACK
              msgLable.text = "修改成功"
            } else {
              msgLable.foreground = Color.RED
              msgLable.text = "股票名称不能修改!"
            }

          case TableModelEvent.INSERT =>


        }
      }
    })
*/


    reactions += {
      case ButtonClicked(addButton) =>
        if (valuesTable.selection.columns.last == 1) {
          valuesTable.peer.editCellAt(valuesTable.selection.rows.last, valuesTable.selection.columns.last)
        }
      case TableUpdated(valuesTable,valuesTable.selection.rows.head to valuesTable.selection.rows.last,valuesTable.selection.columns.last) =>
        if (valuesTable.selection.columns.last == 1) {
          updateStockUnits(valuesTable(valuesTable.selection.rows.last, valuesTable.selection.columns.last - 1).toString, valuesTable(valuesTable.selection.rows.last, valuesTable.selection.columns.last).toString.toInt)
          msgLable.foreground = Color.BLACK
          msgLable.text = "修改成功"
        } else {
          msgLable.foreground = Color.RED
          msgLable.text = "股票名称不能修改!"
        }
    }

  }
}
