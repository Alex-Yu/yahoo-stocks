package org.alexy.models

import java.time.LocalDate

import scala.util.Try

/**
  * Created by alex on 11.05.17.
  */
case class Row(date: LocalDate, open: Double, high: Double, low: Double, close: Double, volume: Long, adjClose: Double)

object Row {
  def from(raw: String): Option[Row] = {
    val cols = raw.split(",")
    Try {
      Row(
        date = LocalDate.parse(cols(0).trim),
        open = cols(1).trim.toDouble,
        high = cols(2).trim.toDouble,
        low = cols(3).trim.toDouble,
        close = cols(4).trim.toDouble,
        volume = cols(5).trim.toLong,
        adjClose = cols(6).trim.toDouble
      )
    }.toOption
  }
}