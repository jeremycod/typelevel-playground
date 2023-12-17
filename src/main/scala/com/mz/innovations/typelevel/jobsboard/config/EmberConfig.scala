package com.mz.innovations.typelevel.jobsboard.config

import pureconfig.*
import pureconfig.generic.derivation.default.*
import pureconfig.error.CannotConvert
import com.comcast.ip4s.Host
import com.comcast.ip4s.Port

object EmberConfig {
  given host: ConfigReader[Host] = ConfigReader[String].emap { hostString =>
    Host
      .fromString(hostString)
      .toRight(
        CannotConvert(hostString, Host.getClass.toString, s"Invalid host string: $hostString")
      )
  }

  given port: ConfigReader[Port] = ConfigReader[Int].emap { portInt =>
    Port
      .fromInt(portInt)
      .toRight(
        CannotConvert(portInt.toString, Port.getClass.toString, s"Invalid port number: $portInt")
      )

  }
}

final case class EmberConfig(host: Host, port: Port) derives ConfigReader
