package com.andy.view

import java.awt.Color
import java.util.Date

import com.andy.service.NetAssetStockPriceHelper

import scala.actors._
import scala.swing._
import event._

/**
 * Created by wb-zhangwei01 on 2014/7/30.
 * 个人股票资产查看
 */
object NetAssetAppGUI extends SimpleSwingApplication {
  def top = mainFrame

  val mainFrame = new MainFrame {
    title = "股票资产"
    val dateLable = new Label {
      text = "最近更新时间:--------------"
    }
    val valuesTable = new Table(
      NetAssetStockPriceHelper.getInitialTableValues,
      Array("股票名称", "持股数量", "最新单股价($)", "您的所得($)")) {
      showGrid = true
      gridColor = Color.BLACK
    }

    val updateButton = new Button {
      text = "更新股票信息"
    }
    val netAssetLabel = new Label {
      text = "总价格:????"
    }

    contents = new BoxPanel(Orientation.Vertical) {
      contents += dateLable
      contents += valuesTable
      contents += new ScrollPane(valuesTable)

      contents += new FlowPanel {
        contents += updateButton
        contents += netAssetLabel
      }
    }

    listenTo(updateButton)



    reactions += {
      case ButtonClicked(button) =>
        button.enabled = false
        NetAssetStockPriceHelper fetchPrice uiUpdater
    }

    val uiUpdater = new Actor {
      def act = {
        loop {
          react {
            case (ticker: String, units: Int, price: Double, value: Double) => updateTable(ticker, units, price, value)
            case netAsset =>
              netAssetLabel.text = "总价格:" + netAsset
              dateLable.text = "最近更新时间:" + new Date()
              updateButton.enabled = true
          }
        }
      }

      // override protected def scheduler() = new SingleThreadedScheduler
    }

    uiUpdater.start()

    def updateTable(ticker: String, units: Int, price: Double, value: Double) {
      for (i <- 0 until valuesTable.rowCount) {
        if (valuesTable(i, 0) == ticker) {
          valuesTable(i, 2) = price
          valuesTable(i, 3) = value
        }
      }
    }
  }


}
