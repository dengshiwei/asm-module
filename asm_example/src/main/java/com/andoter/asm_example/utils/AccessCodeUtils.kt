package com.andoter.asm_example.utils

import org.objectweb.asm.Opcodes

/**
 * ACC_CODE 工具类
 */
object AccessCodeUtils {
    private const val ACCESS_PREFIX = "ACC_"
    private val mapAccess = mutableMapOf<Int, String>()
    private val mapOpcodes = mutableMapOf<String, Int>()
    /**
     * 修饰符
     */
    fun accCode2String(code: Int): String {
        val accString = StringBuilder()
        if (mapAccess.isEmpty()) {
            accCodeMap()
        }

        mapAccess.forEach { (key, value) ->
            if ((code and key) > 0) {
                accString.append("$value ")
            }
        }
        return accString.toString()
    }

    /**
     * 转换其它操作 Opcode
     */
    fun opcode2String(code: Int):String {
        if (mapOpcodes.isEmpty()) {
            accCodeMap()
        }

        for (op in mapOpcodes) {
            if (op.value == code) {
                return op.key
            }
        }
        return ""
    }

    /**
     * 生成 ACC_ 操作符集合
     */
    private fun accCodeMap() {
        if (mapAccess.isEmpty()) {
            val fields = Opcodes::class.java.fields
            for (field in fields) {
                if (field.name.startsWith(ACCESS_PREFIX)) {
                    mapAccess[field.getInt(null)] = field.name
                } else if (field.type == Int::class.java){
                    mapOpcodes[field.name] = field.getInt(null)
                }
            }
        }
    }
}