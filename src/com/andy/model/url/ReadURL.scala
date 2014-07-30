package com.andy.model.url

import java.net.URL

import scala.io._

/**
 * Created by wb-zhangwei01 on 2014/7/29.
 */
object ReadURL {
  def main(args: Array[String]) {
    val source = Source.fromURL(new URL("http://www.scala-lang.org/docu/files/api/index.html"))
    println(source.getLines().length)

    val content = source.mkString
    val VersionRegEx = """[\D\S]+scaladoc\s+\(version\s+(.+)\)[\D\S]+""".r
    content match {
      case VersionRegEx(version) => println("Scala doc for version: "+version)
    }
  }
}
