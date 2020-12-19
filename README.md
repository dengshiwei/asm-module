# asm-module
本工程中主要是针对 ASM 4 教程中的示例代码整合而来，将每个示例代码片段整合成可运行的 `Java` 运行程序，方便初学者学习使用。
- 基础入门介绍文章[AOP 利器 ASM 基础入门](doc/blog/AOP%20利器%20ASM%20基础入门.md)
- ASM 开发指南中文译文：[ASM 6 开发者指南](doc/ASM6%20开发者指南/ASM%206%20开发者指南.md)
- 个人总结的额外案例：[asm-example](https://github.com/dengshiwei/asm-example)

## 章节目录

### [第 2 章](asm_example/src/main/java/com/andoter/asm_example/part2)
#### 2.2 接口与组件
- [2.2.1 ClassReader 读取](asm_example/src/main/java/com/andoter/asm_example/part2/ClassReaderDemo.kt)
- [2.2.2 ClassVisitor 分析类](asm_example/src/main/java/com/andoter/asm_example/part2/ClassPrintVisitor.kt)
- [2.2.3 ClassWriter 生成类](asm_example/src/main/java/com/andoter/asm_example/part2/ClassWriteDemo.kt)
- [2.2.4 转换类](asm_example/src/main/java/com/andoter/asm_example/part2/ConvertDemo.kt)
- [2.2.5 移除类成员](asm_example/src/main/java/com/andoter/asm_example/part2/RemoveDebugDemo.kt)
- [2.2.6 添加类成员](asm_example/src/main/java/com/andoter/asm_example/part2/AddFieldDemo.kt)
- 额外补充
    - [FieldVisitor](asm_example/src/main/java/com/andoter/asm_example/field/FieldVisitorDemo.kt)
    - [CheckFieldInsn](asm_example/src/main/java/com/andoter/asm_example/field/CheckFieldInsn.kt)
    - [TraceFieldInsn](asm_example/src/main/java/com/andoter/asm_example/field/TraceFieldInsn.kt)

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
- [3.2.3 转换方法](asm_example/src/main/java/com/andoter/asm_example/part3/RemoveNopAdapter.kt)
- [3.2.4 无状态转换](asm_example/src/main/java/com/andoter/asm_example/part3/AddTimerAdapter.kt)
- [3.2.5 有状态转换](asm_example/src/main/java/com/andoter/asm_example/part3/RemoveAddZeroAdapter.kt)

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
- [4.1.2 接口与组件 SignatureVisitor](asm_example/src/main/java/com/andoter/asm_example/part4/SignatureGeneric.kt)

#### 4.2 注解
- 4.2.2 接口与组件
    - [AnnotationVisitor 基础](asm_example/src/main/java/com/andoter/asm_example/part4/AnnotationPrinter.kt)
    - [删除注解 RemoveAnnotationAdapter](asm_example/src/main/java/com/andoter/asm_example/part4/AnnotationDemo.kt)
    - [添加注解 AddAnnotationAdapter](asm_example/src/main/java/com/andoter/asm_example/part4/AddAnnotationAdapter.kt)
    
- 4.2.3 工具
    - [创建注解类 DeprecatedDump](asm_example/src/main/java/com/andoter/asm_example/part4/DeprecatedDump.kt)
    - [TraceAnnotationVisitor](asm_example/src/main/java/com/andoter/asm_example/part4/TraceAnnotationVisitorDemo.kt)
    - [CheckAnnotationAdapter](asm_example/src/main/java/com/andoter/asm_example/part4/CheckAnnotationAdapterDemo.kt)
    
### [第 5 章](asm_example/src/main/java/com/andoter/asm_example/part5)
- [向后兼容/向下兼容](asm_example/src/main/java/com/andoter/asm_example/part5/向后兼容)

### [第 6 章](asm_example/src/main/java/com/andoter/asm_example/part6)
- 6.1 接口组件介绍
    - [6.1.1 基本介绍](asm_example/src/main/java/com/andoter/asm_example/part6/TreeAPI.kt)
    - [6.1.2 生成类](asm_example/src/main/java/com/andoter/asm_example/part6/CreateClass.kt)
    - 6.1.3 添加删除类成员
        - [删除类成员](asm_example/src/main/java/com/andoter/asm_example/part6/RemoveMethodDemo.kt)
        - [添加类成员](asm_example/src/main/java/com/andoter/asm_example/part6/AddFieldDemo.kt)
- 6.2 组件合成
    - [6.2.1 基本介绍](asm_example/src/main/java/com/andoter/asm_example/part6/TreeAPI.kt)
    - [6.2.2 模式](asm_example/src/main/java/com/andoter/asm_example/part6/PatternDemo.kt)
    
### [第 7 章](asm_example/src/main/java/com/andoter/asm_example/part7)
- 7.1 接口与组件
    - [7.1.1 介绍](asm_example/src/main/java/com/andoter/asm_example/part7/MethodNodeAPI.kt)
    - [7.1.2 生成方法](asm_example/src/main/java/com/andoter/asm_example/part7/MakeMethod.kt)
    - 7.1.4 无状态转换和有状态转换
        - [AddTimerTransformer](asm_example/src/main/java/com/andoter/asm_example/part7/AddTimerTransformer.kt)
        - [RemoveGetFieldPutFieldTransformer](asm_example/src/main/java/com/andoter/asm_example/part7/RemoveGetFieldPutFieldTransformer.kt)
        - [RemoveGetFieldPutFieldTransformer2](asm_example/src/main/java/com/andoter/asm_example/part7/RemoveGetFieldPutFieldTransformer2.kt)
    - 7.1.5 [全局转换](asm_example/src/main/java/com/andoter/asm_example/part7/OptimizeJumpTransformer.kt)
- 7.2 组件与合成
    - [7.2.2 模式](asm_example/src/main/java/com/andoter/asm_example/part7/MyAdapter.kt)
    
### [第 8 章](asm_example/src/main/java/com/andoter/asm_example/part8)
- [8.1 介绍](asm_example/src/main/java/com/andoter/asm_example/part8/AnalyzerClass.kt)
- 8.2 接口与组件
    - [8.2.1 基本数据流分析](asm_example/src/main/java/com/andoter/asm_example/part8/RemoveDeadCode.kt)
    - [8.2.2 基本数据流校验器](asm_example/src/main/java/com/andoter/asm_example/part8/BasicVerifierAdapter.kt)
    - [8.2.3 简单数据流校验器](asm_example/src/main/java/com/andoter/asm_example/part8/RemoveUnusedCastTransformer.kt)
        - [RemoveUnusedCastAdapter](asm_example/src/main/java/com/andoter/asm_example/part8/RemoveUnusedCastAdapter.kt)
    - [8.2.4 自定义数据流分析](asm_example/src/main/java/com/andoter/asm_example/part8/IsNullInterpreter.kt)
    - [8.2.5 控制流分析](asm_example/src/main/java/com/andoter/asm_example/part8/CyclomaticComplexity.kt)
    
### [第 9 章](asm_example/src/main/java/com/andoter/asm_example/part9)
- [9.1 泛型](asm_example/src/main/java/com/andoter/asm_example/part9/SignatureVisitorAdapter.java)
- [9.2 注解](asm_example/src/main/java/com/andoter/asm_example/part9/AnnotationNodeAdapter.kt)

### [第 10 章](asm_example/src/main/java/com/andoter/asm_example/part10)
