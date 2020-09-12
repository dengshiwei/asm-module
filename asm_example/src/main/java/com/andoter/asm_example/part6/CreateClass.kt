package com.andoter.asm_example.part6

import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldNode
import org.objectweb.asm.tree.MethodNode

class CreateClass {
}

/*
使用树 API 生成类的过程，就是创建一个 ClassNode 对象，然后初始化它的字段。还是以 2.2.3 中的例子说明：
package pkg;
public interface Comparable extends Measurable {
 int LESS = -1;
 int EQUAL = 0;
 int GREATER = 1;
 int compareTo(Object o);
}

使用树 API 生成类时，需要大约多花费 30% 的时间，占用的内存也比较多。但是可以按照任意顺序生成元素，一些情况下可能比较方便。
 */
fun main() {
    val classNode = ClassNode()
    classNode.version = Opcodes.V1_5
    classNode.access = Opcodes.ACC_PUBLIC + Opcodes.ACC_INTERFACE + Opcodes.ACC_ABSTRACT
    classNode.name = "pkg/Comparable"
    classNode.superName = "java/lang/Object"
    classNode.interfaces.add("pkg/Measurable")
    classNode.fields.add(
        FieldNode(
            Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC,
            "LESS",
            "I",
            null,
            -1
        )
    )
    classNode.fields.add(
        FieldNode(
            Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC,
            "EQUAL",
            "I",
            null,
            0
        )
    )
    classNode.fields.add(
        FieldNode(
            Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC,
            "GREATER",
            "I",
            null,
            1
        )
    )
    classNode.methods.add(
        MethodNode(
            Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT,
            "compareTo",
            "(Ljava/lang/Object;)I",
            null,
            null
        )
    )
}