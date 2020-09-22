package com.andoter.asm_example.part8

import com.andoter.asm_example.utils.ADLog
import org.objectweb.asm.ClassReader
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.MethodNode
import org.objectweb.asm.tree.analysis.*
import org.objectweb.asm.tree.analysis.BasicValue.REFERENCE_VALUE
import java.util.*

/*
检测调用对象是否可能为 null
 */
class IsNullInterpreter : BasicInterpreter(Opcodes.ASM7) {

    companion object {
        val NULL: BasicValue = BasicValue(null)
        val MAYBENULL: BasicValue = BasicValue(null)
    }

    override fun newOperation(insn: AbstractInsnNode?): BasicValue? {
        if (insn!!.opcode == Opcodes.ACONST_NULL) {
            return null
        }
        return super.newOperation(insn)
    }

    override fun merge(value1: BasicValue?, value2: BasicValue?): BasicValue {
        if (isRef(value1) && isRef(value2) && value1 != value2) {
            return MAYBENULL
        }
        return super.merge(value1, value2)
    }

    fun isRef(value: Value?): Boolean {
        return value == REFERENCE_VALUE || value == NULL || value == MAYBENULL
    }
}

/*
findNullDereferences 方法用一个 IsNullInterpreter 分析给定方法节点。然后，
对于每条指令，检测其引用操作数（如果有的话）的可能值集是不是 NULL 集或 NONNULL 集。
若是，则这条指令可能导致一个 null 指针异常，将它添加到此类指令的列表中，该列表由这一
方法返回。
getTarget 方法在帧 f 中返回与 insn 对象操作数相对应的 Value，如果 insn 没有对象
操作数，则返回 null。它的主要任务就是计算这个值相对于操作数栈顶端的偏移量，这一数量
取决于指令类型。
 */
class NullDereferenceAnalyzer {
    fun findNullDereferences(owner: String, mn: MethodNode): List<AbstractInsnNode> {
        ADLog.info("owner = $owner, mn_name = ${mn.name}")
        val result = mutableListOf<AbstractInsnNode>()
        val analyzer = Analyzer<BasicValue>(IsNullInterpreter())
        analyzer.analyze(owner, mn)
        val frames = analyzer.frames
        val insns = mn.instructions.toArray()
        for (i in insns.indices) {
            val insnNode = insns[i]
            if (frames[i] != null) {
                val value = getTarget(insnNode, f = frames[i])
                if (value == null || value == IsNullInterpreter.MAYBENULL) {
                    result.add(insnNode)
                }
            }
        }
        return result
    }

    private fun getTarget(insn:AbstractInsnNode,f:Frame<BasicValue>):BasicValue? {
        when (insn.opcode) {
            Opcodes.GETFIELD,Opcodes.ARRAYLENGTH,Opcodes.MONITORENTER,Opcodes.MONITOREXIT -> return getStackValue(f, 0)
            Opcodes.PUTFIELD -> getStackValue(f, 1)
            Opcodes.INVOKEVIRTUAL,Opcodes.INVOKESPECIAL, Opcodes.INVOKEINTERFACE -> {
                val desc = (insn as MethodInsnNode).desc
                return getStackValue(f, Type.getArgumentTypes(desc).size)
            }
        }
        return null
    }

    private fun getStackValue(f: Frame<BasicValue>, index:Int): BasicValue? {
        val top = f.stackSize - 1
        return if (index <= top) f.getStack(top - index) else null
    }
}
class NullTest {
    fun reset() {
        var o : String? = null
        val i = 20
        while (i < 10) {
            o = "number"
        }
        o.toString()
    }
}

fun main() {
    val classReader = ClassReader("com.andoter.asm_example.part8.NullTest")
    val classNode = ClassNode()
    classReader.accept(classNode, ClassReader.SKIP_DEBUG)
    val nullDereferenceAnalyzer = NullDereferenceAnalyzer()
    for (methodNode in classNode.methods) {
        val result = nullDereferenceAnalyzer.findNullDereferences(classNode.name, methodNode)
        for (abstractNode in result) {
            println("${abstractNode.opcode}")
        }
    }
}