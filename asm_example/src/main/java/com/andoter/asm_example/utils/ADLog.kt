package com.andoter.asm_example.utils

object ADLog {
    private const val TAG = "Andoter"
    var debug = false

    /**
     * 输出错误日志
     */
    fun error(errorMessage: String) {
        try {
            println("${LogUI.ERROR.value}[$TAG]:$errorMessage${LogUI.END.value}")
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    /**
     * 输出信息日志
     */
    fun info(info: String) {
        try {
            println("${LogUI.INFO.value}[$TAG]:$info${LogUI.END.value}")
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    /**
     * 具体规则
     * 字背景颜色范围: 40--49                   字颜色: 30--39
     *             40: 黑                         30: 黑
     *             41:红                          31: 红
     *             42:绿                          32: 绿
     *             43:黄                          33: 黄
     *             44:蓝                          34: 蓝
     *             45:紫                          35: 紫
     *             46:深绿                        36: 深绿
     *             47:白色                        37: 白色
     *
     * 输出特效格式控制：[%d;%d;4m   分别为：前景色值、背景色值、加粗（1）斜体（3）下划线（4）
     *             033[0m  关闭所有属性
     *             033[1m   设置高亮度
     *             03[4m   下划线
     *             033[5m   闪烁
     *             033[7m   反显
     *             033[8m   消隐
     *             033[30m   --   \033[37m   设置前景色
     *             033[40m   --   \033[47m   设置背景色
     */
    enum class LogUI(val value: String) {
        //color
        ERROR("\u001B[30;31m"),
        WARN("\u001B[30;33m"),
        INFO("\u001B[30;32m"),
        END("\u001B[0m")
    }
}