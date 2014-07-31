package com.andy.service

import java.util.Date

import com.andy.model.file.StockMngXMLHelper
import com.andy.model.url.StockPriceFinder

/**
 * Created by wb-zhangwei01 on 2014/7/29.
 * 单线程串行的获取网络数据的方式
 * 大概耗时###########Took 76.153774 seconds###
 *
 */
object FinderTotalWorthSequential {
  def main(args: Array[String]) {
    val symbolsAndUnits = StockMngXMLHelper.getTickersAndUnits
    println("Today is " + new Date())
    println("Ticker Units Closing Price($) Total Value($)")
    val startTime = System.nanoTime()
    val netWorth = (0.0 /: symbolsAndUnits) {
      (worth, symbolAndUnits) =>
        val (ticker, units) = symbolAndUnits
        val latestClosingPrice = StockPriceFinder getLatestClosingPrice ticker
        val value = units * latestClosingPrice

        println("%-7s %-5d %-16f %f".format(ticker, units, latestClosingPrice, value))
        worth + value
    }

    val endTime = System.nanoTime()

    println("The total value of your inverstments is $ " + netWorth)
    println("Took %f seconds".format((endTime - startTime) / 1000000000.0))
  }
}
