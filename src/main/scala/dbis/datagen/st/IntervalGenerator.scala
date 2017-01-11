package dbis.datagen.st

import dbis.datagen.st.types.Interval
import dbis.datagen.BasicGenerators
import dbis.datagen.AbstractGenerator

class IntervalGenerator(global: Interval, n: Long) extends AbstractGenerator[Interval] {
  
  require(n > 0, "number of intervals to generate must be > 0")
  
  override def iterator = for(i <- (0L until n).iterator) yield createInterval
  
  def createInterval = {
    
    val start = BasicGenerators.long(global.start, global.end)
    val end = BasicGenerators.long(start, global.end)
    
    Interval(start,end)
    
  }
  
}