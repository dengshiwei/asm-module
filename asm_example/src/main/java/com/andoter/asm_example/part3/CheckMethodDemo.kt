package com.andoter.asm_example.part3

import org.objectweb.asm.*
import org.objectweb.asm.util.CheckMethodAdapter

/**
 * 在前面的章节中，我们了解到 CheckClassAdapter 用于检查 ClassVisitor 方法调用的顺序是否正确，参数是否有效。
 * 同样，对于 MethodVisitor 的方法调用，可以使用 CheckMethodVisitor 来检查一个方法。
 */
class CheckMethodDemo {

    fun checkMethod() {
        println("check method is ok")
    }
}

fun main() {
    val classReader = ClassReader("com.andoter.asm_example.part3.MyMethodAdapter")
    val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
    val classVisitor = object : ClassVisitor(Opcodes.ASM7, classWriter) {
        override fun visitMethod(
            access: Int,
            name: String?,
            descriptor: String?,
            signature: String?,
            exceptions: Array<out String>?
        ): MethodVisitor {
            var methodVisitor = cv.visitMethod(access, name, descriptor, signature, exceptions)
            methodVisitor = CheckMethodAdapter(methodVisitor)
            return MyMethodAdapter(methodVisitor)
        }
    }
    classReader.accept(classVisitor, ClassReader.SKIP_DEBUG)
}