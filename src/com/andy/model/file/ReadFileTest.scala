package com.andy.model.file

import scala.io._

/**
 * Created by wb-zhangwei01 on 2014/7/29.
 */
object ReadFileTest {
  def main(args: Array[String]) {
    println("*** The content of the file you read is:")
    Source.fromFile("ReadFileTest.txt").foreach {
      print
    }

    //to get each line call getLines() on Source instance
  }
}
