import scala.io.Source
import scala.io.BufferedSource
import scala.collection.SortedMap
import scala.collection.mutable.ListMap
import scala.util.control.Breaks.{break, breakable, tryBreakable}

// scalac Main.scala && scala Main
object Main {


    def main(args: Array[String]) = {

      var lines = Source.fromFile("full.txt")
        .getLines()

      val transform = (line : String) => line.split(" ")
        .map(_.toIntOption)
        .filter(_.isDefined)
        .map(_.get)
        .toList
        .mkString

      val duration = transform(lines.next()).toInt
      val distanceToBeat =  transform(lines.next()).toLong

      val computeDistance = (duration: Int,  hold: Int) => {
        var remainingTime = (duration - hold) * 1
        var volocity = if (hold == 0 || hold == duration) 0 else hold
        remainingTime.toLong * volocity.toLong
      }

      var bottom = 0L
      var top = 0L

      breakable {
        for (hold <- (0 to duration by 1)) {

          var distance = computeDistance(duration, hold)

          if (distance > distanceToBeat) {
            println(s"Found bottom is $hold")
            bottom = hold
            break()
          } 
        }
      }

      breakable {
        for (holdInverted <- (duration to 0 by -1)) {

          var distance = computeDistance(duration, holdInverted)

          if (distance > distanceToBeat) {
            println(s"Found top is $holdInverted")
            top = holdInverted
            break()
          } 
        }
      }


      var total = 0
      for (i <- bottom to top) {
        total += 1
      }

      print(s"Result for bottom $bottom and top $top nb of possibilities is $total")

      /* val result = races.map { case (duration, distance) => {

        (0 to duration map { case (hold) =>

          println(s"Hold for $hold out $duration")

          var remainingTime = (duration - hold) * 1
          var volocity = if (hold == 0 || hold == duration) 0 else hold
          var maxDistance = remainingTime.toLong * volocity.toLong

          var compare = maxDistance > distance

          println(s"$maxDistance > $distance = $compare")

          if (compare) {
            Some((hold, maxDistance))
          } else {
            None
          }

        } filter { _.isDefined }).length

      }}

      println(result) */
    }
}