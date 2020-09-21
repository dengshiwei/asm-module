package com.andoter.asm_example.part4

import org.objectweb.asm.Opcodes
import org.objectweb.asm.signature.SignatureReader
import org.objectweb.asm.signature.SignatureVisitor
import org.objectweb.asm.signature.SignatureWriter

/*
 在 ASM 的 API 中提供了 SignatureVisitor 类用于泛型的签名访问。
 访问类型签名的顺序：
 visitBaseType | visitArrayType | visitTypeVariable |
( visitClassType visitTypeArgument*
 ( visitInnerClassType visitTypeArgument* )* visitEnd ) )

 访问方法签名的顺序：
 ( visitFormalTypeParameter visitClassBound? visitInterfaceBound* )*
visitParameterType* visitReturnType visitExceptionType*

访问类签名的顺序：
( visitFormalTypeParameter visitClassBound? visitInterfaceBound* )*
visitSuperClass visitInterface*

和 ClassVisitor 一样，ASM 基于 SignatureVisitor 提供了 SignatureReader 和 SignatureWriter 用于分析和生成签名。
 */
class SignatureGeneric<T> {
    fun addNumber(number: T): T {
        println(number)
        return number
    }
}

fun main() {
    val s = "Ljava/util/HashMap<TK;TV;>.HashIterator<TK;>;"
    val renaming = mutableMapOf<String, String>()
    renaming["java/util/HashMap"] = "A"
    renaming["java/util/HashMap.HashIterator"] = "B"
    val signatureWrite = SignatureWriter()
    val renameSignatureAdapter = RenameSignatureAdapter(signatureWrite, renaming)
    val signatureReader = SignatureReader(s)
    signatureReader.accept(renameSignatureAdapter)
    println("result = $signatureWrite")
}

class RenameSignatureAdapter(api: Int) : SignatureVisitor(api) {
    private lateinit var signatureVisitor:SignatureVisitor
    private var renaming: Map<String, String>?= null
    private var oldName : String? = null

    constructor(
        signatureVisitor: SignatureVisitor,
        renaming: Map<String, String>
    ) : this(Opcodes.ASM7) {
        this.signatureVisitor = signatureVisitor
        this.renaming = renaming
    }

    override fun visitFormalTypeParameter(name: String?) {
        signatureVisitor.visitFormalTypeParameter(name)
    }

    override fun visitClassBound(): SignatureVisitor {
        signatureVisitor.visitClassBound()
        return this
    }

    override fun visitInterfaceBound(): SignatureVisitor {
        signatureVisitor.visitInterfaceBound()
        return this
    }

    override fun visitClassType(name: String?) {
        oldName = name
        val newName = renaming?.get(oldName)
        signatureVisitor.visitClassType(if (name == null) name else newName)
    }

    override fun visitInnerClassType(name: String?) {
        oldName = "$oldName.$name"
        val temName = oldName
        val newName = renaming?.get(temName)
        signatureVisitor.visitInnerClassType(if (name == null) name else newName)
    }

    override fun visitTypeArgument() {
        signatureVisitor.visitTypeArgument()
    }

    override fun visitTypeArgument(wildcard: Char): SignatureVisitor {
        return signatureVisitor.visitTypeArgument(wildcard)
    }

    override fun visitEnd() {
        signatureVisitor.visitEnd()
    }
}