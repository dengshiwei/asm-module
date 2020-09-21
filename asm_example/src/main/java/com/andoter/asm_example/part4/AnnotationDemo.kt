package com.andoter.asm_example.part4

import com.andoter.asm_example.utils.ADLog
import com.andoter.asm_example.utils.ClassOutputUtil
import org.objectweb.asm.*

/**
 * 在 Java 中有很多注解供开发者使用，比如 @exception、@author 等。
 * 同时也可以使用元注解进行自定义注解的声明定义。
 * 在  ASM 中提供了 AnnotationVisitor 类用于注解的访问，可以访问注解的键值对。
 */
@Andoter(authorities = "andoter")
class AnnotationDemo {

    override fun toString(): String {
        return super.toString()
    }

    @Andoter(authorities = "andoter")
    fun annotationTest() {

    }
}

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Andoter (val authorities: String)

fun main() {
    val classReader = ClassReader("com.andoter.asm_example.part4.AnnotationDemo")
    val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
    val removeAnnotationAdapter = RemoveAnnotationAdapter(classWriter, "Lcom/andoter/asm_example/part4/Andoter")
    classReader.accept(removeAnnotationAdapter, ClassReader.SKIP_DEBUG)
    //输出文件查看
    ClassOutputUtil.byte2File("asm_example/files/RemoteAnnotation.class", classWriter.toByteArray())
}

class RemoveAnnotationAdapter(classVisitor : ClassVisitor, var annotation: String) : ClassVisitor(Opcodes.ASM7, classVisitor) {

    override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor? {
        ADLog.info("descriptor = $descriptor")
        if (descriptor == annotation) {
            return null
        }
        return cv.visitAnnotation(descriptor, visible)
    }
}