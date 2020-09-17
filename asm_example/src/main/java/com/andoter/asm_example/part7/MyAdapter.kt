package com.andoter.asm_example.part7

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.MethodNode

class MyAdapter {
}

// 基于继承模式
class MyMethodAdapter(
    access: Int, name: String, desc: String, signature: String, exception: Array<String>,
    methodVisitor: MethodVisitor
) : MethodNode(access, name, desc, signature, exception) {
    override fun visitEnd() {
        // 自定义转换代码
        accept(mv)
    }
}


class MyMethodAdapter2(
    access: Int, name: String, desc: String, signature: String, exception: Array<String>,
    var methodVisitor: MethodVisitor
) : MethodVisitor(access, MethodNode(access, name, desc, signature, exception)) {



    override fun visitEnd() {
        // 自定义转换代码
        val mn = methodVisitor as MethodNode
        mn.accept(mv)
    }
}

fun main() {
    val classReader = ClassReader("java.lang.Runnable")
    classReader.accept(object : ClassVisitor(Opcodes.ASM7) {
        override fun visitMethod(
            access: Int,
            name: String,
            descriptor: String,
            signature: String,
            exceptions: Array<String>
        ): MethodVisitor {
            return object: MethodNode(Opcodes.ASM7, access, name, descriptor, signature, exceptions){
                override fun visitEnd() {
                    super.visitEnd()
                }
            }
        }
    }, ClassReader.SKIP_DEBUG)
}