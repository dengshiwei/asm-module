package com.andoter.asm_example.part4

import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.util.CheckAnnotationAdapter

class CheckAnnotationAdapterDemo {
}

fun main() {
    CheckAnnotation.dump()
}

/**
 * 通过代码示例创建一个注解类，一个无值，一个具有枚举值。同样对于注解也有：TraceAnnotationVisitor 和 CheckAnnotationAdapter 用于
 * 输出和检测是否合法正确
 */
object CheckAnnotation : Opcodes {
    fun dump(): ByteArray {
        val classWriter = ClassWriter(0)
        classWriter.visit(Opcodes.ASM7, Opcodes.ACC_PUBLIC + Opcodes.ACC_ANNOTATION + Opcodes.ACC_ABSTRACT + Opcodes.ACC_INTERFACE,
            "java/lang/Deprecated", null, "java/lang/Object", arrayOf( "java/lang/annotation/Annotation" )
            )
        var av = classWriter.visitAnnotation("Ljava/lang/annotation/Documented;", true)
        var checkAnnotationVisitor = CheckAnnotationAdapter(av)
        checkAnnotationVisitor.visitEnd()

        av = classWriter.visitAnnotation("Ljava/lang/annotation/Retention;", true)
        checkAnnotationVisitor = CheckAnnotationAdapter(av)
        checkAnnotationVisitor.visitEnum("value", "Ljava/lang/annotation/RetentionPolicy;",
            "RUNTIME")
        checkAnnotationVisitor.visitEnd()

        classWriter.visitEnd()
        return classWriter.toByteArray()
    }
}