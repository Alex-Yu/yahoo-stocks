package org.alexy

/**
  * Created by alex on 10.05.17.
  */

import org.alexy.system.{ProdContext, SystemContext}

object Main extends App {
  val systemContext: SystemContext = new ProdContext
  val domain = systemContext.domain

  // example usage
  val googleDailyPrices = domain.dailyPrices("GOOG")
  val googleDailyReturns = domain.returns("GOOG")
  val googleAverageReturns = domain.meanReturn("GOOG")

  println("----------------- daily prices -----------------")
  googleDailyPrices.foreach(println)
  println("----------------- daily returns -----------------")
  googleDailyReturns.foreach(x => println(f"$x%1.6f"))
  println("----------------- mean -----------------")
  println(f"$googleAverageReturns%4.6f")
}







