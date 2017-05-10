package org.alexy

/**
  * Created by alex on 10.05.17.
  */

import java.util.Properties

import org.alexy.system.{ProdContext, SystemContext}

/**
  * Questions:
  * 1. Caching?
  * 2. What price: close or adjClose?
  * 3. In def prices: today argument means businessDate?
  */

object Main extends App {
  val systemContext: SystemContext = new ProdContext
  val domain = systemContext.domain

  // example usage
  val googleDailyPrices = domain.dailyPrices("GOOG")
  val googleDailyReturns = domain.returns("GOOG")
  val googleAverageReturns = domain.meanReturn("GOOG")

  println("-----------------daily prices:")
  googleDailyPrices.foreach(println)
  println("-----------------returns:")
  googleDailyReturns.foreach(println)
  println(s"-----------------mean:    $googleAverageReturns")
}







