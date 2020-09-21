package com.andoter.asm_example.part2

import com.andoter.asm_example.utils.ADLog
import com.andoter.asm_example.utils.AccessCodeUtils
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor


class ChangeVersionVisitor(p0: Int) : ClassVisitor(p0) {
    private  lateinit var classWriter: ClassWriter
    private var startTime:Long = 0
    constructor(version: Int, classWriter: ClassWriter):this(version) {
        this.classWriter = classWriter
    }

    override fun visitMethod(access: Int, name: String?, descriptor: String?, signature: String?, exceptions: Array<out String>?): MethodVisitor? {
        ADLog.info("[visitMethod],access = ${AccessCodeUtils.accCode2String(access)},"+
                "name = ${name},descriptor=${descriptor},signature=${signature}"
        )
        return classWriter.visitMethod(access, name, descriptor, signature, exceptions)
    }

    override fun visit(version: Int, access: Int, name: String?, signature: String?, superName: String?, interfaces: Array<out String>?) {
        startTime = System.nanoTime()
        ADLog.info("[visit],version = ${version},access=${AccessCodeUtils.accCode2String(access)},name=${name}")
        classWriter.visit(52, access, name, signature, superName, interfaces)
    }

    override fun visitEnd() {
        super.visitEnd()
        ADLog.info("[visitEnd], time = ${System.nanoTime() - startTime}")
    }
}