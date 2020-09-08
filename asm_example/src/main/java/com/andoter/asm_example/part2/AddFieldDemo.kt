package com.andoter.asm_example.part2

import com.andoter.asm_example.utils.ClassOutputUtil
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes

class AddFieldDemo {
    private var name:String = ""


    fun getName():String{
        return this.name
    }
}

fun main() {
    val classReader = ClassReader("com.andoter.asm_example.part2.AddFieldDemo")
    val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
    val classVisitor = AddFieldAdapter(Opcodes.ASM7, classWriter, "age",Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, "Ljava/lang/String")
    classReader.accept(classVisitor, ClassReader.SKIP_CODE)

    println("===== 处理后的信息  ======")
    val printVisitor = ClassPrintVisitor(Opcodes.ASM7)
    val printReader = ClassReader(classWriter.toByteArray())
    printReader.accept(printVisitor, ClassReader.SKIP_CODE)

    //输出文件查看
    ClassOutputUtil.byte2File("asm_example/files/AddFiled.class", classWriter.toByteArray())
}