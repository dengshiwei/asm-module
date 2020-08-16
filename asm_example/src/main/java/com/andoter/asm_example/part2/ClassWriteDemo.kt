package com.andoter.asm_example.part2

import com.andoter.asm_example.ClassOutputUtil
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes

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