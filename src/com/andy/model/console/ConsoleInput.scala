package com.andy.model.console

/**
 * Created by wb-zhangwei01 on 2014/7/29.
 */
object ConsoleInput {
  def main(args: Array[String]) {
    println("Please enter a ticker symbol:")
    val symbol = Console.readLine
    println("Got it , you own the ticker symbol :" + symbol)

  }
}
