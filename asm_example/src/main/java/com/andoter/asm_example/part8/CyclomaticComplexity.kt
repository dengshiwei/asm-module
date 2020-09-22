package com.andoter.asm_example.part8

import org.objectweb.asm.tree.MethodNode
import org.objectweb.asm.tree.analysis.*
import java.util.*


class CyclomaticComplexity {

    fun getCyclomaticComplexity(owner: String, mn: MethodNode) {
        val analyzer = object :Analyzer<BasicValue>(BasicInterpreter()) {
            override fun newFrame(numLocals: Int, numStack: Int): Frame<BasicValue> {
                return Node<BasicValue>(numLocals, numStack)
            }

            override fun newControlFlowEdge(insnIndex: Int, successorIndex: Int) {
                val node = frames[insnIndex] as Node<BasicValue>
                node.successors.plus(frames[successorIndex] as Node<BasicValue>)
                super.newControlFlowEdge(insnIndex, successorIndex)
            }
        }
    }
}

class Node<V : Value>(numLocals: Int, numStack: Int) : Frame<V>(numLocals, numStack) {
    var successors: Set<Node<V>> = mutableSetOf()
}