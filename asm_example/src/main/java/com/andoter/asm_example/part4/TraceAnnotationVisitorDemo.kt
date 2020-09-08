package com.andoter.asm_example.part4

import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.util.Textifier
import org.objectweb.asm.util.TraceAnnotationVisitor

class TraceAnnotationVisitorDemo {
}

fun main() {
    TraceAnnotationAdapter.dump()
}

/**
 * 通过代码示例创建一个注解类，一个无值，一个具有枚举值。同样对于注解也有：TraceAnnotationVisitor 和 CheckAnnotationAdapter 用于
 * 输出和检测是否合法正确
 */
object TraceAnnotationAdapter : Opcodes {
    fun dump(): ByteArray {
        val classWriter = ClassWriter(0)
        val printer = object : Textifier(Opcodes.ASM7) {
            override fun visitAnnotationEnd() {
                super.visitAnnotationEnd()
                println(this.stringBuilder.toString())
            }
        }
        var traceAnnotationVisitor : TraceAnnotationVisitor
        classWriter.visit(Opcodes.ASM7, Opcodes.ACC_PUBLIC + Opcodes.ACC_ANNOTATION + Opcodes.ACC_ABSTRACT + Opcodes.ACC_INTERFACE,
            "java/lang/Deprecated", null, "java/lang/Object", arrayOf( "java/lang/annotation/Annotation" )
            )
        var av = classWriter.visitAnnotation("Ljava/lang/annotation/Documented;", true)
        traceAnnotationVisitor = TraceAnnotationVisitor(av, printer)
        traceAnnotationVisitor.visitEnd()

        av = classWriter.visitAnnotation("Ljava/lang/annotation/Retention;", true)
        traceAnnotationVisitor = TraceAnnotationVisitor(av, printer)
        traceAnnotationVisitor.visitEnum("value", "Ljava/lang/annotation/RetentionPolicy;",
            "RUNTIME")
        traceAnnotationVisitor.visitEnd()

        classWriter.visitEnd()
        return classWriter.toByteArray()
    }
}