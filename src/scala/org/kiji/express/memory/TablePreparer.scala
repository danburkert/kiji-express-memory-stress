package org.kiji.express.memory

import scala.util.Random

import org.kiji.express.flow.util.Resources._
import org.kiji.schema.Kiji
import org.kiji.schema.KijiBufferedWriter
import org.kiji.schema.KijiTable
import org.kiji.schema.KijiURI
import org.kiji.schema.shell.api.Client

class TablePreparer(uri: KijiURI, table: String) {
  def create(): Unit = {
    doAndClose(Client.newInstance(uri)) { client =>
      val stream = this.getClass.getClassLoader.getResourceAsStream("ddl/memory_stress.ddl")
      client.executeStream(stream)
    }
  }

  val valueGen: Map[String, () => Any] = {
    val rand: Random = new Random()
    Map("string" -> (() => rand.nextString(1024)),
        "longs" -> (() => rand.nextLong))
  }

  def load(numRows: Int, family: String, columns: Iterable[String], versions: Int): Unit = {
    doAndRelease(Kiji.Factory.open(uri)) { kiji: Kiji =>
      doAndRelease(kiji.openTable(table)) { table: KijiTable =>
        doAndClose(table.getWriterFactory.openBufferedWriter()) { writer: KijiBufferedWriter =>
          for (
            eid <- 0 until numRows;
            column <- columns;
            version <- 0 until versions
          ) {
            val entityId = table.getEntityId(eid: java.lang.Integer)
            writer.put(entityId, family, column, version, valueGen(column)())
          }
        }
      }
    }
  }

  def drop(): Unit = {
    doAndRelease(Kiji.Factory.open(uri)) { kiji: Kiji =>
      kiji.deleteTable(table)
    }
  }
}

object TablePreparer {

  case class Opts(
      create: Boolean = false,
      drop: Boolean = false,
      load: Boolean = false,
      rows: Int = 10000,
      versions: Int = 1,
      columns: Iterable[String] = List("string"),
      uri: KijiURI = KijiURI.newBuilder("kiji://localhost:2181/default/memory_stress").build)

  val parser = new scopt.OptionParser[Opts]("prepare") {
    opt[Unit]("create") action { (_, o) =>
      o.copy(create = true) } text("create the table.")
    opt[Unit]("drop") action { (_, o) =>
      o.copy(drop = true) } text("drop the table.")
    opt[Unit]("load") action { (_, o) =>
      o.copy(load = true) } text("load table with data.")
    opt[Int]("rows") action { (i, o) =>
      o.copy(rows = i) } text("number of rows to create.")
    opt[Int]("versions") action { (i, o) =>
      o.copy(versions = i) } text("number of versions per row.")
    opt[String]("columns") action { (s, o) =>
      o.copy(columns = s.split(',')) } text("columns to load, comma separated.")
    opt[String]("uri") action { (s, o) =>
      o.copy(uri = KijiURI.newBuilder(s).build) } text("URI of Kiji instance.")
  }

  def main(args: Array[String]) {
    val opts = parser.parse(args, Opts()).get
    val preparer = new TablePreparer(opts.uri, "memory_stress")

    if (opts.create) {
      preparer.create
    }

    if (opts.load) {
      val rows: Int = opts.rows
      val versions: Int = opts.versions
      val columns: Iterable[String] = opts.columns
      preparer.load(rows, "default", columns, versions)
    }

    if (opts.drop) {
      preparer.drop
    }
  }
}
