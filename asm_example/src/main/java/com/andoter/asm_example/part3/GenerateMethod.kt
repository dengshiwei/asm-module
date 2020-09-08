package com.andoter.asm_example.part3

import com.andoter.asm_example.utils.ClassOutputUtil
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.util.TraceClassVisitor
import java.io.PrintWriter

class GenerateMethod {
}

class Bean{
    private var f:Int =1
    fun getF(): Int {
        return this.f
    }

    fun setF(value: Int) {
        this.f = value
    }
}

/*
   // access flags 0x11
    public final getF()I
    L0
    LINENUMBER 9 L0
    ALOAD 0
    GETFIELD com/andoter/asm_example/part3/Bean.f : I
    IRETURN
    L1
    LOCALVARIABLE this Lcom/andoter/asm_example/part3/Bean; L0 L1 0
    MAXSTACK = 1
    MAXLOCALS = 1
 */
fun main() {
    val classWriter = ClassWriter(0)
    val traceClassWriter =
        TraceClassVisitor(classWriter, PrintWriter(System.out))
    traceClassWriter.visit(
        Opcodes.V1_7,
        Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL,
        "pkg/Bean",
        null,
        "java/lang/Object",
        null
    )
    val methodVisitor = traceClassWriter.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL, "getF",
        "()I", null, null)
    methodVisitor.visitCode()
    methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
    methodVisitor.visitFieldInsn(Opcodes.GETFIELD, "pkg/Bean", "f", "I")
    methodVisitor.visitInsn(Opcodes.IRETURN)
    methodVisitor.visitMaxs(1, 1)//定义执行栈帧的局部变量表和操作数栈的大小
    methodVisitor.visitEnd()
    traceClassWriter.visitEnd()
    /*
     * 输出的字节码文件：
     *  package pkg;
        public final class Bean {
            public final int getF() {
                return this.f;
            }
        }
     */
    ClassOutputUtil.byte2File("asm_example/files/Bean.class", classWriter.toByteArray())
}