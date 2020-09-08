package com.andoter.asm_example.part2

import com.andoter.asm_example.utils.ClassOutputUtil
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes


/**
 * 在后面的第三章中，我们会了解到帧栈的概念，为一个方法计算栈映射帧并不是非常容易：
 * 必须计算所有帧，找出与跳转目标相对应的帧，或者跳在无条件跳转之后的帧，最后压缩剩余帧。
 * 还好，在 ASM 中帮我们完成了这些工作，提供了几个选项：
 *  - new ClassWriter(0)：不会计算任何东西，必须由开发者手动进行计算帧、局部变量、操作数栈的大小
 *  - new ClassWriter(ClassWriter.COMPUTE_MAXS)：自动计算操作数栈和局部变量表大小，但是必须调用 visitMaxs(int) 方法，可以使用任何参数，它会被重新计算覆盖。
 *          但是该方式需要自行计算帧。
 *  - new ClassWriter(ClassWriter.COMPUTE_FRAMES)：计算全部，必须调用 visitMaxs(int)，但是不必调用 visitFrame()
 *
 *  COMPUTE_MAXS 选项使 ClassWriter 的速度降低 10%，而使用 COMPUTE_FRAMES 选项则使其降低一半。
 *  这必须与我们自行计算时所耗费的时间进行比较：在特定情况下，经常会存在一些比 ASM 所用算法更容易、更快速的计算方法，但ASM 使用的算法必须能够处理所有情况。
 */
class ClassWriteDemo {
}

/*
package pkg;
public interface Comparable extends Mesurable {
 int LESS = -1;
 int EQUAL = 0;
 int GREATER = 1;
 int compareTo(Object o);
}
 */

fun main() {
    val classWriter = ClassWriter(0)
    classWriter.visit(Opcodes.V1_7, Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT + Opcodes.ACC_INTERFACE,
        "pkg/Comparable",null, "java/lang/Object", arrayOf("pkg/Mesureable"))
    classWriter.visitField(Opcodes.ACC_PUBLIC+ Opcodes.ACC_FINAL + Opcodes.ACC_STATIC, "LESS", "I", null,  -1).visitEnd()
    classWriter.visitField(Opcodes.ACC_PUBLIC+ Opcodes.ACC_FINAL + Opcodes.ACC_STATIC, "EQUAL", "I", null,  0).visitEnd()
    classWriter.visitField(Opcodes.ACC_PUBLIC+ Opcodes.ACC_FINAL + Opcodes.ACC_STATIC, "GREATER", "I", null,  1).visitEnd()
    classWriter.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT, "compareTo", "(Ljava/lang/Object;)I" ,null, null).visitEnd()
    classWriter.visitEnd()
    val writeByte = classWriter.toByteArray()

    // 输出字节码，然后通过 javap 指令查看输出
    ClassOutputUtil.byte2File("asm_example/files/Comparable.class", writeByte)
}