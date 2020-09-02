package com.andoter.asm_example.part3

import com.andoter.asm_example.utils.ADLog
import com.andoter.asm_example.utils.AccessCodeUtils
import org.objectweb.asm.*
import kotlin.math.max
import org.objectweb.asm.ClassVisitor as ClassVisitor1


class MethodDemo {
    var name = "method"
    fun printName(value:String) {
        val change = "change"
        val result = if(change.length > name.length) change else name
        name = "$result + ${max(5,6)}"
        println(name)
    }

    override fun toString(): String {
        return "name"
    }
}


fun main() {
    val classReader = ClassReader("com.andoter.asm_example.part3.MethodDemo")
    val classVisitor = object : ClassVisitor1(Opcodes.ASM7) {
        override fun visitMethod(
            access: Int,
            name: String?,
            descriptor: String?,
            signature: String?,
            exceptions: Array<out String>?
        ): MethodVisitor {
            ADLog.info("开始扫描方法：$name")
            val mv = super.visitMethod(access, name, descriptor, signature, exceptions)
            return MethodPrint(Opcodes.ASM7, mv)
        }
    }

    classReader.accept(classVisitor, ClassReader.SKIP_DEBUG)
}

/**
 * MethodVisit 首先会访问注释和属性信息，然后才是方法的字节码，这些访问顺序在 visitCode 和 visitMaxs 调用之间。所以
 * 这两个方法可以用于检测字节码在访问序列中的开始和结束。ASM 中提供了三个基于 MethodVisitor API 的组件，用于生成和转化方法。
 *
 * - ClassReader 类分析已编译方法的内容，其在 accept 方法的参数中传递了 ClassVisitor，ClassReader 类将针对这一 ClassVisitor 返回的
 *  MethodVisitor 对象调用响应的方法。
 *  - ClassWriter 的 visitMethod 方法返回 MethodVisitor 接口的一个实现，它直接以二进制形式生成已编译方法
 *  - MethodVisitor类将它接收到的所有方法调用委托给另一个MethodVisitor方法。可以将它看作一个事件筛选器。
 *
 *  MethodVisitor 回调方法有：
 *  - visitParameter：访问方法一个参数
 *  - visitAnnotationDefualt：访问注解接口方法的默认值
 *  - visitAnnotaion：访问方法的一个注解
 *  - visitTypeAnnotation：访问方法签名上的一个类型的注解
 *  - visitAnnotableParameterCount：访问注解参数数量，就是访问方法参数有注解参数个数
 *  - visitParameterAnnotation：访问参数的注解，返回一个 AnnotationVisitor 可以访问该注解值
 *  - visitAttribute：访问方法的属性
 *  - visitCode：开始访问方法代码，此处可以添加方法运行前拦截器
 *  - visitFrame：访问方法局部变量的当前状态以及操作栈成员信息
 *  - visitIntInsn：访问数值类型指令,当 int 取值-1~5采用 ICONST 指令，取值 -128~127 采用 BIPUSH 指令，取值 -32768~32767 采用 SIPUSH 指令，取值 -2147483648~2147483647 采用 ldc 指令。
 *  - visitVarInsn：访问本地变量类型指令
 *  - visitTypeInsn：访问类型指令，类型指令会把类的内部名称当成参数 Type
 *  - visitFieldInsn：域操作指令，用来加载或者存储对象的 Field
 *  - visitMethodInsn：访问方法操作指令
 *  - visitDynamicInsn：访问动态类型指令
 *  - visitJumpInsn：访问比较跳转指令
 *  - visitLabelInsn：访问 label，当会在调用该方法后访问该label标记一个指令
 *  - visitLdcInsn：访问 LDC 指令，也就是访问常量池索引
 *  - visitLineNumber：访问行号描述
 *  - visitMaxs：访问操作数栈最大值和本地变量表最大值
 *  - visitLocalVariable：访问本地变量描述
 */
class MethodPrint(api: Int, methodVisitor: MethodVisitor?) : MethodVisitor(api, methodVisitor) {

