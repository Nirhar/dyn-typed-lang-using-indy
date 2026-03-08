package mydynlang;

import com.mydynlang.MyDynLangBaseVisitor;
import com.mydynlang.MyDynLangParser;

public class MyDynLangVisitorImpl extends MyDynLangBaseVisitor<Object> {

    @Override
    public Object visitProgram(MyDynLangParser.ProgramContext ctx) {
        return visit(ctx.expression());
    }

    @Override
    public Object visitExpression(MyDynLangParser.ExpressionContext ctx) {
        Object left  = visit(ctx.left);
        Object right = visit(ctx.right);
        return (Long) left + (Long) right;
    }

    @Override
    public Object visitAtom(MyDynLangParser.AtomContext ctx) {
        if (ctx.INTEGER() != null) {
            return Long.parseLong(ctx.INTEGER().getText());
        }
        // Strip surrounding quotes for strings
        String text = ctx.STRING().getText();
        return text.substring(1, text.length() - 1);
    }
}
