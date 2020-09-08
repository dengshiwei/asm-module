package com.andoter.asm_example.part2

import com.andoter.asm_example.utils.ClassOutputUtil
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.util.CheckClassAdapter

/*
ClassWriter 类并不会核实对其方法的调用顺序是否恰当，以及参数是否有效。因此，有
可能会生成一些被 Java 虚拟机验证器拒绝的无效类。为了尽可能提前检测出部分此类错误，可
以使用 CheckClassAdapter 类。和 TraceClassVisitor 类似，这个类也扩展了
ClassVisitor 类，并将对其方法的所有调用都委托到另一个 ClassVisitor，比如一个
TraceClassVisitor 或一个 ClassWriter。但是，这个类并不会打印所访问类的文本表示，
而是验证其对方法的调用顺序是否适当，参数是否有效，然后才会委托给下一个访问器。当发生
错误时，会抛出 IllegalStateException 或 IllegalArgumentException。
 */
class CheckClassDemo {

}

fun main() {
    val classWriter = ClassWriter(0)
    val checkClassAdapter = CheckClassAdapter(classWriter)
    checkClassAdapter.visit(
        Opcodes.V1_7, Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT + Opcodes.ACC_INTERFACE,
        "pkg/Comparable",null, "java/lang/Object1", arrayOf("pkg/Mesureable"))
    checkClassAdapter.visitField(Opcodes.ACC_PUBLIC+ Opcodes.ACC_FINAL + Opcodes.ACC_STATIC, "LESS", "I", null,  -1).visitEnd()
    checkClassAdapter.visitField(Opcodes.ACC_PUBLIC+ Opcodes.ACC_FINAL + Opcodes.ACC_STATIC, "EQUAL", "I", null,  0).visitEnd()
    checkClassAdapter.visitField(Opcodes.ACC_PUBLIC+ Opcodes.ACC_FINAL + Opcodes.ACC_STATIC, "GREATER", "I", null,  1).visitEnd()
    // 比如这里，我们将 Ljava/lang/Object 修改为 Ljava/lang/Object1，此时编译就会报错
    checkClassAdapter.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT, "compareTo", "(Ljava/lang/Object;)I" ,null, null).visitEnd()
    checkClassAdapter.visitEnd()
    val writeByte = classWriter.toByteArray()

    // 输出字节码，然后通过 javap 指令查看输出
    ClassOutputUtil.byte2File("asm_example/files/Comparable.class", writeByte)
}