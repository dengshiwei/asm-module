package com.andoter.asm_example.part2

import com.andoter.asm_example.utils.AccessCodeUtils
import com.andoter.asm_example.utils.SALog
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.FieldVisitor
import org.objectweb.asm.MethodVisitor


class RemoveDebugAdapter(p0: Int) : ClassVisitor(p0) {
    private lateinit var classWriter: ClassWriter
    constructor(version: Int, classWriter: ClassWriter) : this(version) {
        this.classWriter = classWriter
    }


    override fun visitMethod(access: Int, name: String?, descriptor: String?, signature: String?, exceptions: Array<out String>?): MethodVisitor? {
        SALog.info("visitMethod：access=${AccessCodeUtils.accCode2String(access)}," +
                "name=${name}," +
                "descriptor=$descriptor," +
                "signature=$signature")
        if ("getDebugMode()Z" == name + descriptor) {// 移除 getDebugMode 方法
            return null
        }

        return classWriter.visitMethod(access, name, descriptor, signature, exceptions)
    }

    override fun visitSource(source: String?, debug: String?) {
        SALog.info("visitSource: source = ${source}, debug = $debug")
        //classWriter.visitSource(source, debug)   // 通过注释不进行转发，完成对 visitSource 的删除
    }

    override fun visit(version: Int, access: Int, name: String?, signature: String?, superName: String?, interfaces: Array<out String>?) {
        SALog.info("visit: version=$version, access=${AccessCodeUtils.accCode2String(access)},name=$name,signature=$signature" +
                ",superName=$superName")
        classWriter.visit(version, access, name, signature, superName, interfaces)
    }

    override fun visitField(access: Int, name: String?, descriptor: String?, signature: String?, value: Any?): FieldVisitor? {
        SALog.info("visitField: access=${AccessCodeUtils.accCode2String(access)},name=$name,descriptor=$descriptor," +
                "signature=$signature,value=$value")
        return classWriter.visitField(access, name, descriptor, signature, value)
    }

    override fun visitEnd() {
        SALog.info("visitEnd")
        classWriter.visitEnd()
    }
}