    override fun visitMultiANewArrayInsn(descriptor: String?, numDimensions: Int) {
        super.visitMultiANewArrayInsn(descriptor, numDimensions)
        ADLog.info("visitMultiANewArrayInsn, descriptor = $descriptor, numDimensions = $numDimensions")
    }

    override fun visitFrame(
        type: Int,
        numLocal: Int,
        local: Array<out Any>?,
        numStack: Int,
        stack: Array<out Any>?
    ) {
        super.visitFrame(type, numLocal, local, numStack, stack)
        ADLog.info("visitFrame, type = $type, numLocal = $numLocal, local.size = $(local.size), numStack = $numStack")
    }

    override fun visitVarInsn(opcode: Int, `var`: Int) {
        super.visitVarInsn(opcode, `var`)
        ADLog.info("visitVarInsn, opcode = ${AccessCodeUtils.opcode2String(opcode)}, var = $`var`")
    }

    override fun visitTryCatchBlock(start: Label?, end: Label?, handler: Label?, type: String?) {
        super.visitTryCatchBlock(start, end, handler, type)
        ADLog.info("visitTryCatchBlock")
    }

    override fun visitLookupSwitchInsn(dflt: Label?, keys: IntArray?, labels: Array<out Label>?) {
        super.visitLookupSwitchInsn(dflt, keys, labels)
        ADLog.info("visitLookupSwitchInsn")
    }

    override fun visitJumpInsn(opcode: Int, label: Label?) {
        super.visitJumpInsn(opcode, label)
        ADLog.info("visitJumpInsn, opcode = ${AccessCodeUtils.opcode2String(opcode)}")
    }

    override fun visitLdcInsn(value: Any?) {
        super.visitLdcInsn(value)
        ADLog.info("visitLdcInsn, value = $value")
    }

    override fun visitAnnotableParameterCount(parameterCount: Int, visible: Boolean) {
        super.visitAnnotableParameterCount(parameterCount, visible)
    }

    override fun visitIntInsn(opcode: Int, operand: Int) {
        super.visitIntInsn(opcode, operand)
        ADLog.info("visitIntInsn, opcode = ${AccessCodeUtils.opcode2String(opcode)}, operand = $operand")
    }

    override fun visitTypeInsn(opcode: Int, type: String?) {
        super.visitTypeInsn(opcode, type)
        ADLog.info("visitTypeInsn, opcode = ${AccessCodeUtils.opcode2String(opcode)}, type = $type")
    }

    override fun visitAnnotationDefault(): AnnotationVisitor? {
        ADLog.info("visitAnnotationDefault")
        return super.visitAnnotationDefault()
    }

    override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor? {
        ADLog.info("visitAnnotation")
        return super.visitAnnotation(descriptor, visible)
    }

    override fun visitTypeAnnotation(
        typeRef: Int,
        typePath: TypePath?,
        descriptor: String?,
        visible: Boolean
    ): AnnotationVisitor? {
        ADLog.info("visitTypeAnnotation")
        return super.visitTypeAnnotation(typeRef, typePath, descriptor, visible)
    }

    override fun visitMaxs(maxStack: Int, maxLocals: Int) {
        super.visitMaxs(maxStack, maxLocals)
        ADLog.info("visitMaxs")
    }

    override fun visitInvokeDynamicInsn(
        name: String?,
        descriptor: String?,
        bootstrapMethodHandle: Handle?,
        vararg bootstrapMethodArguments: Any?
    ) {
        ADLog.info("visitInvokeDynamicInsn, name = $name, descriptor = $descriptor, bootstrapMethodHandle = ${bootstrapMethodHandle?.name}")
        super.visitInvokeDynamicInsn(
            name,
            descriptor,
            bootstrapMethodHandle,
            *bootstrapMethodArguments
        )
    }

    override fun visitLabel(label: Label?) {
        super.visitLabel(label)
        ADLog.info("visitLabel")
    }

