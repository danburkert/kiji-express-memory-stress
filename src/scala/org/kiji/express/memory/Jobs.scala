package org.kiji.express.memory

import com.twitter.scalding.{Tsv, Args}
import org.kiji.express.flow._

/**
 * Counts the number of versions in each row of a column.
 *
 * @param args should contain 'input', 'family', 'column', and 'output'
 */
class CountColumnVersions(args: Args) extends KijiJob(args) {
  val column = QualifiedColumnInputSpec(args("family"), args("column"), all, paging = PagingSpec.Cells(1000))
  val output = Tsv("output")

  KijiInput(args("input"), Map(column -> 'a))
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
  val column = QualifiedColumnInputSpec(args("family"), args("column"), all, paging = PagingSpec.Cells(1000))
  val output = Tsv("output")

  KijiInput(args("input"), Map(column -> 'a))
    .read
    .mapTo('a -> 'counts) { a: Seq[FlowCell[_]] => a.length -> a.length }
    .write(output)
}

/**
 * Flattens all versions in each row of a column, and then counts them.
 *
 * @param args should contain 'input', 'family', 'column', and 'output'
 */
class FlattenCount(args: Args) extends KijiJob(args) {
  val column = QualifiedColumnInputSpec(args("family"), args("column"), all, paging = PagingSpec.Cells(1000))
  val output = Tsv("output")

  KijiInput(args("input"), Map(column -> 'a))
    .read
    .flatten[Seq[FlowCell[_]]]('a -> 'b)
    .groupAll { _.size }
    .write(output)
}
