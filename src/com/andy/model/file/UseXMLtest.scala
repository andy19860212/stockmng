package com.andy.model.file

/**
 * Created by wb-zhangwei01 on 2014/7/29.
 */
object UseXMLtest {
  def main(args: Array[String]) {
    val xmlFragment =
    <symbols>
      <symbol ticker="APPL"><units>200</units></symbol>
      <symbol ticker="IBM"><units>215</units></symbol>
    </symbols>

    println("Ticker\tUnits")
    xmlFragment match {
      case <symbols>{symbolNodes @ _*}</symbols> =>
        for (symbolNode @ <symbol>{_*}</symbol> <- symbolNodes){
          println("%-7s %s".format(
          symbolNode \ "@ticker",(symbolNode \ "units").text
          ))
        }
    }
  }
}
