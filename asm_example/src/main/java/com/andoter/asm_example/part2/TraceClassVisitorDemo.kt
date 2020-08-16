package com.andoter.asm_example.part2

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.util.TraceClassVisitor
import java.io.PrintWriter

/**
 * ClassWrite 输出的是字节数组，对于我们确认修改后的字节码是否符合预期用处并不大，
 * 在前面，我们是通过 File 文件流操作，将字节码输出到文件，然后利用 javap 指令来查看是否符合预期。
 * 在 ASM 中提供了 TraceClassVisitor 用来对我们的输出做检查，不必这么麻烦
  */
class TraceClassVisitorDemo {

}

fun main() {
    
}