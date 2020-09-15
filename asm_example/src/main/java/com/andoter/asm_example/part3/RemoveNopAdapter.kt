package com.andoter.asm_example.part3

import com.andoter.asm_example.utils.ClassOutputUtil
import org.objectweb.asm.*

/**
 * 在前面的章节中，我们可以对 ClassVisitor 的方法进行拦截不转发，达到删除方法的目的。
 * 同样对于 MethodVisitor 也可以将收到的方法回调进行修改，比如改变参数、删除某次调用的指令、
 * 接收到的调用之间插入代码、增加新的指令等。
 */
class RemoveNopAdapter(api: Int, methodVisitor: MethodVisitor?) :
    MethodVisitor(api, methodVisitor) {

    override fun visitInsn(opcode: Int) {
        if (opcode != Opcodes.NOP) {
            mv.visitInsn(opcode)
        }
    }
}

class RemoveNopAdapterVisitor(api: Int, classVisitor: ClassVisitor?) : ClassVisitor(api, classVisitor) {
    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor? {
        if (name == "trimToSize") {
            return null
        }

        val methodVisitor = cv?.visitMethod(access, name, descriptor, signature, exceptions)
        if (methodVisitor != null) {
            return RemoveNopAdapter(Opcodes.ASM7, methodVisitor)
        }
        return super.visitMethod(access, name, descriptor, signature, exceptions)
    }
}

fun main() {
    val classReader = ClassReader("java.util.ArrayList")
    val classWriter = ClassWriter(ClassWriter.COMPUTE_MAXS)
    val remoteVisitor = RemoveNopAdapterVisitor(Opcodes.ASM7, classWriter)
    classReader.accept(remoteVisitor, ClassReader.SKIP_DEBUG)

    ClassOutputUtil.byte2File("asm_example/files/ArrayList.class", classWriter.toByteArray())
}