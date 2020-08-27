package com.andoter.asm_example.part3

import com.andoter.asm_example.utils.ADLog
import com.andoter.asm_example.utils.AccessCodeUtils
import org.objectweb.asm.*
import java.lang.reflect.Field

class AddTimerAdapter {
    fun addTimer() {
        Thread.sleep(100)
    }
}

/*
// 目标结构
class AddTimerAdapter {
    var time:Long = 0
    fun addTimer() {
        time -= System.currentTimeMillis()
        Thread.sleep(200)
        time += System.currentTimeMillis()
    }
}
 */

fun main() {
    val classReader = ClassReader("com.andoter.asm_example.part3.AddTimerAdapter")
    val classWriter = ClassWriter(classReader, 0)
    val classVisitor = AddTimerAdapterVisitor(Opcodes.ASM7, classWriter)

    classReader.accept(classVisitor, ClassReader.SKIP_DEBUG)
    // 通过自定义 Loader 加载修改后的 .class 文件
    val loader = MyLoader()
    val addTimerAdapterClazz = loader.defineClass("com.andoter.asm_example.part3.AddTimerAdapter", classWriter.toByteArray())
    // 获取 addTimer 方法
    val method = addTimerAdapterClazz?.getDeclaredMethod("addTimer")
    // 获取 time 字段
    val field : Field? = addTimerAdapterClazz?.getDeclaredField("time")
    // 触发方法
    method?.invoke(addTimerAdapterClazz?.newInstance())
    // 打印 time 耗时
    ADLog.error("time = ${field?.getLong(addTimerAdapterClazz?.newInstance())}")
}

class AddTimerAdapterVisitor(api: Int, classVisitor: ClassVisitor) :
    ClassVisitor(api, classVisitor) {
    private var owner: String? = ""
    private var isInterface: Boolean = false

    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        owner = name
        isInterface = access and Opcodes.ACC_INTERFACE != 0
        ADLog.info("visit, name = $name, isInterface = $isInterface")
        cv.visit(version, access, name, signature, superName, interfaces)
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        ADLog.info("visitMethod, name = $name, descriptor = $descriptor")
        var methodVisitor = cv.visitMethod(access, name, descriptor, signature, exceptions)
        if (!isInterface && methodVisitor != null && name != "<init>") {
            methodVisitor = AddTimerMethod(Opcodes.ASM7, methodVisitor, owner)
        }
        return methodVisitor
    }

    override fun visitEnd() {
        ADLog.info("visitEnd")
        if (!isInterface) {
            val fieldVisitor =
                cv.visitField(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, "time", "J", null, null)
            fieldVisitor?.visitEnd()
        }
        super.visitEnd()
    }
}

class AddTimerMethod(api: Int, methodVisitor: MethodVisitor) : MethodVisitor(api, methodVisitor) {
    private var owner: String? = ""

    constructor(api: Int, methodVisitor: MethodVisitor, owner: String?) : this(api, methodVisitor) {
        this.owner = owner
    }


    override fun visitCode() {
        ADLog.info("visitCode")
        mv.visitCode()
        mv.visitFieldInsn(Opcodes.GETSTATIC, owner, "time", "J")
        mv.visitMethodInsn(
            Opcodes.INVOKESTATIC,
            "java/lang/System",
            "currentTimeMillis",
            "()J"
        )
        mv.visitInsn(Opcodes.LSUB)
        mv.visitFieldInsn(Opcodes.PUTSTATIC, owner, "time", "J")
    }

    override fun visitInsn(opcode: Int) {
        ADLog.info("visitInsn, opcode = ${AccessCodeUtils.opcode2String(opcode)}")
        if (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN || opcode == Opcodes.ATHROW) {
            mv.visitFieldInsn(Opcodes.GETSTATIC, owner, "time", "J")
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J")
            mv.visitInsn(Opcodes.LADD)
            mv.visitFieldInsn(Opcodes.PUTSTATIC, owner, "time", "J")
        }
        mv.visitInsn(opcode)
    }

    override fun visitMaxs(maxStack: Int, maxLocals: Int) {
        ADLog.info("visitMaxs")
        mv.visitMaxs(maxStack + 4, maxLocals)
    }
}

class MyLoader : ClassLoader() {
    fun defineClass(name: String?, b: ByteArray): Class<*>? {
        return defineClass(name, b, 0, b.size)
    }
}