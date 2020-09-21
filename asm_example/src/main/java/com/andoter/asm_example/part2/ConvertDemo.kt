package com.andoter.asm_example.part2


import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import kotlin.math.max

class ConvertDemo(private val number: Int, private val age: Int) {

    fun addNumber(scale:Int) {
        val max = max(number, age)
        val result = max * scale
    }
}

fun main() {
    // 第一种方式中，如果在 ConvertVisitor 中仅仅在 visit 方法中使用 classwrite 修改了 version 版本，此时处理后的信息日志输出中只有 visitor。需要
    // 在 visitMethod 中也调用 classWrite 进行输出才行,而不是 调用 super
    val classWriter = ClassWriter(ClassWriter.COMPUTE_MAXS)
    val classReader = ClassReader("com.andoter.asm_example.part2.ConvertDemo")
    val classVisitor = ChangeVersionVisitor(Opcodes.ASM7, classWriter)
    classReader.accept(classVisitor, ClassReader.SKIP_CODE)

    println("===== 处理后的信息  ======")
    val printVisitor = ClassPrintVisitor(Opcodes.ASM7)
    val printReader = ClassReader(classWriter.toByteArray())
    printReader.accept(printVisitor, ClassReader.SKIP_CODE)

    println("==== 2====")
    // 第二种方式，将一个 ClassReader 对象传递给 ClassWrite，会极大提高效率，详情看日志的 visitEnd 时间对比
    val classReader2 = ClassReader("com.andoter.asm_example.part2.ConvertDemo")
    val classWriter2 = ClassWriter(classReader2, ClassWriter.COMPUTE_MAXS)  //将 classWrite 传入一个 classReader 参数
    val classVisitor2 = ChangeVersionVisitor(Opcodes.ASM7, classWriter2)
    classReader2.accept(classVisitor2, ClassReader.SKIP_CODE)

    println("===== 处理后的信息2  ======")
    val printVisitor2 = ClassPrintVisitor(Opcodes.ASM7)
    val printReader2 = ClassReader(classWriter.toByteArray())
    printReader2.accept(printVisitor2, ClassReader.SKIP_CODE)
}

class MyLoader(p0: ClassLoader?) : ClassLoader(p0) {


    override fun findClass(p0: String?): Class<*> {
        return super.findClass(p0)
    }
}