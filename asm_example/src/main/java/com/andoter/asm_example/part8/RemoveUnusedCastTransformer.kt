package com.andoter.asm_example.part8

import com.andoter.asm_example.part7.MethodTransformer
import com.andoter.asm_example.utils.ClassOutputUtil
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodNode
import org.objectweb.asm.tree.TypeInsnNode
import org.objectweb.asm.tree.analysis.Analyzer
import org.objectweb.asm.tree.analysis.AnalyzerException
import org.objectweb.asm.tree.analysis.BasicValue
import org.objectweb.asm.tree.analysis.SimpleVerifier

/**
 * SimpleVerifier 是 BasicVerifier 的子类，它使用更多的集合来模拟字节代码指令的执行，所以它可以检测出更多的错误。
 * 这个类使用 Java 反射 API，以执行与类层次结构有关的验证和计算。然后，它将一个方法引用的类加载到 JVM 中。这一默认行为可以通过重写这个类的受保护方法来改变。
 * 和 BasicVerifier 一样，这个类也可以在开发类生成器或适配器时使用，以便更轻松地找出 Bug。
 *
 */
class RemoveUnusedCastTransformer(var owner: String) : MethodTransformer() {

    override fun transform(mn: MethodNode) {
        val analyzer = Analyzer<BasicValue>(SimpleVerifier())
        try {
            analyzer.analyze(this.owner, mn)
            val frames = analyzer.frames
            val insns = mn.instructions.toArray()
            for (i in insns.indices) {
                val insnNode = insns[i]
                if (insnNode.opcode == Opcodes.CHECKCAST) {
                    val frame = frames[i]
                    if (frame != null && frame.stackSize>0) {
                        val operand = frame.getStack(frame.stackSize - 1)
                        val to = getClass((insnNode as TypeInsnNode).desc)
                        val from = getClass((operand as BasicValue).type)
                        if (to!!.isAssignableFrom(from)) {
                            mn.instructions.remove(insnNode)
                        }
                    }
                }
            }
        } catch (ex: AnalyzerException) {

        }
        super.transform(mn)
    }

    companion object {
        private fun getClass(desc: String) :Class<*>? {
            return try {
                Class.forName(desc.replace("/", "."))
            } catch (ex : ClassNotFoundException) {
                null
            }
        }

        private fun getClass(t: Type): Class<*>? {
            return if (t.sort == Type.OBJECT) {
                getClass(t.internalName)
            } else getClass(t.descriptor)
        }
    }
}

fun main() {
    val classReader = ClassReader("com.andoter.asm_example.part8.Timer")
    val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
    val classNode = ClassNode()
    classReader.accept(classNode, ClassReader.SKIP_DEBUG)
    val removeUnusedCastTransformer = RemoveUnusedCastTransformer(classNode.name)
    for (methodNode in classNode.methods) {
        removeUnusedCastTransformer.transform(methodNode)
    }

    classNode.accept(classWriter)
    /*
    // 执行前
    public final java.lang.Runnable cast();
      descriptor: ()Ljava/lang/Runnable;
      flags: ACC_PUBLIC, ACC_FINAL
      Code:
        stack=1, locals=1, args_size=1
           0: aload_0
           1: instanceof    #6                  // class java/lang/Runnable
           4: ifeq          12
           7: aload_0
           8: checkcast     #6                  // class java/lang/Runnable
          11: areturn
          12: aconst_null
          13: areturn
        StackMapTable: number_of_entries = 1
          frame_type = 12 /* same */
      RuntimeInvisibleAnnotations:
        0: #27()
    // 执行后
    public final java.lang.Runnable cast();
      descriptor: ()Ljava/lang/Runnable;
      flags: ACC_PUBLIC, ACC_FINAL
      Code:
        stack=1, locals=1, args_size=1
           0: aload_0
           1: instanceof    #6                  // class java/lang/Runnable
           4: ifeq          9
           7: aload_0
           8: areturn
           9: aconst_null
          10: areturn
        StackMapTable: number_of_entries = 1
          frame_type = 9 /* same */
      RuntimeInvisibleAnnotations:
        0: #27()
     */
    ClassOutputUtil.byte2File("asm_example/files/Timer.class", classWriter.toByteArray())
}

class Timer : Runnable {
    override fun run() {
        println("timer")
    }

    fun cast(): Runnable? {
        if (this is Runnable) {
            return this  as Runnable
        }
        return null
    }
}