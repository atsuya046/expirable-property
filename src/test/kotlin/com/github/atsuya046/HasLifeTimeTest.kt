package com.github.atsuya046

import org.junit.Assert
import org.junit.Test

class HasLifeTimeTest {
    @Test
    fun isNotExpiredInLifeSpan() {
        val value: Int? by Expirable.withLifetime(2L) { 1 }
        Thread.sleep(1)
        Assert.assertTrue(value == 1)
    }

    @Test
    fun isExpiredOutOfLifeSpan() {
        val value: Int? by Expirable.withLifetime(0L) { 1 }
        Thread.sleep(1)
        Assert.assertTrue(value == null)
    }
}