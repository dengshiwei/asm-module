package com.andoter.asm_example.part7

import com.andoter.asm_example.part3.MyLoader
import com.andoter.asm_example.part6.ClassTransformer
import com.andoter.asm_example.utils.ADLog
import com.andoter.asm_example.utils.ClassOutputUtil
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.*
import java.lang.reflect.Field

class AddTimerMethodTree {
    fun testTimer() {
        Thread.sleep(1000)
    }
}

fun main() {
    val classNode = ClassNode()
    val classReader = ClassReader("com.andoter.asm_example.part7.AddTimerMethodTree")
    classReader.accept(classNode, 0)
    val addTimerTransformer = AddTimerTransformer()
    addTimerTransformer.transform(classNode)

    val classWriter = ClassWriter(ClassWriter.COMPUTE_MAXS)
    classNode.accept(classWriter)
    //输出文件查看
    ClassOutputUtil.byte2File("asm_example/files/AddTimerMethodTree.class", classWriter.toByteArray())

    // 通过自定义 Loader 加载修改后的 .class 文件
    val loader = MyLoader()
    val addTimerAdapterClazz = loader.defineClass("com.andoter.asm_example.part7.AddTimerMethodTree", classWriter.toByteArray())
    // 获取 addTimer 方法
    val method = addTimerAdapterClazz?.getDeclaredMethod("testTimer")
    // 获取 time 字段
    val field : Field? = addTimerAdapterClazz?.getDeclaredField("timer")
    // 触发方法
    method?.invoke(addTimerAdapterClazz.newInstance())
    // 打印 time 耗时
    ADLog.error("timer = ${field?.getLong(addTimerAdapterClazz.newInstance())}")
}


class AddTimerTransformer : ClassTransformer() {

    override fun transform(cn: ClassNode?) {
        if (cn?.methods != null) {
            for (methodNode in cn.methods) {
                ADLog.info("name = ${methodNode.name}")
                if ("<init>" == methodNode.name || "<client>" == methodNode.name) {
                    continue
                }
                val insns = methodNode.instructions
                if (insns.size() == 0) {
                    continue
                }

                val iterator = insns.iterator()
                while (iterator.hasNext()) {
                    val inNode = iterator.next()
                    val opCode = inNode.opcode
                    if (opCode >= Opcodes.IRETURN && opCode <= Opcodes.RETURN || opCode == Opcodes.ATHROW) {
                        val insnList = InsnList()
                        insnList.add(FieldInsnNode(Opcodes.GETSTATIC, cn.name, "timer", "J"))
                        insnList.add(
                            MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                "java/lang/System",
                                "currentTimeMillis",
                                "()J",
                                false
                            )
                        )
                        insnList.add(InsnNode(Opcodes.LADD))
                        insnList.add(FieldInsnNode(Opcodes.PUTSTATIC, cn.name, "timer", "J"))
                        insns.insert(inNode.previous, insnList) // 在当前的指令前面插入代码
                    }
                }

                val insnListAfter = InsnList()
                insnListAfter.add(FieldInsnNode(Opcodes.GETSTATIC, cn.name, "timer", "J"))
                insnListAfter.add(
                    MethodInsnNode(
                        Opcodes.INVOKESTATIC,
                        "java/lang/System",
                        "currentTimeMillis",
                        "()J",
                        false
                    )
                )
                insnListAfter.add(InsnNode(Opcodes.LSUB))
                insnListAfter.add(FieldInsnNode(Opcodes.PUTSTATIC, cn.name, "timer", "J"))
                insns.insert(insnListAfter)
                methodNode.maxStack += 4
            }
            val acc = Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC
            cn.fields.add(FieldNode(acc, "timer", "J", null, null))
        }
    }
}