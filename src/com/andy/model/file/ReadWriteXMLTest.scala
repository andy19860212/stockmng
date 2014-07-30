package com.andy.model.file

import scala.xml.XML

/**
 * Created by wb-zhangwei01 on 2014/7/29.
 */
object ReadWriteXMLTest {
  def main(args: Array[String]) {
    val stocksAndUnits = XML.loadFile("stocks.xml")
    println("Loaded File has " + (stocksAndUnits \\ "symbol").size + " symbol elements")

    val stocksAndUnitsMap = (Map[String, Int]() /: (stocksAndUnits \ "symbol")) {
      (map, symbolNode) =>
        val ticker = (symbolNode \ "@ticker").toString
        val units = (symbolNode \ "units").text.toInt
        map.updated(ticker, units)
    }
    println("Number of symbol elements found is " + stocksAndUnitsMap.size)

    def updateUnitsAndCreateXML(element: (String, Int)) = {
      val (ticker, units) = element
      <symbol ticker={ticker}>
        <units>
          {units + 1}
        </units>
      </symbol>
    }

    val updatedStocksAndUnitsXML =
      <symbols>
        {stocksAndUnitsMap.map {
        updateUnitsAndCreateXML
      }}
      </symbols>



    XML save("stocks2.xml", updatedStocksAndUnitsXML)
    println("The saved file contains " + (XML.load("stocks2.xml") \\ "symbol").size + " symbol elements")
  }
}
