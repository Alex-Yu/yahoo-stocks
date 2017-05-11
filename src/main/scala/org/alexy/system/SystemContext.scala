package org.alexy.system

import java.util.ResourceBundle

import org.alexy.domain.Domain
import org.alexy.utils.{Source, SourceImpl}

/**
  * Created by alex on 11.05.17.
  */
trait SystemContext {
  def parser: Source
  def domain: Domain
  def config: Config
}

class ProdContext extends SystemContext {
  override lazy val parser: Source = new SourceImpl(config)

  override lazy val domain: Domain = new Domain(parser)

  override lazy val config: Config = new Config
}

//TODO: Maybe unneccessary
class TestContext extends SystemContext {
  override lazy val parser: SourceImpl = ???

  override lazy val domain: Domain = ???

  override lazy val config: Config = null
}

class Config {
  private[this] lazy val rb = ResourceBundle.getBundle("config")

  lazy val urlMacrosO: Option[String] = Option(rb.getString("url"))
}
