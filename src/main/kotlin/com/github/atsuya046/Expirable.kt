package com.github.atsuya046

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Provide ReadWriteProperty has expiration rule.
 */
abstract class Expirable<T>: ReadWriteProperty<Any?, T?> {
    var value: T? = null

    /**
     * Expiration rule.
     * If this method returns true, [getValue] will returns null.
     *
     * @return if expired return true
     */
    protected abstract fun isExpired(): Boolean

    override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        if (isExpired()) this.value = null
        return this.value
    }

    companion object {
        fun <T> withLifetime(lifeSpanMillSec: Long, initializer: () -> T?): Expirable<T> = HasLifeTime(lifeSpanMillSec, initializer)
    }
}

internal class HasLifeTime<T>(private val lifeSpanMillSec: Long, initializer: () -> T? = { null }): Expirable<T>() {
    init {
        value = initializer()
    }

    private var limitation: Long = now() + lifeSpanMillSec

    override fun isExpired(): Boolean = now() > limitation

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        this.limitation = now() + lifeSpanMillSec
        this.value = value
    }

    private fun now() = System.currentTimeMillis()
}