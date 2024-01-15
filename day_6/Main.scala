import scala.io.Source
import scala.io.BufferedSource
import scala.collection.SortedMap
import scala.collection.mutable.ListMap

// scalac Main.scala && scala Main
object Main {

    def main(args: Array[String]) = {

      var lines = Source.fromResource("full.txt")
        .getLines()

      val transform = (line : String) => line.split(" ")
        .map(_.toIntOption)
        .filter(_.isDefined)
        .map(_.get)
        .toList

      val durations = transform(lines.next())
      val distances =  transform(lines.next())

      var races = durations.zipWithIndex.map{ case (duration, index) => (duration, distances(index)) }
      
      val result = races.map { case (duration, distance) => {

        (0 to duration map { case (hold) =>

          var remainingTime = (duration - hold) * 1
          var volocity = if (hold == 0 || hold == duration) 0 else hold
          var maxDistance = remainingTime * volocity

          if (maxDistance > distance) {
            Some((hold, maxDistance))
          } else {
            None
          }

        } filter { _.isDefined }).length

      }}

      println(result.reduce((x, y) => x * y))
    }
}