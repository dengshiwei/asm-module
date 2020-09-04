package com.andoter.asm_example.part4

import com.andoter.asm_example.utils.ADLog
import org.objectweb.asm.*
@Deprecated(message = "deprecated")
class AnnotationPrinter {
}

fun main() {
    val classReader = ClassReader("com.andoter.asm_example.part4.AnnotationPrinter")
    val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
    classReader.accept(object : ClassVisitor(Opcodes.ASM7, classWriter) {
        override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor {
            return AnnotationPrinterVisitor(cv.visitAnnotation(descriptor, visible))
        }
    }, ClassReader.SKIP_DEBUG)
}


class AnnotationPrinterVisitor(annotationVisitor: AnnotationVisitor) : AnnotationVisitor(Opcodes.ASM7, annotationVisitor) {

    override fun visitEnd() {
        super.visitEnd()
        ADLog.info("visitEnd")
    }

    override fun visitAnnotation(name: String?, descriptor: String?): AnnotationVisitor {
        ADLog.info("visitAnnotation, name = $name, descriptor = $descriptor")
        return super.visitAnnotation(name, descriptor)
    }

    override fun visitEnum(name: String?, descriptor: String?, value: String?) {
        ADLog.info("visitEnum, name = $name, descriptor = $descriptor, value = $value")
        super.visitEnum(name, descriptor, value)
    }

    override fun visit(name: String?, value: Any?) {
        super.visit(name, value)
        ADLog.info("visit, name = $name, value = $value")
    }

    override fun visitArray(name: String?): AnnotationVisitor {
        ADLog.info("visitArray, name = $name")
        return super.visitArray(name)
    }
}