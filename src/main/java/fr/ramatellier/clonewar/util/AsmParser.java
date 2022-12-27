package fr.ramatellier.clonewar.util;


import fr.ramatellier.clonewar.instruction.Instruction;
import fr.ramatellier.clonewar.instruction.InstructionBuilder;
import org.objectweb.asm.*;

import java.io.IOException;
import java.lang.module.ModuleFinder;
import java.nio.file.Path;
import java.util.ArrayList;

public final class AsmParser {
    /**
     * Method that will print the name of all the files present at the path
     * @param path The path that we want to print the name of his files
     * @throws IOException Because open a ModuleReference can throw an IOException
     */
    public static void printStream(Path path) throws IOException {
        var finder = ModuleFinder.of(path);
        var moduleReference = finder.findAll().stream().findFirst().orElseThrow();
        try(var reader = moduleReference.open()) {
            for(var filename: (Iterable<String>) reader.list()::iterator) {
                System.out.println(filename);
            }
        }
    }

    /**
     * Method to find the instructions of a jar by parsing it and by taking the instructions that are inside the method, we also abstract the code
     * @param jarName The name of the jar we want to parse
     * @param bytes The array of bytes that represent the content of the jar
     * @return The list of all the instructions we have found in the .class files of the jar
     * @throws IOException It throws an IOException because consumeReader can throw it
     */
    public static ArrayList<Instruction> getInstructionsFromJar(String jarName, byte[] bytes) throws IOException {
        var builder = new InstructionBuilder(jarName);
        var resourceReader = new ByteResourceReader(bytes);
        resourceReader.consumeReader(r -> {
            try {
                for(var filename: (Iterable<String>) r.list()::iterator) {
                    if (!filename.endsWith(".class")) {
                        continue;
                    }
                    try(var inputStream = r.open(filename).orElseThrow()) {
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
                                        builder.endInstruction(filename);
                                    }
                                };
                            }
                        }, 0);
                    }
                }
            } catch (IOException e) {
                throw new AssertionError();
            }
        });

        return builder.instructions();
    }
}