package mydynlang;

import com.mydynlang.MyDynLangBaseVisitor;
import com.mydynlang.MyDynLangParser;

import org.objectweb.asm.*;
import static org.objectweb.asm.Opcodes.*;

import java.nio.file.Path;
import java.io.IOException;
import java.nio.file.Files;

public class MyDynLangVisitorImpl extends MyDynLangBaseVisitor<Void> {
    
    ClassWriter cw;
    MethodVisitor mv;
    
    MyDynLangVisitorImpl() {
        super();
        cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        cw.visit(V21, ACC_PUBLIC, "MyLangProgram", null, "java/lang/Object", null);
        mv = cw.visitMethod(ACC_PUBLIC|ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
    }

    @Override
    public Void visitProgram(MyDynLangParser.ProgramContext ctx) {
        mv.visitCode();
        visit(ctx.expression());
        cw.visitEnd();
        return null;
    }

    @Override
    public Void visitExpression(MyDynLangParser.ExpressionContext ctx) {
        visit(ctx.left);
        visit(ctx.right);
        // TODO: Insert Invokedynamic instruction here
        mv.visitInsn(RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();
        return null;
    }

    @Override
    public Void visitAtom(MyDynLangParser.AtomContext ctx) {
        String text = ctx.STRING().getText();
        // Strip surrounding quotes for strings
        text = text.substring(1, text.length() - 1);
        mv.visitLdcInsn(text);
        return null;
    }
    
    public void dumpBytecodeFile(Path path) {
        byte[] bytecode = cw.toByteArray();
        try {
            Files.write(path, bytecode);
        } catch(IOException ioe) {
            System.out.println("Exception thrown: " + ioe);
        }
    }
}
