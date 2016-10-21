package dbis.datagen.spatial

import dbis.datagen.spatial.types.Point

class PointGenerator(min: Double, max: Double, n: Long) extends AbstractGenerator[Point] {
  
  override def iterator = for(i <- (0L until n).iterator) yield PointGenerator.point(min, max) 
  
}

object PointGenerator {
  
  def point(min: Double, max: Double) = Point(BasicGenerators.double(min, max), BasicGenerators.double(min, max))
  
}
