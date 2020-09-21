package com.andoter.asm_example.part3

import com.andoter.asm_example.utils.ADLog
import org.objectweb.asm.*
import org.objectweb.asm.commons.LocalVariablesSorter
import java.lang.reflect.Field

class AddTimerAdapter4 {
    fun addTimer() {
        Thread.sleep(200)
    }
}

/*
实现类似如下效果：
public class AddTimerAdapter4 {
 public static long timer;
 public void addTimer() throws Exception {
     long t = System.currentTimeMillis();
     Thread.sleep(200);
     timer += System.currentTimeMillis() - t;
 }
}
 */
fun main() {
    val classReader = ClassReader("com.andoter.asm_example.part3.AddTimerAdapter4")
    val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
    classReader.accept(object : ClassVisitor(Opcodes.ASM7, classWriter) {
        var owner: String = ""
        override fun visit(
            version: Int,
            access: Int,
            name: String?,
            signature: String?,
            superName: String?,
            interfaces: Array<out String>?
        ) {
            owner = name!!
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
            return AddTimerMethodAdapter4(
                owner,
                access,
                descriptor,
                cv.visitMethod(access, name, descriptor, signature, exceptions)
            )
        }

        override fun visitEnd() {
            ADLog.info("visitEnd")
            val fieldVisitor =
                cv.visitField(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, "timer", "J", null, null)
            fieldVisitor?.visitEnd()
            super.visitEnd()
        }
    }, ClassReader.SKIP_DEBUG)

    // 通过自定义 Loader 加载修改后的 .class 文件
    val loader = MyLoader()
    val addTimerAdapterClazz = loader.defineClass(
        "com.andoter.asm_example.part3.AddTimerAdapter4",
        classWriter.toByteArray()
    )
    // 获取 addTimer 方法
    val method = addTimerAdapterClazz?.getDeclaredMethod("addTimer")
    // 获取 time 字段
    val field: Field? = addTimerAdapterClazz?.getDeclaredField("timer")
    // 触发方法
    method?.invoke(addTimerAdapterClazz?.newInstance())
    // 打印 time 耗时
    ADLog.error("time = ${field?.getLong(addTimerAdapterClazz?.newInstance())}")
}

class AddTimerMethodAdapter4(
    private var owner: String,
    access: Int,
    descriptor: String?,
    methodVisitor: MethodVisitor?
) : LocalVariablesSorter(Opcodes.ASM7, access, descriptor, methodVisitor ){
    private var time:Int = -1

    override fun visitCode() {
        super.visitCode()
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false)
        time = newLocal(Type.LONG_TYPE)
        mv.visitVarInsn(Opcodes.LSTORE, time)
    }

    override fun visitInsn(opcode: Int) {
        if (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN || Opcodes.ATHROW == opcode) {
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false)
            mv.visitVarInsn(Opcodes.LLOAD, time)
            mv.visitInsn(Opcodes.LSUB)
            mv.visitFieldInsn(Opcodes.GETSTATIC, owner, "timer", "J")
            mv.visitInsn(Opcodes.LADD)
            mv.visitFieldInsn(Opcodes.PUTSTATIC, owner, "timer", "J")
        }
        super.visitInsn(opcode)
    }

    override fun visitMaxs(maxStack: Int, maxLocals: Int) {
        super.visitMaxs(maxStack + 4, maxLocals)
    }
}