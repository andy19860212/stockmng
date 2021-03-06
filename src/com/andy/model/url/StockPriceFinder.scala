package com.andy.model.url

import scala.io.Source
import scala.xml.XML

/**
 * Created by wb-zhangwei01 on 2014/7/29.
 */
object StockPriceFinder {
  /**
   * 通过股票名称获取股票价格
   * @param ticker
   * @return
   */
  def getLatestClosingPrice(ticker: String) = {
    val url = "http://ichart.finance.yahoo.com/table.csv?s=" + ticker + "&a=00&b=01&c=" + new java.util.Date().getYear
    val data = Source.fromURL(url).mkString
    val mostRecentData = data.split("\n")(1)
    val closingPrice = mostRecentData.split(",")(4).toDouble
    closingPrice
  }
}
