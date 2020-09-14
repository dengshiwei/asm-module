package com.andoter.asm_example.part7

/*
 在树 API 中通过 MethodNode 进行方法的生成和转换。
 public class MethodNode ... {
     public int access;
     public String name;
     public String desc;
     public String signature;
     public List<String> exceptions;
     public List<AnnotationNode> visibleAnnotations;
     public List<AnnotationNode> invisibleAnnotations;
     public List<Attribute> attrs;
     public Object annotationDefault;
     public List<AnnotationNode>[] visibleParameterAnnotations;
     public List<AnnotationNode>[] invisibleParameterAnnotations;
     public InsnList instructions;
     public List<TryCatchBlockNode> tryCatchBlocks;
     public List<LocalVariableNode> localVariables;
     public int maxStack;
     public int maxLocals;
}

这其中最重要的是从 instructions 字段后的几个。instructions 是一个指令列表，通过 InsnList 进行管理。
InsnList 是一个由指令组成的双向链表，它们的链接存储在 AbstractInsnNode 对象本身中。AbstractInsnNode 类是表示字节代码指令的类的超类。
它的子类是 Xxx InsnNode 类，对应于 MethodVisitor 接口的 visitXxx Insn 方法，而且其构造方式完全相同。例如，VarInsnNode 类对应于 visitVarInsn 方法。
所以对于一个 AbstractInsnNode 对象来说，具有以下特征：
- 一个 AbstractInsnNode 对象在一个指令列表中最多出现一次
- 一个 AbstractInsnNode 对象不能同时属于多个指令列表
- 一个结果是：如果一个 AbstractInsnNode 属于某个列表，要将它添加到另一列表，必须先将其从原列表中删除
- 将一个列表中的所有元素都添加到另一个列表中，将会清空第一个列表。

标记与帧，还有行号，尽管它们并不是指令，但也都用 AbstractInsnNode 类的子类表示，即 LabelNode、FrameNode 和 LineNumberNode 类。
 */
class MethodNodeAPI {
}