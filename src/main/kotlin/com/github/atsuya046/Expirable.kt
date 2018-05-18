package com.github.atsuya046

import kotlin.reflect.KProperty

/**
 * Provide ReadWriteProperty has expiration rule.
 */
abstract class Expirable<T>(private val default: T) {

    var value: T = default

    /**
     * Expiration rule.
     * If this method returns true, [getValue] will returns null.
     *
     * @return if expired return true
     */
    protected abstract fun isExpired(): Boolean

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        if (isExpired()) this.value = default
        return this.value
    }

    abstract operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T)

    companion object {
        fun <T> withLifetime(lifeSpanMillSec: Long, default: T) : Expirable<T> = HasLifeTime(lifeSpanMillSec, default)
    }
}

internal class HasLifeTime<T>(private val lifeSpanMillSec: Long, default: T): Expirable<T>(default) {

    private var limitation: Long = now() + lifeSpanMillSec

    override fun isExpired(): Boolean = now() > limitation

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.limitation = now() + lifeSpanMillSec
        this.value = value
    }

    private fun now() = System.currentTimeMillis()
}