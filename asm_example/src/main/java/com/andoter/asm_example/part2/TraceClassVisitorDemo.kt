package com.andoter.asm_example.part2

import com.andoter.asm_example.utils.ClassOutputUtil
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.util.TraceClassVisitor
import java.io.PrintWriter

/**
 * ClassWrite 输出的是字节数组，对于我们确认修改后的字节码是否符合预期用处并不大，
 * 在前面，我们是通过 File 文件流操作，将字节码输出到文件，然后利用 javap 指令来查看是否符合预期。
 * 在 ASM 中提供了 TraceClassVisitor 用来对我们的输出做检查，不必这么麻烦
 */
interface TraceClassVisitorDemo {
    var className: String
    var classVersion: Int

    fun getTraceInfo(): String
}

/**
 * 依照上面的 TraceClassVisitorDemo 为例
 */
fun main() {
    val classWriter = ClassWriter(0)
    /*
     使用 TraceClassVisitor，同时使用 System.out 流将结果输出。
     另外测试时还有一种写法，输出到文件。PrintWriter("asm_example/files/TraceClassVisitorDemo.class") 但是输出的文件通过 javap -v 指令查看会报错。
     详情可参照：https://stackoverflow.com/questions/63443099/asm-traceclassvisitor-output-file-is-error
     */
    val traceClassWriter =
        TraceClassVisitor(classWriter, PrintWriter(System.out))
    traceClassWriter.visit(
        Opcodes.V1_7,
        Opcodes.ACC_PUBLIC + Opcodes.ACC_INTERFACE + Opcodes.ACC_ABSTRACT,
        "com.andoter.asm_example.part2/TraceClassVisitorDemo",
        null,
        "java/lang/Object",
        null
    )
    traceClassWriter.visitSource("TraceClassVisitorDemo.class", null)
    traceClassWriter.visitField(Opcodes.ACC_PUBLIC+ Opcodes.ACC_FINAL + Opcodes.ACC_STATIC, "className", "Ljava/lang/String;", null, "").visitEnd()
    traceClassWriter.visitField(Opcodes.ACC_PUBLIC+ Opcodes.ACC_FINAL + Opcodes.ACC_STATIC, "classVersion", "I", null, 50).visitEnd()
    traceClassWriter.visitMethod(
        Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT,
        "getTraceInfo",
        "()Ljava/lang/String;",
        null,
        null
    ).visitEnd()
    traceClassWriter.visitEnd()

    ClassOutputUtil.byte2File("asm_example/files/TraceClassVisitorDemo1.class", classWriter.toByteArray())
}