package com.andoter.asm_example.part3

import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type

/*
前面 2.3 中介绍的 Type 工具类同样可以用作方法
 */
class TypeDemo {
    fun test() {

    }
}

fun main() {

    val type = Type.FLOAT_TYPE.getOpcode(Opcodes.IMUL)
    println(type)
    Type.getArgumentTypes(TypeDemo::class.java.getDeclaredMethod("test", null))
}