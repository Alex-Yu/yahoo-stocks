package org.alexy.system

import java.util.ResourceBundle

import org.alexy.domain.Domain
import org.alexy.utils.{Parser, Sources}

/**
  * Created by alex on 11.05.17.
  */
trait SystemContext {
  def parser: Parser
  def domain: Domain
  def config: Config
}

class ProdContext extends SystemContext {
  override lazy val parser: Parser = new Parser(Sources.getRawFromUrl, config)

  override lazy val domain: Domain = new Domain(parser)

  override lazy val config: Config = new Config
}

class TestContext extends SystemContext {
  override lazy val parser: Parser = ???

  override lazy val domain: Domain = ???

  override lazy val config: Config = null
}

class Config {
  private[this] lazy val rb = ResourceBundle.getBundle("config")
  lazy val urlMacrosO: Option[String] = Option(rb.getString("url"))
}
