package com.andoter.asm_example.part6

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode

class PatternDemo {
}

fun main() {
    val classWriter = ClassWriter(0)
    val myClassAdapter = MyClassAdapter(classWriter)
    val classReader = ClassReader("com.andoter.asm_example.part6.PatternDemo")
    classReader.accept(myClassAdapter, ClassReader.SKIP_DEBUG)
    val bytes = classWriter.toByteArray()
}

class MyClassAdapter(var classVisitor: ClassVisitor) : ClassNode(Opcodes.ASM7) {

    override fun visitEnd() {
        // put your transformation code here
        accept(cv)
    }
}