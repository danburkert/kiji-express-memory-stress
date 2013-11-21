package org.kiji.express.memory

import com.twitter.scalding.{Tsv, Args}
import org.kiji.express.{PagedKijiSlice, KijiSlice}
import org.kiji.express.flow.{All, KijiOutput, QualifiedColumn, KijiInput, KijiJob}

/**
 * Counts the number of versions in each row of a column.
 *
 * @param args should contain 'input', 'family', 'column', and 'output'
 */
class CountColumnVersions(args: Args) extends KijiJob(args) {
  val family = args.getOrElse("family", "default")
  val qualifier = args.getOrElse("qualifier", "string")
  val uri = args.getOrElse("uri", "kiji://localhost:2181/default/memory_stress")
  val out = args.getOrElse("output", "output/count-column-versions")

  val column = QualifiedColumn(family, qualifier).withPaging(1000).useDefaultReaderSchema()
  val output = Tsv(out)

  KijiInput(uri)(Map(column -> 'a))
    .read
    .mapTo('a -> 'count) { a: PagedKijiSlice[_] => a.size }
    .write(output)
}

/**
 * Counts the number of versions in each row of a column, twice.
 *
 * @param args should contain 'input', 'family', 'column', and 'output'
 */
class CountColumnVersionsTwice(args: Args) extends KijiJob(args) {
  val family = args.getOrElse("family", "default")
  val qualifier = args.getOrElse("qualifier", "string")
  val uri = args.getOrElse("uri", "kiji://localhost:2181/default/memory_stress")
  val out = args.getOrElse("output", "output/count-column-versions-twice")

  val column = QualifiedColumn(family, qualifier).withPaging(1000).useDefaultReaderSchema()
  val output = Tsv(out)

  KijiInput(uri)(Map(column -> 'a))
      .read
      .mapTo('a -> 'count) { a: PagedKijiSlice[_] => a.size }
      .write(output)
}

/**
 * Flattens all versions in each row of a column, and then counts them.
 *
 * @param args should contain 'input', 'family', 'column', and 'output'
 */
class CountColumnCells(args: Args) extends KijiJob(args) {
  val family = args.getOrElse("family", "default")
  val qualifier = args.getOrElse("qualifier", "string")
  val uri = args.getOrElse("uri", "kiji://localhost:2181/default/memory_stress")
  val out = args.getOrElse("output", "output/count-column-cells")

  val column = QualifiedColumn(family, qualifier).withPaging(1000).useDefaultReaderSchema()
  val output = Tsv(out)

  KijiInput(uri)(Map(column -> 'a))
    .read
    .mapTo('a -> 'b) { a: KijiSlice[_] => a.cells }
    .flattenTo[Seq[_]]('b -> 'c)
    .groupAll { _.size }
    .write(output)
}

class WriteRandomLong(args: Args) extends KijiJob(args) {
  val family = args.getOrElse("family", "default")
  val inQualifier = args.getOrElse("inQualifier", "string")
  val outQualifier = args.getOrElse("outQualifier", "long")
  val uri = args.getOrElse("uri", "kiji://localhost:2181/default/memory_stress")

  val inColumn = QualifiedColumn(family, inQualifier).useDefaultReaderSchema()
  val outColumn = QualifiedColumn(family, outQualifier).useDefaultReaderSchema()

  KijiInput(uri)(Map(inColumn -> 'a))
    .read
    .map('a -> 'b) { a: KijiSlice[_] => System.currentTimeMillis() }
    .write(KijiOutput(uri)(Map('b -> outColumn)))

}
