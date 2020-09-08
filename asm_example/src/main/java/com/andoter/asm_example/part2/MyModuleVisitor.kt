package com.andoter.asm_example.part2

import com.andoter.asm_example.utils.ADLog
import org.objectweb.asm.ModuleVisitor

class MyModuleVisitor(api: Int) : ModuleVisitor(api) {
    override fun visitEnd() {
        super.visitEnd()
        ADLog.info("visitEnd")
    }

    override fun visitExport(packaze: String?, access: Int, vararg modules: String?) {
        super.visitExport(packaze, access, *modules)
        ADLog.info("visitExport")
    }

    override fun visitMainClass(mainClass: String?) {
        super.visitMainClass(mainClass)
        ADLog.info("visitMainClass")
    }

    override fun visitOpen(packaze: String?, access: Int, vararg modules: String?) {
        super.visitOpen(packaze, access, *modules)
        ADLog.info("visitOpen")
    }

    override fun visitPackage(packaze: String?) {
        super.visitPackage(packaze)
        ADLog.info("visitPackage")
    }

    override fun visitProvide(service: String?, vararg providers: String?) {
        super.visitProvide(service, *providers)
        ADLog.info("visitProvide")
    }

    override fun visitRequire(module: String?, access: Int, version: String?) {
        super.visitRequire(module, access, version)
        ADLog.info("visitRequire")
    }

    override fun visitUse(service: String?) {
        super.visitUse(service)
        ADLog.info("visitUse")
    }
}