package advent2018.common

fun <T> Iterator<T>.scan(transform: (acc: T, elem: T) -> T): Sequence<T> =
    if (!hasNext()) sequenceOf() else scan(next(), transform).asSequence()

fun <T> Sequence<T>.scan(transform: (acc: T, elem: T) -> T): Sequence<T> = iterator().scan(transform)
fun <T> Iterable<T>.scan(transform: (acc: T, elem: T) -> T): Iterable<T> = iterator().scan(transform).asIterable()

fun <T, R> Iterable<T>.scan(initial: R, transform: (acc: R, elem: T) -> R): Iterable<R> =
    Iterable { iterator().scan(initial, transform) }

private fun <T, R> Iterator<T>.scan(initial: R, transform: (acc: R, elem: T) -> R): Iterator<R> =
    object : Iterator<R> {
        val it = this@scan.iterator()
        var last: R = initial

        override fun next(): R {
            last = transform(last!!, it.next())
            return last!!
        }

        override fun hasNext(): Boolean = it.hasNext()
    }
