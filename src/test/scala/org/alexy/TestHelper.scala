package org.alexy

import java.time.LocalDate
import org.alexy.models.Row

/**
  * Created by alex on 11.05.17.
  */
trait TestHelper {
  val ticker = "GOOG"

  val url = "http://dummy.com"
  val urlOnlyHeaders = "http://empty.com"
  val urlBadRow = "http://partly-bad.com"

  val rawData = Iterator(
    "Date,Open,High,Low,Close,Volume,Adj Close",
    "2016-11-01,100.00,120.00,110.00,120.00,1000,120.00",
    "2016-11-02,130.00,150.00,110.00,140.00,2000,140.00",
    "2016-11-05,50.00,80.00,50.00,120.00,10000,120.00",
    "2016-11-03,200.00,250.00,110.00,200.00,5000,200.00",
    "2016-11-04,100.00,120.00,110.00,120.00,1000,120.00"
  )

  val rawDataWithOnlyHeaders = Iterator(
    "Date,Open,High,Low,Close,Volume,Adj Close"
  )

  val rawDataWithBadRow = Iterator(
    "Date,Open,High,Low,Close,Volume,Adj Close",
    "2016-11-01,100.00,120.00,110.00,120.00,1000,120.00",
    "2016-11-02,130-00,150.00,110.00,140.00,2000,140.00"
  )

  val parsedData = Seq(
    Row(LocalDate.of(2016, 11, 1), 100.00, 120.00, 110.00, 120.00, 1000, 120.00),
    Row(LocalDate.of(2016, 11, 2), 130.00, 150.00, 110.00, 140.00, 2000, 140.00),
    Row(LocalDate.of(2016, 11, 5), 50.00, 80.00, 50.00, 120.00, 10000, 120.00),
    Row(LocalDate.of(2016, 11, 3), 200.00, 250.00, 110.00, 200.00, 5000, 200.00),
    Row(LocalDate.of(2016, 11, 4), 100.00, 120.00, 110.00, 120.00, 1000, 120.00)
  )

  val delta = 0.00000001
}
