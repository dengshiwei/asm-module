package com.andoter.asm_example.part3

import com.andoter.asm_example.utils.ClassOutputUtil
import org.objectweb.asm.*

/*
 在上一节中，我们对 AddTimer 的操作称之为无状态转换。
 无状态转换即在局部的转换中，不会依赖当前指令访问之前的指令的转换。比如我们在方法的开头添加代码，每个方法都会添加。

 比如我们要删除所有出现 ICONST_0 IADD 的序列，因为这个操作就是加入 0，没什么意义。所以当访问到 IADD 时，上一条指令
 是 ICONST_0，则进行指令删除。这样就需要存储上个指令的，像这种转换称之为有状态转换。
 */
class RemoveAddZeroAdapter {
    fun addNumber(number:Int) {
        var result = number + 0
        println("result = $result")
    }
}

/*
针对 RemoteAddZeroAdapter 类的 addNumber 方法作为例子，转换后的字节码片段如下：
    ILOAD 1
    ICONST_0
    IADD
    ISTORE 2
 */
fun main() {
    val classReader = ClassReader("com.andoter.asm_example.part3.RemoveAddZeroAdapter")
    val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
    val classVisitor = object : ClassVisitor(Opcodes.ASM7, classWriter) {
        override fun visitMethod(
            access: Int,
            name: String?,
            descriptor: String?,
            signature: String?,
            exceptions: Array<out String>?
        ): MethodVisitor {
            val methodVisitor =  cv.visitMethod(access, name, descriptor, signature, exceptions)
            return RemoteZeroAdapter(methodVisitor)
        }
    }
    classReader.accept(classVisitor, ClassReader.SKIP_DEBUG)
    /*
    输出 .class 文件，然后通过 javap -v 查看
    stack=2, locals=5, args_size=2
         0: iload_1
         1: istore_2
         2: new           #8                  // class java/lang/StringBuilder
         5: dup
         6: invokespecial #12                 // Method java/lang/StringBuilder."<init>":()V
         9: ldc           #14                 // String result =
        11: invokevirtual #18
     */
    ClassOutputUtil.byte2File("asm_example/files/RemoteZeroAdapter.class", classWriter.toByteArray())
}

abstract class PatternMethodVisitor(api: Int, methodVisitor: MethodVisitor?) :
    MethodVisitor(api, methodVisitor) {
    protected val SEEN_NOTHING= 0
    protected var state:Int = 0
    override fun visitInsn(opcode: Int) {
        visitInsn()
        super.visitInsn(opcode)
    }

    override fun visitIntInsn(opcode: Int, operand: Int) {
        visitInsn()
        super.visitIntInsn(opcode, operand)
    }

    abstract fun visitInsn()
}

class RemoteZeroAdapter(methodVisitor: MethodVisitor?) : PatternMethodVisitor(Opcodes.ASM7, methodVisitor = methodVisitor) {
    private val SEEN_ICONST_0 = 1

    override fun visitInsn(opcode: Int) {
        if (state == SEEN_ICONST_0) {
            if (opcode == Opcodes.IADD) {
                state = SEEN_NOTHING
                return
            }
        }

        visitInsn()

        if (opcode == Opcodes.ICONST_0) {
            state = SEEN_ICONST_0
            return
        }
        mv.visitInsn(opcode)
    }

    override fun visitInsn() {
        if (state == SEEN_ICONST_0) {
            mv.visitInsn(Opcodes.ICONST_0)
        }
        state = SEEN_NOTHING
    }
}
