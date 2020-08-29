package com.andoter.asm_example.part3

import org.objectweb.asm.*
import org.objectweb.asm.util.Textifier
import org.objectweb.asm.util.TraceMethodVisitor
import java.util.*


/*
TraceMethodVisitor 用于跟踪某一方法链接调用处的内容，而不是跟踪类的所有类容。
 */
class TraceMethodVisitorDemo {
    fun visitorMethod() {
        println("访问方法的内容")
        println("当前事件 = ${Date().time}")
    }
}

fun main() {
    val classReader = ClassReader("com.andoter.asm_example.part3.TraceMethodVisitorDemo")
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
            if (methodVisitor != null) {
                val printer = object : Textifier(Opcodes.ASM7) {
                    override fun visitMethodEnd() {
                        super.visitMethodEnd()
                        println(this.stringBuilder.toString())
                    }
                }
                methodVisitor = MyMethodAdapter(TraceMethodVisitor(methodVisitor, printer))
                return methodVisitor
            }
            return super.visitMethod(access, name, descriptor, signature, exceptions)
        }
    }
    classReader.accept(classVisitor, ClassReader.SKIP_DEBUG)
}

class MyMethodAdapter(methodVisitor: MethodVisitor) : MethodVisitor(Opcodes.ASM7, methodVisitor) {
    override fun visitCode() {
        mv.visitCode()
    }

    override fun visitInsn(opcode: Int) {
        mv.visitInsn(opcode)
    }

    override fun visitLdcInsn(value: Any?) {
        if (value is String) {
            value.replace("事件", "时间")
        }
        mv.visitLdcInsn(value)
    }

    override fun visitEnd() {
        mv.visitEnd()
    }
}