package com.andoter.asm_example.part7

import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.*

class MakeMethod {
    private var f : Int = 0
    fun checkAndSetF(f: Int) {
        if (f >= 0) {
            this.f = f
        } else {
            throw IllegalArgumentException()
        }
    }
}
/*
参照 3.1.5 小节中的代码：
public void checkAndSetF(int f) {
 if (f >= 0) {
 this.f = f;
 } else {
 throw new IllegalArgumentException();
 }
}
这个新 setter 方法的字节代码如下：
 ILOAD 1
 IFLT label
 ALOAD 0
 ILOAD 1
 PUTFIELD pkg/Bean f I
 GOTO end
label:
 NEW java/lang/IllegalArgumentException
 DUP
 INVOKESPECIAL java/lang/IllegalArgumentException <init> ()V
 ATHROW
end:
 RETURN
 */
fun main() {
    val mn = MethodNode()
    val insnList = mn.instructions
    insnList.add(VarInsnNode(Opcodes.ILOAD, 1))
    val label = LabelNode()
    insnList.add(JumpInsnNode(Opcodes.IFLE, label))
    insnList.add(VarInsnNode(Opcodes.ALOAD, 0))
    insnList.add(VarInsnNode(Opcodes.ILOAD, 1))
    insnList.add(FieldInsnNode(Opcodes.PUTFIELD, "pkg/Bean", "f", "I"))
    val endLabel = LabelNode()
    insnList.add(JumpInsnNode(Opcodes.GOTO, endLabel))
    insnList.add(endLabel)
    insnList.add(FrameNode(Opcodes.F_SAME, 0, null, 0, null))
    insnList.add(TypeInsnNode(Opcodes.NEW,  "java/lang/IllegalArgumentException"))
    insnList.add(InsnNode(Opcodes.DUP))
    insnList.add(MethodInsnNode(Opcodes.INVOKESPECIAL, "java/lang/IllegalArgumentException",  "<init>","()V"))
    insnList.add(InsnNode(Opcodes.ATHROW))
    insnList.add(endLabel)
    insnList.add(FrameNode(Opcodes.F_SAME, 0 ,null, 0, null))
    insnList.add(InsnNode(Opcodes.RETURN))
    mn.maxLocals = 2
    mn.maxStack = 2
}