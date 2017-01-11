package dbis.datagen.st.types

case class Interval(start: Long, end: Long) extends WKT {
  
  
  require(start <= end, s"start ($start) must be <= end ($end)")
  
  override def toString() = s"(${mkString(';')})"
  
  def mkString(sep: Char = ';') = s"${start}${sep}${end}"
  
  def interval = (start, end)
  
  def wkt = s"INTERVAL(${start},${end})"
  
  
}