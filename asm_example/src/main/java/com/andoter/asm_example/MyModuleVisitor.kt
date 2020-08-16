package com.andoter.asm_example

import com.andoter.asm_example.utils.SALog
import org.objectweb.asm.ModuleVisitor

class MyModuleVisitor(api: Int) : ModuleVisitor(api) {
    override fun visitEnd() {
        super.visitEnd()
        SALog.info("visitEnd")
    }

    override fun visitExport(packaze: String?, access: Int, vararg modules: String?) {
        super.visitExport(packaze, access, *modules)
        SALog.info("visitExport")
    }

    override fun visitMainClass(mainClass: String?) {
        super.visitMainClass(mainClass)
        SALog.info("visitMainClass")
    }

    override fun visitOpen(packaze: String?, access: Int, vararg modules: String?) {
        super.visitOpen(packaze, access, *modules)
        SALog.info("visitOpen")
    }

    override fun visitPackage(packaze: String?) {
        super.visitPackage(packaze)
        SALog.info("visitPackage")
    }

    override fun visitProvide(service: String?, vararg providers: String?) {
        super.visitProvide(service, *providers)
        SALog.info("visitProvide")
    }

    override fun visitRequire(module: String?, access: Int, version: String?) {
        super.visitRequire(module, access, version)
        SALog.info("visitRequire")
    }

    override fun visitUse(service: String?) {
        super.visitUse(service)
        SALog.info("visitUse")
    }
}