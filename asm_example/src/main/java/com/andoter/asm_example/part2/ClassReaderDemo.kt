package com.andoter.asm_example.part2

import org.objectweb.asm.ClassReader
import org.objectweb.asm.Opcodes

/**
 * ClassReader 类用于读取一个类文件的字节码，通过 accept 方法委托给 ClassVisitor 解析
 * 其中有在 accept 方法中有 4 个重要的 parsingOptions：
 * - ClassReader.SKIP_DEBUG：标识跳过调试内容，即 SourceFile(跳过源文件)、SourceDebugExtension(源码调试扩展)、LocalVariableTable(局部变量表)、
 *      LocalVariableTypeTable(局部变量类型表)和 LineNumberTable(行号表属性)，
 *      同时以下方法既不会被解析也不会被访问（ClassVisitor.visitSource，MethodVisitor.visitLocalVariable，MethodVisitor.visitLineNumber）。
 *      使用此标识后，类文件调试信息会被去除，请警记
 * - ClassReader.SKIP_CODE：跳过 Code attributes(代码属性)将不会被转换和访问，比如方法体代码不会进行解析和访问。
 * - ClassReader.SKIP_FRAMES：跳过 StackMap(栈图)和 StackMapTable(栈图表) 属性，即 MethodVisitor.visitFrame 方法不会被转换和访问。
 *      当使用 ClassWriter.COMPUTE_FRAMES 时，该标识会很有用，因为它避免了访问帧内容（这些内容会被忽略和重新计算，无需访问）。
 * - ClassReader.EXPAND_FRAMES：表示扩展栈帧图。默认栈图以它们原始格式（V1_6以下使用扩展格式，其他使用压缩格式）被访问。
 *      如果设置该标识，栈图则始终以扩展格式进行访问（此标识在 ClassReader 和 ClassWriter 中增加了解压/压缩步骤，会大幅度降低性能）
 *
 */
class ClassReaderDemo {
}

fun main() {
    val classReader = ClassReader("java.util.ArrayList")
    val classVisitor = ClassPrintVisitor(Opcodes.ASM7)
    classReader.accept(classVisitor, ClassReader.SKIP_DEBUG)
}