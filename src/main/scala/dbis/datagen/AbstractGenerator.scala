package dbis.datagen

import dbis.datagen.st.types.WKT

trait AbstractGenerator[+T <: WKT] extends Iterable[T]