package dbis.datagen.spatial

import org.scalacheck.Gen

object BasicGenerators {
  
  def double(min: Double, max: Double): Double = Gen.choose(min, max).sample.get
  def double(min: Double, max: Double, f: Double => Boolean) = Gen.choose(min, max).suchThat(f).sample.get  
  
  def int(min: Int, max: Int) = Gen.choose(min, max).sample.get  
  
}