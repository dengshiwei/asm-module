package com.andoter.asm_example.part3

import com.andoter.asm_example.utils.ADLog
import org.objectweb.asm.*
import org.objectweb.asm.commons.AdviceAdapter
import java.lang.reflect.Field

/*
AdviceAdapter 是继承自 MethodVisitor 的一个抽象类，可用于在一个方法的开头以及任意 RETURN 或 ATHROW 指令之前插入代码。
它的优点是可以对构造器也是有效的，在构造器中插入到构造器调用之后。事实上，这个适配器的大多数代码都是用于检测对这个构造器的调用。
注意，AdviceAdapter 继承自 LocalVariablesSorter，所以也可以轻松完成对一个局部变量的操作。

 */
class AddTimerAdapter6 {
    fun addTimer() {
        Thread.sleep(200)
    }
}

/*
实现类似如下效果：
public class AddTimerAdapter5 {
 public static long timer;
 public void addTimer() throws Exception {
     long t = System.currentTimeMillis();
     Thread.sleep(200);
     timer += System.currentTimeMillis() - t;
 }
}
 */
fun main() {
    val classReader = ClassReader("com.andoter.asm_example.part3.AddTimerAdapter6")
    val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
    classReader.accept(object : ClassVisitor(Opcodes.ASM7, classWriter) {
        private var isInterface: Boolean = false
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
            isInterface = access and Opcodes.ACC_INTERFACE != 0
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
            if (!isInterface && cv != null && name != "<init>") {
                return AddTimerMethodAdapter6(
                    owner,
                    cv.visitMethod(access, name, descriptor, signature, exceptions),
                    access, name, descriptor
                )
            }
            return super.visitMethod(access, name, descriptor, signature, exceptions)
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
        "com.andoter.asm_example.part3.AddTimerAdapter6",
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

class AddTimerMethodAdapter6(
    private var owner: String,
    methodVisitor: MethodVisitor?,
    access: Int,
    name: String?,
    descriptor: String?
) : AdviceAdapter(Opcodes.ASM7, methodVisitor, access, name, descriptor) {

    override fun onMethodEnter() {
        mv.visitFieldInsn(GETSTATIC, owner, "timer", "J")
        mv.visitMethodInsn(
            INVOKESTATIC, "java/lang/System",
            "currentTimeMillis", "()J", false
        )
        mv.visitInsn(LSUB)
        mv.visitFieldInsn(PUTSTATIC, owner, "timer", "J")
    }

    override fun onMethodExit(opcode: Int) {
        mv.visitFieldInsn(GETSTATIC, owner, "timer", "J")
        mv.visitMethodInsn(
            INVOKESTATIC, "java/lang/System",
            "currentTimeMillis", "()J", false
        )
        mv.visitInsn(LADD)
        mv.visitFieldInsn(PUTSTATIC, owner, "timer", "J")
    }

    override fun visitMaxs(maxStack: Int, maxLocals: Int) {
        mv.visitMaxs(maxStack + 4, maxLocals)
    }
}