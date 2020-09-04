# asm-module
ASM 4 教程中的示例代码

## 章节目录

### [第 2 章](asm_example/src/main/java/com/andoter/asm_example/part2)
#### 2.2 接口与组件
- [2.2.1 ClassReader 读取](asm_example/src/main/java/com/andoter/asm_example/part2/ClassReaderDemo.kt)
- [2.2.2 ClassVisitor 分析类](asm_example/src/main/java/com/andoter/asm_example/part2/ClassPrintVisitor.kt)
- [2.2.3 ClassWriter 生成类](asm_example/src/main/java/com/andoter/asm_example/part2/ClassWriteDemo.kt)
- [2.2.4 转换类](asm_example/src/main/java/com/andoter/asm_example/part2/ConvertDemo.kt)
- [2.2.5 移除类成员](asm_example/src/main/java/com/andoter/asm_example/part2/RemoveDebugDemo.kt)
- [2.2.6 添加类成员](asm_example/src/main/java/com/andoter/asm_example/part2/AddFieldDemo.kt)

#### 2.3 工具
- [2.3.1 Type](asm_example/src/main/java/com/andoter/asm_example/part2/TypeDemo.kt)
- [2.3.2 TraceClassVisitor](asm_example/src/main/java/com/andoter/asm_example/part2/TraceClassVisitorDemo.kt)
- [2.3.3 CheckClassAdapter](asm_example/src/main/java/com/andoter/asm_example/part2/CheckClassAdapterDemo.kt)

### [第 3 章](MethodVisitor)
#### 3.1 结构
- [3.1.3 字节码指令](asm_example/src/main/java/com/andoter/asm_example/part3/字节码指令)

#### 3.2 接口与组件
- [3.2.1 MethodVisitor](asm_example/src/main/java/com/andoter/asm_example/part3/MethodPrint.kt)
- [3.2.2 生成方法](asm_example/src/main/java/com/andoter/asm_example/part3/GenerateMethod.kt)
- [3.2.2 无状态转换](asm_example/src/main/java/com/andoter/asm_example/part3/AddTimerAdapter.kt)
- [3.2.2 有状态转换](asm_example/src/main/java/com/andoter/asm_example/part3/RemoveAddZeroAdapter.kt)

#### 3.3 工具
- 3.3.1 基本工具
    - [1. Type](asm_example/src/main/java/com/andoter/asm_example/part3/TypeDemo.kt)
    - [2. TraceMethodVisitor](asm_example/src/main/java/com/andoter/asm_example/part3/TraceMethodVisitorDemo.kt)
- 3.3.2 AnalyzerAdapter
    - [AddTimerMethodAdapter2](asm_example/src/main/java/com/andoter/asm_example/part3/AddTimerAdapter2.kt)
    - [AddTimerMethodAdapter3](asm_example/src/main/java/com/andoter/asm_example/part3/AddTimerAdapter3.kt)
- 3.3.3 LocalVariablesSorter
    - [AddTimerMethodAdapter4](asm_example/src/main/java/com/andoter/asm_example/part3/AddTimerAdapter4.kt)
    - [AddTimerMethodAdapter5](asm_example/src/main/java/com/andoter/asm_example/part3/AddTimerAdapter5.kt)
- [3.3.4. AdviceAdapter](asm_example/src/main/java/com/andoter/asm_example/part3/AddTimerAdapter6.kt)

### [第 4 章](asm_example/src/main/java/com/andoter/asm_example/part4)
#### 4.1 泛型
- [4.1.2 接口与组件 SinatureVisitor](asm_example/src/main/java/com/andoter/asm_example/part4/SignatureGeneric.kt)

#### 4.2 注解
- 4.2.2 接口与组件
    - [AnnotationVisitor 基础](asm_example/src/main/java/com/andoter/asm_example/part4/AnnotationPrinter.kt)
    - [删除注解 RemoveAnnotationAdapter](asm_example/src/main/java/com/andoter/asm_example/part4/AnnotationDemo.kt)
