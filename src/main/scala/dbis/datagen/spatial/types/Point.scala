package dbis.datagen.spatial.types

case class Point(x: Double, y: Double) extends WKT {
  
  override def toString = s"$x $y"
  
  override def wkt = s"POINT($x $y)"
  
}

