package com.andoter.asm_example.part2

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes

class RemoveDebugDemo(var debug:Boolean) {

    fun setDebugValue(debug: Boolean) {
         this.debug = debug
    }

    fun getDebugMode():Boolean {
        return this.debug
    }
}

fun main() {
    val classReader = ClassReader("com.andoter.asm_example.part2.RemoveDebugDemo")
    val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
    val classVisitor = RemoveDebugAdapter(Opcodes.ASM7, classWriter)
    classReader.accept(classVisitor, ClassReader.SKIP_CODE)

    println("==== 删除结果 ======")
    val printClassVisitor = ClassPrintVisitor(Opcodes.ASM7)
    val printReader = ClassReader(classWriter.toByteArray())
    printReader.accept(printClassVisitor, ClassReader.SKIP_CODE)
}