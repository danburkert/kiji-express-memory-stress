package org.kiji.express.memory

import scala.io.Source
import scala.util.Random

import org.kiji.express.flow.util.Resources._
import org.kiji.schema.Kiji
import org.kiji.schema.KijiBufferedWriter
import org.kiji.schema.KijiTable
import org.kiji.schema.KijiURI
import org.kiji.schema.shell.api.Client
import com.twitter.scalding.Tool
import org.apache.hadoop.util.ToolRunner
import org.apache.hadoop.conf.Configuration
import org.kiji.schema.util.InstanceBuilder

class TablePreparer(uri: KijiURI, table: String) {
  def create(): Unit = {
    doAndClose(Client.newInstance(uri)) { client =>
      val stream = this.getClass.getClassLoader.getResourceAsStream("ddl/memory_stress.ddl")
      client.executeStream(stream)
    }
  }

  val valueGen: Map[String, () => Any] = {
    val rand: Random = new Random()
    Map("string" -> (() => rand.nextString(1024)))
  }

  def populate(numRows: Int, family: String, columns: Iterable[String], versions: Int): Unit = {
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

  def main(args: Array[String]) {
    val tool = new Tool
    tool.setConf(new Configuration())
    val opts = tool.parseModeArgs(args)._2

    val uri: KijiURI = if (opts.boolean("fake")) {
      new InstanceBuilder("default").build().getURI
    } else {
      KijiURI.newBuilder(opts.getOrElse("uri", "kiji://localhost:2181/default/memory_stress")).build
    }

    val conf = doAndRelease(Kiji.Factory.open(uri))(kiji => kiji.getConf)

    val preparer = new TablePreparer(uri, "memory_stress")

    if (opts.boolean("prepare")) {
      val rows: Int = opts.getOrElse("rows", "10000").toInt
      val versions: Int = opts.getOrElse("versions", "1").toInt
      val columns: Iterable[String] = opts.list("columns")
      preparer.create
      preparer.populate(rows, "default", columns, versions)
    }

    ToolRunner.run(conf, new Tool, args)

    if (opts.boolean("cleanup")) {
      preparer.drop
    }
  }
}
