package org.alexy.utils

import scala.io.Source

/**
  * Created by alex on 11.05.17.
  */
object Sources {
  def getRawFromUrl(url: String): Iterator[String] = Source.fromURL(url).getLines()//TODO: Think about IOExceptions and BadRequests
  def getTestRaw(stub: String): Iterator[String] = ???
}
