// Command Line Parser

package org.elmarweber.github.bigquery

import java.io.File
import javax.sql.DataSource

import org.apache.commons.dbcp2.BasicDataSource

object Parser {
  case class Config(dbUrl: String = "jdbc:mysql://localhost:3306/test", username: String = "root", password: String = "", table: String = "", outDir: File = new File("./")) {
    def dataSource = {
      val ds = new BasicDataSource()
      ds.setDriverClassName("com.mysql.cj.jdbc.Driver")
      ds.setUrl(dbUrl)
      ds.setUsername(username)
      ds.setPassword(password)
      ds.setValidationQuery("select 1 from dual")
      ds
    }
  }

  val parser = new scopt.OptionParser[Config]("mysql2bigquery") {
    head("mysql2bigquery")

    opt[String]('d', "database-url")
      .required()
      .action( (x, c) => c.copy(dbUrl = x) )
      .text(s"the URL, e.g. 'jdbc:mysql://localhost:3306/test'")

    opt[String]('u', "username")
      .action( (x, c) => c.copy(username = x) )

    opt[String]('p', "password")
      .action( (x, c) => c.copy(password = x) )

    opt[String]('t', "table")
      .required()
      .action( (x, c) => c.copy(table = x) )

    opt[File]('o', "out")
      .required()
      .action( (x, c) => c.copy(outDir = x) )
      .validate(f => if (f.exists()) success else failure(s"Directory ${f.getAbsolutePath} does not exist"))
  }
}
