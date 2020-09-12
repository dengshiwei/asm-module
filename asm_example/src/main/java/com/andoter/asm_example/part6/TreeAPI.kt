package com.andoter.asm_example.part6

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldNode
import org.objectweb.asm.tree.MethodNode

/*
ClassNode 代表着一个类文件，是 ClassVisitor 的一个子类。内部包含了 name、access 等 class 文件的信息

FiledNode 代表着一个字段 Field 信息，是 FieldVisitor 的子类
    public class FieldNode extends FieldVisitor {

        public int access;

        public String name;

        public String desc;

        public String signature;
    }

 MethodNode 代表着一个方法信息，是 MethodVisitor 的子类。
 */
class TreeAPI {
}

fun main() {
    // ClassNode 与 ClassReader 的结合转换
    val classNode = ClassNode()
    val classReader = ClassReader("")
    classReader.accept(classNode, ClassReader.SKIP_DEBUG)

    // ClassNode 与 ClassWriter 的结合
    val classNode1 = ClassNode()
    val classWriter = ClassWriter(ClassWriter.COMPUTE_MAXS)
    classNode1.accept(classWriter)

    val fieldNode = FieldNode(Opcodes.ACC_PUBLIC, "field", "Ljava/lang/String", null, "field")
    val methodNode = MethodNode()
}