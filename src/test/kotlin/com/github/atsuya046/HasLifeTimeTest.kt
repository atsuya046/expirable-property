package com.github.atsuya046

import org.junit.Assert
import org.junit.Test

class HasLifeTimeTest {
    @Test
    fun test() {
        val default = 1
        var value: Int by Expirable.withLifetime(2L, default)
        value = 100
        Thread.sleep(1)
        Assert.assertTrue(value == 100)

        Thread.sleep(2)
        Assert.assertTrue(value == default)
    }
}