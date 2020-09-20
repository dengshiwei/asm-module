package com.andoter.asm_example.part8

import com.andoter.asm_example.utils.ClassOutputUtil
import org.objectweb.asm.*
import org.objectweb.asm.tree.MethodNode
import org.objectweb.asm.tree.analysis.Analyzer
import org.objectweb.asm.tree.analysis.AnalyzerException
import org.objectweb.asm.tree.analysis.BasicValue
import org.objectweb.asm.tree.analysis.BasicVerifier

/**
 * BasicVerifier 是 BasicInterpreter 的子类，用于实现对字节码指令是否正确的校验。
 * 例如，它会验证 IADD 指令的操作数为 INTEGER_VALUE 值（而 BasicInterpreter 只是返回结果，即 INTEGER_VALUE）。这个类可在开发类生成器或适配器时进行调试。
 */
class BasicVerifierAdapter : MethodVisitor {
    private var owner:String? = ""
    private var next:MethodVisitor
    constructor(
        owner: String?,
        access: Int,
        name: String?,
        desc: String?,
        methodVisitor: MethodVisitor
    ) : super(Opcodes.ASM7, MethodNode(access, name, desc, null, null)) {
        this.owner = owner
        this.next = methodVisitor
    }

    override fun visitEnd() {
        val methodNode = mv as MethodNode
        val analyzer = Analyzer<BasicValue>(BasicVerifier())
        try {
            analyzer.analyze(owner, methodNode)
        } catch (ignore: AnalyzerException) {
            throw RuntimeException(ignore.message);
        }
        methodNode.accept(next)
    }
}

fun main() {
    val classReader = ClassReader("org.objectweb.asm.tree.MethodNode")
    val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
    classReader.accept(object : ClassVisitor(Opcodes.ASM7, classWriter){
        private var owner: String? = ""
        override fun visit(
            version: Int,
            access: Int,
            name: String?,
            signature: String?,
            superName: String?,
            interfaces: Array<out String>?
        ) {
            this.owner = name
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
                return BasicVerifierAdapter(owner, access, name, descriptor, methodVisitor)
            }
            return methodVisitor
        }
    }, ClassReader.SKIP_DEBUG)

    ClassOutputUtil.byte2File("asm_example/files/MethodNode.class", classWriter.toByteArray())
}