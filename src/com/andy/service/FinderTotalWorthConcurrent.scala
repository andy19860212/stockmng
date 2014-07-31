package com.andy.service

import java.util.Date

import com.andy.model.file.StockMngXMLHelper
import com.andy.model.url.StockPriceFinder
import scala.actors._
import Actor._

/**
 * Created by wb-zhangwei01 on 2014/7/30.
 * 添加并发控制
 * 消耗时间约####Took 10.736967 seconds####
 */
object FinderTotalWorthConcurrent extends App {

  val symbolsAndUnits = StockMngXMLHelper.getTickersAndUnits

  val caller = self
  println("Today is " + new Date())
  println("Ticker Units Closing Price($) Total Value($)")

  val startTime = System.nanoTime()

  symbolsAndUnits.keys.foreach {
    ticker =>
      actor {
        caller !(ticker, StockPriceFinder.getLatestClosingPrice(ticker))
      }
  }

  val networth = (0.0 /: (1 to symbolsAndUnits.size)) { (worth, index) =>
    receiveWithin(10000) {
      case (ticker: String, latestClosingPrice: Double) =>
        val units = symbolsAndUnits(ticker)
        val value = units * latestClosingPrice
        println("%-7s %-5d %-16f %f".format(ticker, units, latestClosingPrice, value))
        worth + value
      case TIMEOUT =>
        worth
    }
  }

  val endTime = System.nanoTime()
  //println("The total value of your inverstments is $ " + netWorth)
  println("Took %f seconds".format((endTime - startTime) / 1000000000.0))

}
