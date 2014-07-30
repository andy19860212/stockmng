package com.andy.model.file

import java.io._


/**
 * Created by wb-zhangwei01 on 2014/7/29.
 */
object WriteFileTest {
  def main(args: Array[String]) {
    val writer = new PrintWriter(new File("symbols.txt"))
    writer write "APPL"
    writer close

  }
}
