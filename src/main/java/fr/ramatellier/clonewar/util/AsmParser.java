package fr.ramatellier.clonewar.util;


import fr.ramatellier.clonewar.instruction.Instruction;
import fr.ramatellier.clonewar.instruction.InstructionBuilder;
import org.objectweb.asm.*;

import java.io.IOException;
import java.lang.module.ModuleFinder;
import java.nio.file.Path;
import java.util.ArrayList;

public class AsmParser {
    public static ArrayList<Instruction> addInstructionsFromJar(String jarName) throws IOException {
        var builder = new InstructionBuilder(jarName);
        var finder = ModuleFinder.of(Path.of(builder.filename()));
        var moduleReference = finder.findAll().stream().findFirst().orElseThrow();
        try(var reader = moduleReference.open()) {
            for(var filename: (Iterable<String>) reader.list()::iterator) {
                if (!filename.endsWith(".class")) {
                    continue;
                }
                try(var inputStream = reader.open(filename).orElseThrow()) {
                    var classReader = new ClassReader(inputStream);
                    classReader.accept(new ClassVisitor(Opcodes.ASM9) {

                        @Override
                        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                            // System.err.println("class " + modifier(access)); // " ---> NEW INSTRUCTION CLASS " + name
                        }

                        @Override
                        public RecordComponentVisitor visitRecordComponent(String name, String descriptor, String signature) {
                            // System.err.println("  component " + name + " " + ClassDesc.ofDescriptor(descriptor).displayName());
                            return null;
                        }

                        @Override
                        public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
                            // System.err.println("  field " + modifier(access) + " " + ClassDesc.ofDescriptor(descriptor).displayName());
                            return null;
                        }

                        @Override
                        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                            // System.err.println("  method " + modifier(access) + " " + MethodTypeDesc.ofDescriptor(descriptor).displayDescriptor()); // " ---> NEW INSTRUCTION method " + (name.equals("<init>") ? "constructor" : name)

                            return new MethodVisitor(Opcodes.ASM9) {

                                @Override
                                public void visitLineNumber(int line, Label start) {
                                    if(!builder.hasFirstLine()) {
                                        builder.firstLine(line);
                                    }
                                    // System.err.println("line -> " + line);
                                }

                                /**
                                 * @Modified
                                 * Les 3 fonctions private suivantes servent à abstraire des instructions génériques, exemple :
                                 * Tout types de return sont des Return
                                 */
                                private boolean isLoadInstr(int opcode) {
                                    return switch (opcode){
                                        case    Opcodes.ALOAD,
                                                Opcodes.LLOAD,
                                                Opcodes.DLOAD,
                                                Opcodes.FLOAD,
                                                Opcodes.ILOAD -> true;
                                        default -> false;
                                    };
                                }

                                private boolean isALoadInstr(int opcode) {
                                    return switch(opcode) {
                                        case Opcodes.IALOAD,
                                                Opcodes.LALOAD,
                                                Opcodes.FALOAD,
                                                Opcodes.DALOAD,
                                                Opcodes.AALOAD,
                                                Opcodes.BALOAD,
                                                Opcodes.CALOAD,
                                                Opcodes.SALOAD-> true;
                                        default -> false;
                                    };
                                }

                                private boolean isReturnInstr(int opcode) {
                                    return switch(opcode){
                                        case    Opcodes.ARETURN,
                                                Opcodes.FRETURN,
                                                Opcodes.DRETURN,
                                                Opcodes.IRETURN,
                                                Opcodes.LRETURN,
                                                Opcodes.RETURN -> true;
                                        default -> false;
                                    };
                                }

                                private boolean isStoreInstr(int opcode) {
                                    return switch(opcode){
                                        case Opcodes.ASTORE,
                                                Opcodes.DSTORE,
                                                Opcodes.FSTORE,
                                                Opcodes.ISTORE,
                                                Opcodes.LSTORE -> true;
                                        default -> false;
                                    };
                                }

                                private boolean isAStoreInstr(int opcode) {
                                    return switch(opcode) {
                                        case Opcodes.IASTORE,
                                                Opcodes.LASTORE,
                                                Opcodes.FASTORE,
                                                Opcodes.DASTORE,
                                                Opcodes.AASTORE,
                                                Opcodes.BASTORE,
                                                Opcodes.CASTORE,
                                                Opcodes.SASTORE-> true;
                                        default -> false;
                                    };
                                }
                                @Override
                                public void visitInsn(int opcode) {
                                    var code = (isStoreInstr(opcode)) ? "Store" : (isAStoreInstr(opcode)) ? "AStore" : (isLoadInstr(opcode)) ? "Load" : (isALoadInstr(opcode)) ? "ALoad" : (isReturnInstr(opcode)) ? "Return" : String.valueOf(opcode);
                                    // System.err.println("opcode " + code);
                                    builder.append("opcode " + code);
                                }

                                @Override
                                public void visitIntInsn(int opcode, int operand) {
                                    // System.err.println("opcode : " + opcode + " operand : " + operand);
                                    builder.append("opcode : " + opcode + " operand : " + operand);
                                }

                                @Override
                                public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
                                    // System.err.println("opcode field " + opcode);
                                    builder.append("opcode field " + opcode);
                                }

                                @Override
                                public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
                                    var s = switch(opcode) {
                                        // case Opcodes.INVOKEVIRTUAL -> "";
                                        case Opcodes.INVOKESPECIAL -> "constructor";
                                        case Opcodes.INVOKESTATIC -> {
                                            var owners = owner.split("/");
                                            yield owners[owners.length - 1] + "." + name;
                                        }
                                        default -> "";
                                    };
                                    // System.err.println("opcode " + opcode + " " + s);
                                    builder.append("opcode " + opcode + " " + s);
                                }

                                @Override
                                public void visitVarInsn(int opcode, int varIndex) {
                                    // System.err.println("opcode " + opcode + " " + varIndex);
                                    builder.append("opcode " + opcode + " " + varIndex);
                                }

                                @Override
                                public void visitTypeInsn(int opcode, String type) {
                                    // System.err.println("opcode " + opcode);
                                    builder.append("opcode " + opcode);
                                }

                                @Override
                                public void visitEnd() {
                                    // System.err.println("end");
                                    builder.endInstruction();
                                }
                            };
                        }
                    }, 0);
                }
            }
        }

        return builder.instructions();
    }
}