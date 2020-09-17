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
通过对上面的 getNext() 方法进行调整，现在是对迭代列表进行操作。当序列识别确认完毕后，迭代器会恰好
在他后面的位置。所以不需要 while(i.next() != i4) 的循环遍历判断
 */
class RemoveGetFieldPutFieldTransformer2 : MethodTransformer() {
    override fun transform(mn: MethodNode) {
        val insnList = mn.instructions
        val iterator = insnList.iterator()
        while (iterator.hasNext()) {
            var insnNode = iterator.next()
            if (isALOAD(insnNode)) {
                var i2 = getNext(iterator)
                if (i2 != null && isALOAD(i2)) {
                    var i3 = getNext(iterator)
                    while (i3 != null && isALOAD(i3)) {
                        insnNode = i2;
                        i2 = i3;
                        i3 = getNext(iterator);
                    }
                    if (i3 != null && i3.opcode == Opcodes.GETFIELD) {
                        val i4 = getNext(iterator)
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

        private fun getNext(iterator: Iterator<AbstractInsnNode>): AbstractInsnNode? {
            while (iterator.hasNext()) {
                val abstractInsnNode = iterator.next()
                if(abstractInsnNode !is LineNumberNode) {
                    return abstractInsnNode
                }
            }
            return null
        }
    }
}
