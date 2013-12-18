package org.kiji.express.memory

import com.twitter.scalding.{Job, IterableSource, NullSource, Tsv, Args}
import org.kiji.express.flow._
import org.kiji.express.flow.framework.hfile.{HFileKijiOutput, HFileKijiJob}

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

  val column = QualifiedColumnInputSpec(family, qualifier, all, paging = PagingSpec.Cells(1000))
  val output = Tsv(out)

  KijiInput(uri, Map(column -> 'a))
    .read
    .mapTo('a -> 'count) { a: Seq[FlowCell[_]] => a.length }
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

  val column = QualifiedColumnInputSpec(family, qualifier, all, paging = PagingSpec.Cells(1000))
  val output = Tsv(out)

  KijiInput(uri, Map(column -> 'a))
      .read
      .mapTo('a -> 'count) { a: Seq[FlowCell[_]] => a.length }
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

  val column = QualifiedColumnInputSpec(family, qualifier, all, paging = PagingSpec.Cells(1000))
  val output = Tsv(out)

  KijiInput(uri, Map(column -> 'a))
    .read
    .flatten[Seq[FlowCell[_]]]('a -> 'b)
    .groupAll { _.size }
    .write(output)
}

class WriteRandomLong(args: Args) extends KijiJob(args) {
  val family = args.getOrElse("family", "default")
  val inQualifier = args.getOrElse("inQualifier", "string")
  val outQualifier = args.getOrElse("outQualifier", "long")
  val uri = args.getOrElse("uri", "kiji://localhost:2181/default/memory_stress")

  val inColumn = QualifiedColumnInputSpec(family, inQualifier, all)
  val outColumn = QualifiedColumnOutputSpec(family, outQualifier)

  KijiInput(uri, Map(inColumn -> 'a))
    .read
    .map('a -> 'b) { a: Seq[_] => System.currentTimeMillis() }
    .write(KijiOutput(uri, Map('b -> outColumn)))
}

class HFile(args: Args) extends HFileKijiJob(args) {
  val family = args.getOrElse("family", "default")
  val inQualifier = args.getOrElse("inQualifier", "long")
  val outQualifier = args.getOrElse("outQualifier", "long")
  val uri = args.getOrElse("uri", "kiji://localhost:2181/default/memory_stress")

  @transient val inColumn = QualifiedColumnInputSpec(family, inQualifier, all)
  @transient val outColumn = QualifiedColumnOutputSpec(family, outQualifier)

  KijiInput(uri, Map(inColumn -> 'a))
      .read
//      .map('a -> 'b) { a: Seq[_] => System.currentTimeMillis() }
//      .write(HFileKijiOutput(uri, "hfiles1", Map('b -> outColumn)))
//      .write(HFileKijiOutput(uri, "hfiles2", Map('b -> outColumn)))
      .groupAll { group => group.size }
      .insert('entityId, EntityId(42))
      .write(HFileKijiOutput(uri, "hfiles1", Map('size -> outColumn)))

//  IterableSource(1 to 10)
//    .read
//    .write(NullSource)
}

class NullJob(args: Args) extends HFileKijiJob(args) {
  val p = IterableSource(1 to 10, 'a)
    .read
    .write(Tsv("fooz"))
    .write(Tsv("barz"))
}
