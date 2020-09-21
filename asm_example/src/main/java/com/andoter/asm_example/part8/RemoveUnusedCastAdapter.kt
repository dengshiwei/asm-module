package com.andoter.asm_example.part8

import com.andoter.asm_example.utils.ADLog
import com.andoter.asm_example.utils.ClassOutputUtil
import org.objectweb.asm.*
import org.objectweb.asm.commons.AnalyzerAdapter

class RemoveUnusedCastAdapter(
    owner: String,
    access: Int,
    name: String?,
    desc: String?,
    methodVisitor: MethodVisitor
) : MethodVisitor(Opcodes.ASM7, methodVisitor) {

    private var analyzerAdapter: AnalyzerAdapter? = null

    init {
        analyzerAdapter = AnalyzerAdapter(owner, access, name, desc, methodVisitor)
    }

    override fun visitTypeInsn(opcode: Int, type: String?) {
        ADLog.info("opcode = $opcode, type = $type")
        if (opcode == Opcodes.CHECKCAST) {
            val to = type?.let { getClass(it) }
            if (analyzerAdapter?.stack != null && analyzerAdapter!!.stack.size > 0) {
                val operand = analyzerAdapter?.stack!![analyzerAdapter!!.stack.size - 1]
                if (operand is String) {
                    val from = getClass(operand as String)
                    if (to!!.isAssignableFrom(from)) {
                        return
                    }
                }
            }
        }
        mv.visitTypeInsn(opcode, type)
    }

    companion object {
        private fun getClass(desc: String): Class<*>? {
            return try {
                Class.forName(desc.replace("/", "."))
            } catch (ex: ClassNotFoundException) {
                null
            }
        }
    }
}

fun main() {
    val classReader = ClassReader("com.andoter.asm_example.part8.Timer")
    val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
    classReader.accept(object : ClassVisitor(Opcodes.ASM7,classWriter){
        private lateinit var owner: String
        override fun visit(
            version: Int,
            access: Int,
            name: String?,
            signature: String?,
            superName: String?,
            interfaces: Array<out String>?
        ) {
            this.owner = name!!
            super.visit(version, access, name, signature, superName, interfaces)
        }

        override fun visitMethod(
            access: Int,
            name: String?,
            descriptor: String?,
            signature: String?,
            exceptions: Array<out String>?
        ): MethodVisitor {
            val methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions)
            if (methodVisitor != null) {
                return RemoveUnusedCastAdapter(owner, access,name, descriptor, methodVisitor)
            }
            return methodVisitor
        }
    }, ClassReader.SKIP_DEBUG)

    ClassOutputUtil.byte2File("asm_example/files/Timer.class", classWriter.toByteArray())
}