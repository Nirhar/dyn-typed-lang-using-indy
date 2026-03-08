package mydynlang;

import static org.objectweb.asm.Opcodes.*;

import com.mydynlang.MyDynLangBaseVisitor;
import com.mydynlang.MyDynLangParser;
import java.io.IOException;
import java.lang.invoke.*;
import java.nio.file.Files;
import java.nio.file.Path;
import org.objectweb.asm.*;

public class MyDynLangVisitorImpl extends MyDynLangBaseVisitor<Void> {

    ClassWriter cw;
    MethodVisitor mv;

    MyDynLangVisitorImpl() {
        super();
        cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        cw.visit(
            V21,
            ACC_PUBLIC,
            "MyLangProgram",
            null,
            "java/lang/Object",
            null
        );
        mv = cw.visitMethod(
            ACC_PUBLIC | ACC_STATIC,
            "main",
            "([Ljava/lang/String;)V",
            null,
            null
        );
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
        // Get System.out onto the stack so that we can print the result
        mv.visitFieldInsn(
            GETSTATIC,
            "java/lang/System",
            "out",
            "Ljava/io/PrintStream;"
        );

        visit(ctx.left);
        visit(ctx.right);
        // Call Indy
        Handle bootstrapHandle = new Handle(
            H_INVOKESTATIC, // Bootstrap is usually a static method
            "mydynlang/MyDynLangAdd",
            "bootstrap",
            "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;",
            false // Not an interface
        );

        mv.visitInvokeDynamicInsn(
            "add", // Operation name (Currently unused as we only have add as the operation)
            "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;", // Callsite description
            bootstrapHandle
        );

        // Call println
        mv.visitMethodInsn(
            INVOKEVIRTUAL,
            "java/io/PrintStream",
            "println",
            "(Ljava/lang/Object;)V",
            false
        );

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
        } catch (IOException ioe) {
            System.out.println("Exception thrown: " + ioe);
        }
    }
}
