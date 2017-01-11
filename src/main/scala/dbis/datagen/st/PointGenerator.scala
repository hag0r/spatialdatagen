package dbis.datagen.st

import dbis.datagen.st.types.Point
import dbis.setm.SETM._
import dbis.datagen.BasicGenerators
import dbis.datagen.AbstractGenerator

/**
 * A generator for random points.
 * 
 * @param minX minimal x value
 * @param maxX maximal x value
 * @param minY minimal y value
 * @param maxY maximal y value 
 */
class PointGenerator(minX: Double, maxX: Double, minY: Double, maxY: Double, n: Long) extends AbstractGenerator[Point] {
  
  override def iterator = for(i <- (0L until n).iterator) yield PointGenerator.point(minX, maxX, minY, maxY) 
  
}

object PointGenerator {
  
  def point(minX: Double, maxX: Double, minY: Double, maxY: Double) = timing("single point") { Point(BasicGenerators.double(minX, maxX), BasicGenerators.double(minY, maxY))}
  
}
