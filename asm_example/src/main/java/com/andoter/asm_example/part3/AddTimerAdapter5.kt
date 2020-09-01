package com.andoter.asm_example.part3

import com.andoter.asm_example.utils.ADLog
import org.objectweb.asm.*
import org.objectweb.asm.commons.AnalyzerAdapter
import org.objectweb.asm.commons.LocalVariablesSorter
import java.lang.reflect.Field
import kotlin.math.max

class AddTimerAdapter5 {
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
    val classReader = ClassReader("com.andoter.asm_example.part3.AddTimerAdapter5")
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
                val methodAdapter =  AddTimerMethodAdapter5(
                    owner,
                    cv.visitMethod(access, name, descriptor, signature, exceptions)
                )
                methodAdapter.analyzerAdapter = AnalyzerAdapter(owner, access, name, descriptor, methodAdapter)
                methodAdapter.localVariablesSorter = LocalVariablesSorter(access, descriptor, methodAdapter)
                return methodAdapter.localVariablesSorter!!
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
        "com.andoter.asm_example.part3.AddTimerAdapter5",
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

class AddTimerMethodAdapter5(
    private var owner: String,
    methodVisitor: MethodVisitor?
) : MethodVisitor(Opcodes.ASM7,  methodVisitor ){
    private var time:Int = -1
    var localVariablesSorter: LocalVariablesSorter? = null
    var analyzerAdapter: AnalyzerAdapter? = null
    private var maxStack: Int = 0

    override fun visitCode() {
        super.visitCode()
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false)
        time = localVariablesSorter?.newLocal(Type.LONG_TYPE)!!
        mv.visitVarInsn(Opcodes.LSTORE, time)
        maxStack = 4
    }

    override fun visitInsn(opcode: Int) {
        if (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN || Opcodes.ATHROW == opcode) {
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false)
            mv.visitVarInsn(Opcodes.LLOAD, time)
            mv.visitInsn(Opcodes.LSUB)
            mv.visitFieldInsn(Opcodes.GETSTATIC, owner, "timer", "J")
            mv.visitInsn(Opcodes.LADD)
            mv.visitFieldInsn(Opcodes.PUTSTATIC, owner, "timer", "J")
            maxStack = max(analyzerAdapter!!.stack.size + 4, maxStack)
        }
        super.visitInsn(opcode)
    }

    override fun visitMaxs(maxStack: Int, maxLocals: Int) {
        mv.visitMaxs(max(this.maxStack, maxStack), maxLocals)
    }
}