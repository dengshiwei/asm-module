package com.andoter.asm_example.part7

import com.andoter.asm_example.utils.ClassOutputUtil
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.JumpInsnNode
import org.objectweb.asm.tree.MethodNode

class Optimize {
    var number: Int = 0
    fun checkAndSet(value: Int) {
        if (value > 0) {
            this.number = value
        } else {
            return
        }
    }
}


fun main() {
    val classReader = ClassReader("com.andoter.asm_example.part7.Optimize")
    val classNode = ClassNode()
    classReader.accept(classNode, ClassReader.SKIP_DEBUG)
    val optimizeJumpTransformer = OptimizeJumpTransformer()
    for (methodNode in classNode.methods) {
        optimizeJumpTransformer.transform(methodNode)
    }

    val classWriter = ClassWriter(ClassWriter.COMPUTE_MAXS)
    classNode.accept(classWriter)
    ClassOutputUtil.byte2File("asm_example/files/Optimize.class", classWriter.toByteArray())
}

class OptimizeJumpTransformer : MethodTransformer() {

    override fun transform(mn: MethodNode) {
        val insns = mn.instructions
        val iterator = insns.iterator()
        while (iterator.hasNext()) {
            val inNode = iterator.next()
            if (inNode is JumpInsnNode) {
                var label = inNode.label
                var target: AbstractInsnNode
                while (true) {
                    target = label
                    while (target != null && target.opcode < 0) {
                        target = target.next
                    }

                    if (target != null && target.opcode == Opcodes.GOTO) {
                        label = (target as JumpInsnNode).label
                    } else {
                        break;
                    }
                    (inNode as JumpInsnNode).label = label
                    if (inNode.opcode == Opcodes.GOTO && target != null) {
                        var op = target.opcode
                        if (op >= Opcodes.IRETURN && op <= Opcodes.RETURN || op == Opcodes.ATHROW) {
                            insns.set(inNode, target.clone(null))
                        }
                    }
                }
            }
        }
        super.transform(mn)
    }
}