package fr.ramatellier.clonewar.asm;

import fr.ramatellier.clonewar.instruction.InstructionBuilder;
import org.objectweb.asm.*;

import java.io.IOException;
import java.lang.constant.ClassDesc;
import java.lang.constant.MethodTypeDesc;
import java.lang.module.ModuleFinder;
import java.lang.reflect.Modifier;
import java.nio.file.Path;
import java.util.Arrays;

public class Asm {
    public static void main(String[] args) throws IOException {
        var builder = new InstructionBuilder();
        var finder = ModuleFinder.of(Path.of("TestJar.jar"));
        var moduleReference = finder.findAll().stream().findFirst().orElseThrow();
        try(var reader = moduleReference.open()) {
            for(var filename: (Iterable<String>) reader.list()::iterator) {
                if (!filename.endsWith(".class")) {
                    continue;
                }
                try(var inputStream = reader.open(filename).orElseThrow()) {
                    var classReader = new ClassReader(inputStream);
                    classReader.accept(new ClassVisitor(Opcodes.ASM9) {

                        private static String modifier(int access) {
                            if (Modifier.isPublic(access)) {
                                return "public";
                            }
                            if (Modifier.isPrivate(access)) {
                                return "private";
                            }
                            if (Modifier.isProtected(access)) {
                                return "protected";
                            }
                            return "";
                        }
                        @Override
                        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                            System.err.println("class " + modifier(access) + " ---> NEW INSTRUCTION CLASS " + name);
                        }

                        @Override
                        public RecordComponentVisitor visitRecordComponent(String name, String descriptor, String signature) {
                            System.err.println("  component " + name + " " + ClassDesc.ofDescriptor(descriptor).displayName());
                            return null;
                        }

                        @Override
                        public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
                            System.err.println("  field " + modifier(access) + " " + ClassDesc.ofDescriptor(descriptor).displayName() + " " + signature);
                            return null;
                        }

                        @Override
                        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                            System.err.println("  method " + modifier(access) + " " + MethodTypeDesc.ofDescriptor(descriptor).displayDescriptor() + " ---> NEW INSTRUCTION method " + (name.equals("<init>") ? "constructor" : name));

                            return new MethodVisitor(Opcodes.ASM9) {

                                private String abstractInstr(int opcode){
                                    return switch(opcode){
                                        case    Opcodes.ARETURN,
                                                Opcodes.FRETURN,
                                                Opcodes.DRETURN,
                                                Opcodes.IRETURN,
                                                Opcodes.LRETURN,
                                                Opcodes.RETURN -> "Return";
                                        default -> String.valueOf(opcode);
                                    };
                                }

                                @Override
                                public void visitLineNumber(int line, Label start) {
                                    System.err.println("line -> " + line);
                                }

                                /**
                                 * @Modified
                                 * Les 3 fonctions private suivantes servent à abstraire des instructions génériques, exemple :
                                 * Tout types de return sont des Return
                                 */
                                private boolean isLoadInstr(int opcode){
                                    return switch (opcode){
                                        case    Opcodes.ALOAD,
                                                Opcodes.LLOAD,
                                                Opcodes.DLOAD,
                                                Opcodes.FLOAD,
                                                Opcodes.ILOAD -> true;
                                        default -> false;
                                    };
                                }
                                private boolean isReturnInstr(int opcode){
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

                                private boolean isStoreInstr(int opcode){
                                    return switch(opcode){
                                        case Opcodes.ASTORE,
                                                Opcodes.DSTORE,
                                                Opcodes.FSTORE,
                                                Opcodes.ISTORE,
                                                Opcodes.LSTORE -> true;
                                        default -> false;
                                    };
                                }
                                @Override
                                public void visitInsn(int opcode) {
                                    var code = (isStoreInstr(opcode)) ? "Store" : (isLoadInstr(opcode)) ? "Load" : (isReturnInstr(opcode)) ? "Return" : String.valueOf(opcode);
                                    System.err.println("    opcode " + code);
                                }

                                @Override
                                public void visitIntInsn(int opcode, int operand) {
                                    System.err.println("    opcode : " + opcode + " operand : " + operand);
                                }

                                @Override
                                public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
                                    System.err.println("    opcode field " + opcode);
                                }

                                @Override
                                public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
                                    var s = switch(opcode) {
                                        case Opcodes.INVOKEVIRTUAL -> "";
                                        case Opcodes.INVOKESPECIAL -> "constructor";
                                        case Opcodes.INVOKESTATIC -> {
                                            var owners = owner.split("/");
                                            yield owners[owners.length - 1] + "." + name;
                                        }
                                        default -> "";
                                    };
                                    System.err.println("    opcode " + opcode + " " + s);
                                }

                                @Override
                                public void visitVarInsn(int opcode, int varIndex) {
                                    System.err.println("    opcode " + opcode + " " + varIndex);
                                }

                                @Override
                                public void visitTypeInsn(int opcode, String type) {
                                    System.err.println("    opcode " + opcode + " type"); //TODO enlever type
                                }

                                @Override
                                public void visitEnd() {
                                    System.err.println("    end");
                                }

                                // + the other visit methods to get all the opcodes
                            };
                        }
                    }, 0);
                }
            }
        }
    }
}