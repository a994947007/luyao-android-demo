package com.hc.base.framework

import java.util.NoSuchElementException
import java.util.ServiceLoader

interface Syncable<T: Any> {
    fun key(): String

    fun onSync(target: T)
}

interface SyncableRegister {
    fun targetClass(): Class<*>

    fun <T: Any> create(t: T): Syncable<T>
}

object SyncableManager {
    fun <T: Any> create(target: T): Syncable<T> {
        val registers = ServiceLoader.load(SyncableRegister::class.java)
        for (register in registers) {
            if (target.javaClass == register.targetClass()) {
                return register.create(target)
            }
        }
        throw NoSuchElementException("你没有创建对应的Syncable")
    }
}