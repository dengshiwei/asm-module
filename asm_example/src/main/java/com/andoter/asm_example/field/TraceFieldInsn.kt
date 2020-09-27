package com.andoter.asm_example.field

import com.andoter.asm_example.utils.ClassOutputUtil
import org.objectweb.asm.*
import org.objectweb.asm.util.CheckFieldAdapter
import org.objectweb.asm.util.Textifier
import org.objectweb.asm.util.TraceFieldVisitor

class TraceFieldInsn {
}

fun main() {
    val classReader = ClassReader("com.andoter.asm_example.field.TraceFieldInsn")
    val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
    classReader.accept(object: ClassVisitor(Opcodes.ASM7, classWriter){
        var isExist = false
        override fun visitField(
            access: Int,
            name: String?,
            descriptor: String?,
            signature: String?,
            value: Any?
        ): FieldVisitor? {
            if (name == "TAG") {
                isExist = true
            }

            return super.visitField(access, name, descriptor, signature, value)
        }

        override fun visitEnd() {
            super.visitEnd()
            if (!isExist) {
                val fieldVisitor = cv.visitField(Opcodes.ACC_PUBLIC, "TAG", "Ljava/lang/String;",null,"CheckField")
                val traceFieldVisitor = TraceFieldVisitor(fieldVisitor, Textifier())
                traceFieldVisitor.visitAnnotation("Lcom/andoter/Interface;", true)
                traceFieldVisitor.visitEnd()
            }
        }
    }, ClassReader.SKIP_DEBUG)

    ClassOutputUtil.byte2File("asm_example/files/TraceFieldInsn.class", classWriter.toByteArray())
}