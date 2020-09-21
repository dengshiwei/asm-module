package com.andoter.asm_example.part4

import com.andoter.asm_example.part3.MyLoader
import com.andoter.asm_example.utils.ADLog
import com.andoter.asm_example.utils.ClassOutputUtil
import org.objectweb.asm.*

/**
 * 类的注解添加要复杂一些，因为存在一些限制条件：必须调用 ClassVisitor 类的方法
 */
class AddAnnotationAdapter {
}

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class NewAPI

fun main() {
    val classReader = ClassReader("com.andoter.asm_example.part4.AddAnnotationAdapter")
    val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
    val addAnnotationAdapter = AddAnnotationAdapterVisitor(classWriter, "Lcom/andoter/asm_example/part4/NewAPI;")
    classReader.accept(addAnnotationAdapter, ClassReader.SKIP_DEBUG)
    //输出文件查看
    ClassOutputUtil.byte2File("asm_example/files/AddAnnotation.class", classWriter.toByteArray())
    val loadClass = MyLoader()
    val classAdapter = loadClass.defineClass("com.andoter.asm_example.part4.AddAnnotationAdapter",classWriter.toByteArray())
    val method = classAdapter?.declaredMethods

    val annotations = classAdapter?.annotations

    val getAnnotation = classAdapter?.getDeclaredAnnotation(NewAPI::class.java)
    if (getAnnotation != null) {
        println("newApi")
    } else {
        println("NoAPI")
    }
}


class AddAnnotationAdapterVisitor(classVisitor: ClassVisitor, val annoDesc: String) : ClassVisitor(Opcodes.ASM7,classVisitor) {
    private var isAnnotationPresent = false
    override fun visitInnerClass(
        name: String?,
        outerName: String?,
        innerName: String?,
        access: Int
    ) {
        addAnnotation()
        cv.visitInnerClass(name, outerName, innerName, access)
    }

    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        cv.visit(version, access, name, signature, superName, interfaces)
    }

    override fun visitField(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        value: Any?
    ): FieldVisitor {
        addAnnotation()
        return cv.visitField(access, name, descriptor, signature, value)
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        addAnnotation()
        return cv.visitMethod(access, name, descriptor, signature, exceptions)
    }

    override fun visitEnd() {
        addAnnotation()
        cv.visitEnd()
    }

    override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor {
        ADLog.info("descriptor = $descriptor")
        if (visible and (annoDesc == descriptor)) {
            isAnnotationPresent = true
        }
        return super.visitAnnotation(descriptor, visible)
    }

    private fun addAnnotation() {
        if (!isAnnotationPresent) {
            ADLog.info("descriptor = $annoDesc")
            cv.visitAnnotation(annoDesc, true)?.visitEnd()
            isAnnotationPresent = true
        }
    }
}