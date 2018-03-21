# expirable-property

[![CircleCI](https://circleci.com/gh/atsuya046/expirable-property.svg?style=svg)](https://circleci.com/gh/atsuya046/expirable-property) [![](https://jitpack.io/v/atsuya046/expirable-property.svg)](https://jitpack.io/#atsuya046/expirable-property) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/4a92b4d31426402faeab3e08fa8fb247)](https://www.codacy.com/app/atsuya046/expirable-property?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=atsuya046/expirable-property&amp;utm_campaign=Badge_Grade)

expirable-property provides property with expiration.

# Install

Add this to your build.gradle.

```
repositories {
    ...
    maven { url 'https://jitpack.io' }
}

dependencies {
    compile 'com.github.atsuya046:expirable-property:1.0.0'
}
```

# How to use

You can use built in delegate property like below.
```kotlin
// This value will expira after 1000 mill sec.
val value : String? by Expirable.lifetime(1000L) { "hello" /** initializer **/ }
println(value) // => hello

Thread.sleep(2000L)

println(value) // => null

```

And you can create custom delegate properties.
```kotlin
// This provides null, if you are referenced once.
class OneTimeReferenceable(initializer: () -> T? = { null }) : Expirable<T>() {
    init { value = initializer() }
    
    private var refCount = 0
    
    override fun isExpired(): Boolean = refCount > 1
    
    override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        refCount += 1
        return super.getValue(thisRef, property)
    }
}

var value: String? by OneTimeReferenceable({ "Hello" })

println(value) // => Hello

println(value) // => null
```

# License

This software is released under the MIT License, see LICENSE file.