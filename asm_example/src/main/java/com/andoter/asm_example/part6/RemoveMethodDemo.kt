package com.andoter.asm_example.part6

import com.andoter.asm_example.utils.ADLog
import com.andoter.asm_example.utils.ClassOutputUtil
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.tree.ClassNode

class RemoveMethodDemo {
    fun test() {

    }
}

fun main() {
    val removeMethod = RemoveMethodTransformer("test", "()V")
    val classReader = ClassReader("com.andoter.asm_example.part6.RemoveMethodDemo")
    val classNode = ClassNode()
    classReader.accept(classNode, ClassReader.SKIP_DEBUG)
    removeMethod.transform(classNode)

    // 尝试输出进行查看
    val classWriter = ClassWriter(ClassWriter.COMPUTE_MAXS)
    classNode.accept(classWriter)
    //输出文件查看
    ClassOutputUtil.byte2File("asm_example/files/RemoveMethodDemo.class", classWriter.toByteArray())
}

open class ClassTransformer() {
    protected var ct: ClassTransformer? = null
    open fun transform(cn: ClassNode?) {
        if (ct != null) {
            ct!!.transform(cn)
        }
    }
}

class RemoveMethodTransformer(
    var methodName: String,
    var methodDesc: String
) : ClassTransformer() {

    override fun transform(cn: ClassNode?) {
        val methodNodes = cn?.methods
        if (methodNodes != null) {
            for (methodNode in methodNodes) {
                ADLog.info("node = " + methodNode.name + ", desc = " + methodNode.desc)
                if (methodNode.name == methodName && methodNode.desc == methodDesc) {
                    methodNodes.remove(methodNode)
                }
            }
        }
    }
}