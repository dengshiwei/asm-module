package com.andoter.asm_example.part2

import jdk.internal.org.objectweb.asm.Type

class TypeDemo {
    fun testMethod(value: String):Int {
        return value.length
    }
}

/**
 * Type 类型是 ASM 提供的一个辅助类，用于对内部类型转换
 */
fun main() {
    // getInternalName 方法返回一个 Type 的内部名。例如，
    // Type.getType(String.class). getInternalName()给出 String 类
    val getInternalName = Type.getType(String::class.java).internalName
    val getInternalName2 = Type.getInternalName(String::class.java)
    println("$getInternalName,$getInternalName2")
    // getDescriptor 方法返回一个 Type 的述符。
    val getDescriptor = Type.getDescriptor(String::class.java)
    val getDescriptor2 = Type.getType(String::class.java).descriptor
    println("$getDescriptor,$getDescriptor2")

    // Type 获取方法的描述符，传入 Method 对象
    val getMethodDescriptor = Type.getMethodDescriptor(TypeDemo::class.java.getDeclaredMethod("testMethod", String::class.java))
    println("getMethodDescriptor = $getMethodDescriptor")
    // Type 获取方法的描述符，传入方法的返回值类型和参数类型
    val getMethodDescriptor2 = Type.getMethodDescriptor(Type.INT_TYPE, Type.LONG_TYPE)
    println("getMethodDescriptor2 = $getMethodDescriptor2")

    val getArgumentType = Type.getArgumentTypes(TypeDemo::class.java.getDeclaredMethod("testMethod", String::class.java))
    println("getArgumentType = ${getArgumentType[0]}")

    val getReturnType = Type.getReturnType(TypeDemo::class.java.getDeclaredMethod("testMethod", String::class.java))
    println("getReturnType = $getReturnType")
}