package io.github.gubarsergey.accounting.redux

interface Mapper<A,B> {
    fun map(a: A): B
}