    override fun visitTryCatchAnnotation(
        typeRef: Int,
        typePath: TypePath?,
        descriptor: String?,
        visible: Boolean
    ): AnnotationVisitor? {
        return super.visitTryCatchAnnotation(typeRef, typePath, descriptor, visible)
    }

    override fun visitMethodInsn(opcode: Int, owner: String?, name: String?, descriptor: String?) {
        super.visitMethodInsn(opcode, owner, name, descriptor)
        ADLog.info("visitMethodInsn, opcode = ${AccessCodeUtils.opcode2String(opcode)}, owner = $owner, name = $name, descriptor = $descriptor")
    }

    override fun visitMethodInsn(
        opcode: Int,
        owner: String?,
        name: String?,
        descriptor: String?,
        isInterface: Boolean
    ) {
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface)
        ADLog.info("visitMethodInsn, opcode = ${AccessCodeUtils.opcode2String(opcode)}, owner = $owner, name = $name, descriptor = $descriptor")
    }

    override fun visitInsn(opcode: Int) {
        super.visitInsn(opcode)
        ADLog.info("visitInsn, opcode = ${AccessCodeUtils.opcode2String(opcode)}")
    }

    override fun visitInsnAnnotation(
        typeRef: Int,
        typePath: TypePath?,
        descriptor: String?,
        visible: Boolean
    ): AnnotationVisitor? {
        ADLog.info("visitInsnAnnotation")
        return super.visitInsnAnnotation(typeRef, typePath, descriptor, visible)
    }

    override fun visitParameterAnnotation(
        parameter: Int,
        descriptor: String?,
        visible: Boolean
    ): AnnotationVisitor? {
        ADLog.info("visitParameterAnnotation")
        return super.visitParameterAnnotation(parameter, descriptor, visible)
    }

    override fun visitIincInsn(`var`: Int, increment: Int) {
        super.visitIincInsn(`var`, increment)

        ADLog.info("visitIincInsn")
    }

    override fun visitLineNumber(line: Int, start: Label?) {
        super.visitLineNumber(line, start)
        ADLog.info("visitLineNumber")
    }

    override fun visitLocalVariableAnnotation(
        typeRef: Int,
        typePath: TypePath?,
        start: Array<out Label>?,
        end: Array<out Label>?,
        index: IntArray?,
        descriptor: String?,
        visible: Boolean
    ): AnnotationVisitor? {
        ADLog.info("visitLocalVariableAnnotation")
        return super.visitLocalVariableAnnotation(
            typeRef,
            typePath,
            start,
            end,
            index,
            descriptor,
            visible
        )
    }

    override fun visitTableSwitchInsn(min: Int, max: Int, dflt: Label?, vararg labels: Label?) {
        super.visitTableSwitchInsn(min, max, dflt, *labels)
        ADLog.info("visitTableSwitchInsn")
    }

    override fun visitEnd() {
        super.visitEnd()
        ADLog.info("visitEnd")
    }

    override fun visitLocalVariable(
        name: String?,
        descriptor: String?,
        signature: String?,
        start: Label?,
        end: Label?,
        index: Int
    ) {
        super.visitLocalVariable(name, descriptor, signature, start, end, index)
        ADLog.info(
            "visitLocalVariable, name = $name, descriptor = $descriptor, " +
                    "signature = $signature, start = $start + end = $end, index = $index"
        )
    }

    override fun visitParameter(name: String?, access: Int) {
        super.visitParameter(name, access)
        ADLog.info("visitParameter, name = $name, access = ${AccessCodeUtils.accCode2String(access)}")
    }

    override fun visitAttribute(attribute: Attribute?) {
        super.visitAttribute(attribute)
        ADLog.info("visitAttribute")
    }

    override fun visitFieldInsn(opcode: Int, owner: String?, name: String?, descriptor: String?) {
        super.visitFieldInsn(opcode, owner, name, descriptor)
        ADLog.info("visitFieldInsn, opcode = ${AccessCodeUtils.opcode2String(opcode)}, owner = $owner, name = $name, descriptor = $descriptor")
    }

    override fun visitCode() {
        super.visitCode()
        ADLog.info("visitCode")
    }
}