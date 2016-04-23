/*
 * Created on 05/11/2006
 */
package gipsy.GIPC.intensional.SIPL.JOOIP.ast.body;

import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.AnnotationExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.Expression;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.visitor.GenericVisitor;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.visitor.VoidVisitor;

import java.util.List;

/**
 * @author Julio Vilmar Gesser
 */
public final class EnumConstantDeclaration extends BodyDeclaration {

    public final List<AnnotationExpr> annotations;

    public final String name;

    public final List<Expression> args;

    public final List<BodyDeclaration> classBody;

    public EnumConstantDeclaration(int line, int column, List<AnnotationExpr> annotations, String name, List<Expression> args, List<BodyDeclaration> classBody) {
        super(line, column);
        this.annotations = annotations;
        this.name = name;
        this.args = args;
        this.classBody = classBody;
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
