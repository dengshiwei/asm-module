package com.andoter.asm_example.part2

import com.andoter.asm_example.MyModuleVisitor
import com.andoter.asm_example.utils.AccessCodeUtils
import com.andoter.asm_example.utils.SALog
import org.objectweb.asm.*

class ClassPrintVisitor(version:Int) : ClassVisitor(version) {

    private val apiVersion = api
    override fun visitMethod(access: Int, name: String?, descriptor: String?, signature: String?, exceptions: Array<out String>?): MethodVisitor? {
        SALog.info("visitMethod：access=${AccessCodeUtils.accCode2String(access)}," +
                "name=${name}," +
                "descriptor=$descriptor," +
                "signature=$signature")
        return null
    }

    override fun visitModule(name: String?, access: Int, version: String?): ModuleVisitor? {
        SALog.info("visitModule:name=$name,access=${AccessCodeUtils.accCode2String(access)},version=$version")
        return MyModuleVisitor(apiVersion)
    }

    override fun visitNestHost(nestHost: String?) {
        SALog.info("visitNestHost:nestHost=$nestHost")
        super.visitNestHost(nestHost)
    }

    override fun visitInnerClass(name: String?, outerName: String?, innerName: String?, access: Int) {
        SALog.info("visitInnerClass:name=$name,outerName=$outerName,innerName:$innerName,access=${AccessCodeUtils.accCode2String(access)}")
        super.visitInnerClass(name, outerName, innerName, access)
    }

    override fun visitSource(source: String?, debug: String?) {
        SALog.info("visitSource:source=$source,debug=$debug")
        super.visitSource(source, debug)
    }

    override fun visitOuterClass(owner: String?, name: String?, descriptor: String?) {
        SALog.info("visitOuterClass:owner:$owner,name=$name,descriptor=$descriptor")
        super.visitOuterClass(owner, name, descriptor)
    }

    override fun visit(version: Int, access: Int, name: String?, signature: String?, superName: String?, interfaces: Array<out String>?) {
        SALog.info("visit:version=$version, access=${AccessCodeUtils.accCode2String(access)},name=$name,signature=$signature" +
                ",superName=$superName")
        super.visit(version, access, name, signature, superName, interfaces)
    }

    override fun visitNestMember(nestMember: String?) {
        SALog.info("visitNestMember:nestMember=$nestMember")
        super.visitNestMember(nestMember)
    }

    override fun visitField(access: Int, name: String?, descriptor: String?, signature: String?, value: Any?): FieldVisitor? {
        SALog.info("visitField:access=${AccessCodeUtils.accCode2String(access)},name=$name,descriptor=$descriptor," +
                "signature=$signature,value=$value")
        return super.visitField(access, name, descriptor, signature, value)
    }

    override fun visitEnd() {
        SALog.info("visitEnd")
        super.visitEnd()
    }

    override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor? {
        SALog.info("visitAnnotation:descriptor=$descriptor,visible=$visible")
        return super.visitAnnotation(descriptor, visible)
    }

    override fun visitTypeAnnotation(typeRef: Int, typePath: TypePath?, descriptor: String?, visible: Boolean): AnnotationVisitor? {
        SALog.info("visitTypeAnnotation:typeRef=$typeRef,typePath=${typePath.toString()},descriptor=$descriptor,visible=$visible")
        return super.visitTypeAnnotation(typeRef, typePath, descriptor, visible)
    }

    override fun visitAttribute(attribute: Attribute?) {
        SALog.info("visitAttribute")
        super.visitAttribute(attribute)
    }
}