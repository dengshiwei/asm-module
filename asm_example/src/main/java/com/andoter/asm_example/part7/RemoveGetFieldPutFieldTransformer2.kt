package com.andoter.asm_example.part7

import com.andoter.asm_example.utils.ClassOutputUtil
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.*

fun main() {
    val classReader = ClassReader("com.andoter.asm_example.part7.BeanField")
    val classNode = ClassNode()
    classReader.accept(classNode, ClassReader.SKIP_DEBUG)
    val removeTransformer = RemoveGetFieldPutFieldTransformer2()
    for (methodNode in classNode.methods) {
        removeTransformer.transform(methodNode)
    }

    val classWriter = ClassWriter(ClassWriter.COMPUTE_MAXS)
    classNode.accept(classWriter)
    /*
    删除之前的字节码指令：
    public final void setF(int);
    descriptor: (I)V
    flags: ACC_PUBLIC, ACC_FINAL
    Code:
      stack=2, locals=2, args_size=2
         0: aload_0
         1: aload_0
         2: getfield      #27                 // Field f:I
         5: putfield      #27                 // Field f:I
         8: aload_0
         9: iload_1
        10: putfield      #27                 // Field f:I
        13: return

    删除后的字节码指令：
    public final void setF(int);
    descriptor: (I)V
    flags: ACC_PUBLIC, ACC_FINAL
    Code:
      stack=2, locals=2, args_size=2
         0: aload_0
         1: iload_1
         2: putfield      #27                 // Field f:I
         5: return
     */
    ClassOutputUtil.byte2File("asm_example/files/BeanField.class", classWriter.toByteArray())
}

/*
我们可以看到，基于树 API 的代码数量要多余核心 API 的情况，但是这两种方式之间的主要区别是，使用树 API 时不需要状态机。
当有三个火多个连续的 ALOAD 0 指令时，不在成为问题。
 */
class RemoveGetFieldPutFieldTransformer2 : MethodTransformer() {
    override fun transform(mn: MethodNode) {
        val insnList = mn.instructions
        val iterator = insnList.iterator()
        while (iterator.hasNext()) {
            var insnNode = iterator.next()
            if (isALOAD(insnNode)) {
                var i2 = getNext(insnNode)
                if (i2 != null && isALOAD(i2)) {
                    var i3 = getNext(i2)
                    while (i3 != null && isALOAD(i3)) {
                        insnNode = i2;
                        i2 = i3;
                        i3 = getNext(insnNode);
                    }
                    if (i3 != null && i3.opcode == Opcodes.GETFIELD) {
                        val i4 = getNext(i3)
                        if (i4 != null && i4.opcode == Opcodes.PUTFIELD) {
                            if (sameField(i3, i4)) {
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
                if (newNode != null && newNode !is LineNumberNode) {
                    break
                }
            } while (newNode != null)
            return newNode
        }
    }
}
