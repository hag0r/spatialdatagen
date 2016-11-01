package dbis.datagen.spatial.types

case class Polygon(points: Seq[Point]) extends WKT {
  override def toString() = points.mkString(",")
  
  override def wkt = s"POLYGON((${points.map { p => p.toString() }.mkString(",")}))"
}
