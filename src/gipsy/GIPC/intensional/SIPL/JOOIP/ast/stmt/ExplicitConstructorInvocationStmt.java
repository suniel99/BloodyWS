/*
 * Created on 03/11/2006
 */
package gipsy.GIPC.intensional.SIPL.JOOIP.ast.stmt;

import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.Expression;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.type.Type;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.visitor.GenericVisitor;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.visitor.VoidVisitor;

import java.util.List;

/**
 * @author Julio Vilmar Gesser
 */
public final class ExplicitConstructorInvocationStmt extends Statement {

    public final List<Type> typeArgs;

    public final boolean isThis;

    public final Expression expr;

    public final List<Expression> args;

    public ExplicitConstructorInvocationStmt(int line, int column, List<Type> typeArgs, boolean isThis, Expression expr, List<Expression> args) {
        super(line, column);
        this.typeArgs = typeArgs;
        this.isThis = isThis;
        this.expr = expr;
        this.args = args;
    }

    @Override
    public <A> void accept(VoidVisitor<A> v, A arg) {
        v.visit(this, arg);
    }

    @Override
    public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }
}
