package com.andoter.asm_example.part3

import com.andoter.asm_example.utils.ADLog
import org.objectweb.asm.*
import org.objectweb.asm.commons.AnalyzerAdapter
import java.lang.reflect.Field
import kotlin.math.max

class AddTimerAdapter2 {
    fun addTimer() {
        Thread.sleep(200)
    }
}

fun main() {
    val classReader = ClassReader("com.andoter.asm_example.part3.AddTimerAdapter2")
    val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
    classReader.accept(object : ClassVisitor(Opcodes.ASM7, classWriter) {
        var owner: String? = ""
        override fun visit(
            version: Int,
            access: Int,
            name: String?,
            signature: String?,
            superName: String?,
            interfaces: Array<out String>?
        ) {
            owner = name
            super.visit(version, access, name, signature, superName, interfaces)
            ADLog.info("owner = $owner")
        }

        override fun visitMethod(
            access: Int,
            name: String?,
            descriptor: String?,
            signature: String?,
            exceptions: Array<out String>?
        ): MethodVisitor {
            return AddTimerMethodAdapter2(
                Opcodes.ASM7,
                owner,
                access,
                name,
                descriptor,
                cv.visitMethod(access, name, descriptor, signature, exceptions)
            )
        }

        override fun visitEnd() {
            ADLog.info("visitEnd")
            val fieldVisitor =
                cv.visitField(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, "time", "J", null, null)
            fieldVisitor?.visitEnd()
            super.visitEnd()
        }
    }, ClassReader.SKIP_DEBUG)

    // 通过自定义 Loader 加载修改后的 .class 文件
    val loader = MyLoader()
    val addTimerAdapterClazz = loader.defineClass(
        "com.andoter.asm_example.part3.AddTimerAdapter2",
        classWriter.toByteArray()
    )
    // 获取 addTimer 方法
    val method = addTimerAdapterClazz?.getDeclaredMethod("addTimer")
    // 获取 time 字段
    val field: Field? = addTimerAdapterClazz?.getDeclaredField("time")
    // 触发方法
    method?.invoke(addTimerAdapterClazz?.newInstance())
    // 打印 time 耗时
    ADLog.error("time = ${field?.getLong(addTimerAdapterClazz?.newInstance())}")
}

class AddTimerMethodAdapter2(
    api: Int,
    var owner: String?,
    access: Int,
    name: String?,
    descriptor: String?,
    methodVisitor: MethodVisitor?
) : AnalyzerAdapter(api, owner, access, name, descriptor, methodVisitor) {
    private var maxStack: Int = 0

    override fun visitCode() {
        super.visitCode()
        mv.visitFieldInsn(Opcodes.GETSTATIC, owner, "time", "J")
        mv.visitMethodInsn(
            Opcodes.INVOKESTATIC,
            "java/lang/System",
            "currentTimeMillis",
            "()J",
            false
        )
        mv.visitInsn(Opcodes.LSUB)
        mv.visitFieldInsn(Opcodes.PUTSTATIC, owner, "time", "J")
        maxStack = 4
    }

    override fun visitInsn(opcode: Int) {
        if (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN || opcode == Opcodes.ATHROW) {
            mv.visitFieldInsn(Opcodes.GETSTATIC, owner, "time", "J")
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J")
            mv.visitInsn(Opcodes.LADD)
            mv.visitFieldInsn(Opcodes.PUTSTATIC, owner, "time", "J")
            maxStack = max(maxStack, stack.size + 4)
        }
        super.visitInsn(opcode)
    }

    override fun visitMaxs(maxStack: Int, maxLocals: Int) {
        super.visitMaxs(this.maxStack.coerceAtLeast(maxStack), maxLocals)
    }
}