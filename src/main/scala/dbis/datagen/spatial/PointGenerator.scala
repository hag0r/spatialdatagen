package dbis.datagen.spatial

import org.scalacheck.Gen
import org.scalacheck.Arbitrary
import org.scalacheck.Prop.{forAll, BooleanOperators}
import dbis.datagen.spatial.types.Point
import scala.collection.mutable.ArrayBuffer
import org.scalacheck.rng.Seed

class PointGenerator(min: Double, max: Double, n: Long) extends AbstractGenerator[Point] {
  
  private[datagen] val pointGen = for {
    x <- Gen.choose(min, max)
    y <- Gen.choose(min, max)
  } yield Point(x,y)
  
  override def iterator = for(i <- (0L until n).iterator) yield pointGen.sample.get
  
}