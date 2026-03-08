package mydynlang;

import com.mydynlang.MyDynLangLexer;
import com.mydynlang.MyDynLangParser;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.nio.file.Path;

public class App {
    public static void main(String[] args) throws Exception {
        // The expression to evaluate
        String source = args[0];
        System.out.println("Received source: " + source);

        // 1. Lex the input
        CharStream input = CharStreams.fromString(source);
        MyDynLangLexer lexer = new MyDynLangLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // 2. Parse the tokens
        MyDynLangParser parser = new MyDynLangParser(tokens);
        MyDynLangParser.ProgramContext tree = parser.program();

        // 3. Generate class file by walking the parse tree
        MyDynLangVisitorImpl visitor = new MyDynLangVisitorImpl();
        visitor.visit(tree);
        
        // 4. Dump the class file for debugging and execution
        visitor.dumpBytecodeFile(Path.of("MyLangProgram.class"));
    }
}