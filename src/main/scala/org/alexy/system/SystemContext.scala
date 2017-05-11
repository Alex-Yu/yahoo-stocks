package org.alexy.system

import java.util.ResourceBundle
import org.alexy.domain.{Domain, DomainImpl}
import org.alexy.utils.{DataSource, DataSourceImpl, Parser, ParserImpl}

/**
  * Created by alex on 11.05.17.
  */
trait SystemContext {
  def config: Config
  def parser: Parser
  def dataSource: DataSource
  def domain: Domain
}

class ProdContext extends SystemContext {
  override lazy val config: Config = new Config

  override lazy val parser: Parser = new ParserImpl

  override lazy val dataSource: DataSource = new DataSourceImpl(config)

  override lazy val domain: Domain = new DomainImpl(parser)(dataSource)
}

class Config {
  private[this] lazy val rb = ResourceBundle.getBundle("config")

  lazy val urlMacrosO: Option[String] = Option(rb.getString("url"))
}
