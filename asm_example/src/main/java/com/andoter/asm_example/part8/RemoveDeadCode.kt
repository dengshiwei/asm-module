package com.andoter.asm_example.part8

import com.andoter.asm_example.utils.ADLog
import com.andoter.asm_example.utils.ClassOutputUtil
import org.objectweb.asm.*
import org.objectweb.asm.tree.LabelNode
import org.objectweb.asm.tree.MethodNode
import org.objectweb.asm.tree.analysis.Analyzer
import org.objectweb.asm.tree.analysis.AnalyzerException
import org.objectweb.asm.tree.analysis.BasicInterpreter
import org.objectweb.asm.tree.analysis.BasicValue

/*
在 ASM 的 API 中，org.objectweb.asm.tree.analysis 包中提供了用于分析代码的类，它是基于树 API 的一个进行正向数据流分析的框架。
为了能够以准确度不一的取值进行各种数据流分析，数据流分析算法分为两部分：一种是固
定的，由框架提供，另一种是变化的，由用户提供。更准确地说：
- 整体数据流分析算法、将适当数量的值从栈中弹出和压回栈中的任务仅实现一次，用于 Analyzer 和 Frame 类中的所有内容。
- 合并值的任何和计算值集并集的任务由用户定义的 Interpreter 和 Value 抽象类的子类提供。
尽管框架的主要目的是执行数据流分析，但 Analyzer 类也可构造所分析方法的控制流图。为此，可以重写这个类的newControlFlowEdge和newControlFlowExceptionEdge方法，
它们默认情况下不做任何事情。其结果可用于进行控制流分析。

Interpreter 类是抽象类，它利用在 BasicValue 类中定义的 7 个值集来模拟字节代码指令的效果。
- UNINITIALIZED_VALUE 指“所有可能值”
- INT_VALUE 指“所有 int、short、byte、boolean 或 char 值”
- FLOAT_VALUE 指“所有 float 值”
- LONG_VALUE 指“所有 long 值”
- DOUBLE_VALUE 指“所有 double 值”
- REFERENCE_VALUE 指“所有对象和数组值”
- RETURNADDRESS_VALUE 用于子例程
这个解释器本身作用不大，主要是作为一个空实现，用于构建 Analyzer 对象。
 */
class RemoveDeadCode {
    var number: Int = 0
    fun checkAndSet(value: Int) {
        if (value > 0) {
            this.number = value
            return
        } else {
            return
        }
        val sum = number + 10
        println()
    }
}

/*
在分析之后，无论什么样的 Interpreter 实现，由 Analyzer.getFrames 方法返回的计算帧，对于不可到达的指令都是 null。这一特性可用于
非常轻松地实现一个 RemoveDeadCodeAdapter 类用于删除无用代码。
 */
class RemoveDeadCodeAdapter(
    var owner: String?, var access: Int, var name: String?,
    var desc: String?, var methodVisitor: MethodVisitor
) : MethodVisitor(Opcodes.ASM7, MethodNode(access, name, desc, null, null)) {

    override fun visitEnd() {
        val methodNode = mv as MethodNode
        val analyzer = Analyzer<BasicValue>(BasicInterpreter())
        try {
            analyzer.analyze(owner, methodNode)
            val frames = analyzer.frames
            val insns = methodNode.instructions.toArray()
            for (i in insns.indices) {
                if (frames[i] == null && insns[i] !is LabelNode) {
                    methodNode.instructions.remove(insns[i])
                }
            }
        } catch (ignore: AnalyzerException) {

        }
        methodNode.accept(methodVisitor)
    }
}

fun main() {
    val classReader = ClassReader("com.andoter.asm_example.part8.RemoveDeadCode")
    val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
    classReader.accept(object : ClassVisitor(Opcodes.ASM7, classWriter) {
        private var name: String? = ""
        override fun visit(
            version: Int,
            access: Int,
            name: String?,
            signature: String?,
            superName: String?,
            interfaces: Array<out String>?
        ) {
            this.name = name
            super.visit(version, access, name, signature, superName, interfaces)
        }

        override fun visitMethod(
            access: Int,
            name: String?,
            descriptor: String?,
            signature: String?,
            exceptions: Array<out String>?
        ): MethodVisitor? {
            ADLog.info("visitMethod, name = $name, desc = $descriptor")
            val methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions)
            if (methodVisitor != null) {
                return RemoveDeadCodeAdapter(this.name, access, name, descriptor, methodVisitor)
            }
            return methodVisitor
        }
    }, ClassReader.SKIP_DEBUG)

    ClassOutputUtil.byte2File("asm_example/files/RemoveDeadCode.class", classWriter.toByteArray())
}