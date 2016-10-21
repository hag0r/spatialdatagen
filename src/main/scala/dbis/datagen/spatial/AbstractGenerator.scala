package dbis.datagen.spatial

import dbis.datagen.spatial.types.WKT

trait AbstractGenerator[+T <: WKT] extends Iterable[T]