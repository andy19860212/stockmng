package com.andy.model.file

import scala.xml.XML

/**
 * Created by wb-zhangwei01 on 2014/7/31.
 */
object StockMngXMLHelper {
  /**
   * 从xml文件获取用户股票持有信息
   * @return
   */
  def getTickersAndUnits = {
    val stocksAndUnitsXML = XML.load("stocks.xml")
    (Map[String, Int]() /: (stocksAndUnitsXML \\ "symbol")) {
      (map, symbolNode) => {
        val ticker = (symbolNode \ "@ticker").toString
        val units = (symbolNode \ "units").text.replaceAll("\\r|\\n| ", "").toInt
        map.updated(ticker, units)

      }
    }
  }


  /**
   * 更新和创建股票XML单元
   * @param element
   * @return
   */
  def updateUnitsAndCreateXML(element: (String, Int)) = {
    //定义股票的XML单元
    val (ticker, units) = element
    <symbol ticker={ticker}>
      <units>
        {units}
      </units>
    </symbol>
  }


  /**
   * 保存XML的修改
   * @param stockMap
   */
  def save(stockMap: Map[String, Int]) = {
    //定义股票的XML的结构
    val updatedStocksAndUnitsXML =
      <symbols>
        {stockMap.map {
        updateUnitsAndCreateXML
      }}
      </symbols>


    XML save("stocks.xml", updatedStocksAndUnitsXML)
  }

}
