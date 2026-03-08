package mydynlang;

import com.mydynlang.MyDynLangLexer;
import com.mydynlang.MyDynLangParser;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class App {
    public static void main(String[] args) throws Exception {
        // The expression to evaluate
        String source = "1 + 3";

        // 1. Lex the input
        CharStream input = CharStreams.fromString(source);
        MyDynLangLexer lexer = new MyDynLangLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // 2. Parse the tokens
        MyDynLangParser parser = new MyDynLangParser(tokens);
        MyDynLangParser.ProgramContext tree = parser.program();

        // 3. Evaluate by walking the parse tree
        MyDynLangVisitorImpl visitor = new MyDynLangVisitorImpl();
        Object result = visitor.visit(tree);

        // 4. Print the result
        System.out.println(result);
    }
}