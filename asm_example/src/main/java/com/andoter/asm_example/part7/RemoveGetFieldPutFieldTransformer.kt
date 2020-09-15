package com.andoter.asm_example.part7

import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.*

/*
我们可以看到，基于树 API 的代码数量要多余核心 API 的情况，但是这两种方式之间的主要区别是，使用树 API 时不需要状态机。
当有三个火多个连续的 ALOAD 0 指令时，不在成为问题。
 */
class RemoveGetFieldPutFieldTransformer : MethodTransformer() {
    override fun transform(mn: MethodNode) {
        val insnList = mn.instructions
        val iterator = insnList.iterator()
        while (iterator.hasNext()) {
            val insnNode = iterator.next()
            if (isALOAD(insnNode)) {
                val i2 = getNext(insnNode)
                if (i2 != null && isALOAD(i2)) {
                    val i3 = getNext(insnNode)
                    if (i3 != null && i3.opcode == Opcodes.GETFIELD) {
                        val i4 = getNext(i3)
                        if (i4 != null && i4.opcode == Opcodes.PUTFIELD) {
                            if (sameField(i3, i4)) {
                                while (iterator.next() != i4) {

                                }
                                insnList.remove(insnNode)
                                insnList.remove(i2)
                                insnList.remove(i3)
                                insnList.remove(i4)
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        fun isALOAD(abstractInsnNode: AbstractInsnNode): Boolean {
            return abstractInsnNode.opcode == Opcodes.ALOAD && (abstractInsnNode as VarInsnNode).`var` == 0
        }

        fun sameField(iNode: AbstractInsnNode, jNode: AbstractInsnNode): Boolean {
            return (iNode as FieldInsnNode).name == (jNode as FieldInsnNode).name
        }

        private fun getNext(insnNode: AbstractInsnNode?): AbstractInsnNode? {
            var newNode = insnNode
            do {
                newNode = newNode?.next
                if (newNode != null && newNode is LineNumberNode) {
                    break
                }
            } while (newNode != null)
            return newNode
        }
    }
}

open class MethodTransformer {
    private var methodTransformer: MethodTransformer? = null

    open fun transform(mn: MethodNode) {
        methodTransformer?.transform(mn)
    }
}
