package com.andoter.asm_example.part9

import com.andoter.asm_example.utils.ADLog
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.AnnotationNode

/**
 * 在树 API 中提供了对应的 AnnotationNode 类用于处理注解，该类继承 AnnotationVisitor
 */

@Test("Adapter")
class AnnotationNodeAdapter {

}

annotation class Test(val value: String)

fun main() {
    val classReader = ClassReader("com.andoter.asm_example.part9.AnnotationNodeAdapter")
    classReader.accept(object :ClassVisitor(Opcodes.ASM7){
        override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor {
            return object :AnnotationNode(Opcodes.ASM6, descriptor) {
                override fun visit(name: String?, value: Any?) {
                    super.visit(name, value)
                    ADLog.info("visit, name = $name, value = $value")
                }

                override fun visitEnd() {
                    ADLog.info("visitEnd")
                    super.visitEnd()
                }
            }
        }
    }, ClassReader.SKIP_DEBUG)

}