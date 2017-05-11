package org.alexy.system

import java.util.ResourceBundle

import org.alexy.domain.{Domain, DomainImpl}
import org.alexy.utils.{DataSource, DataSourceImpl}

/**
  * Created by alex on 11.05.17.
  */
trait SystemContext {
  def source: DataSource
  def domain: Domain
  def config: Config
}

class ProdContext extends SystemContext {
  override lazy val source: DataSource = new DataSourceImpl(config)

  override lazy val domain: Domain = new DomainImpl(source)

  override lazy val config: Config = new Config
}

//TODO: Maybe unneccessary
class TestContext extends SystemContext {
  override lazy val source: DataSourceImpl = ???

  override lazy val domain: DomainImpl = ???

  override lazy val config: Config = null
}

class Config {
  private[this] lazy val rb = ResourceBundle.getBundle("config")

  lazy val urlMacrosO: Option[String] = Option(rb.getString("url"))
}
