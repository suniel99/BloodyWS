/*
 * Created on 05/10/2006
 */
package gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr;

import gipsy.GIPC.intensional.SIPL.JOOIP.ast.type.Type;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.visitor.GenericVisitor;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.visitor.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class CastExpr extends Expression {

    public final Type type;

    public final Expression expr;

    public CastExpr(int line, int column, Type type, Expression expr) {
        super(line, column);
        this.type = type;
        this.expr = expr;
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
