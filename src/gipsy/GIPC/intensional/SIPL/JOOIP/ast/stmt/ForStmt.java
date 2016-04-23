/*
 * Created on 07/11/2006
 */
package gipsy.GIPC.intensional.SIPL.JOOIP.ast.stmt;

import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.Expression;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.visitor.GenericVisitor;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.visitor.VoidVisitor;

import java.util.List;

/**
 * @author Julio Vilmar Gesser
 */
public final class ForStmt extends Statement {

    public final List<Expression> init;

    public final Expression compare;

    public final List<Expression> update;

    public final Statement body;

    public ForStmt(int line, int column, List<Expression> init, Expression compare, List<Expression> update, Statement body) {
        super(line, column);
        this.compare = compare;
        this.init = init;
        this.update = update;
        this.body = body;
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
