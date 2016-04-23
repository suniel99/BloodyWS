/*
 * Created on 21/11/2006
 */
package gipsy.GIPC.intensional.SIPL.JOOIP.ast.body;

import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.AnnotationExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.Expression;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.type.Type;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.visitor.GenericVisitor;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.visitor.VoidVisitor;

import java.util.List;

/**
 * @author Julio Vilmar Gesser
 */
public final class AnnotationMemberDeclaration extends BodyDeclaration {

    public final int modifiers;

    public final List<AnnotationExpr> annotations;

    public final Type type;

    public final String name;

    public final Expression defaultValue;

    public AnnotationMemberDeclaration(int line, int column, int modifiers, List<AnnotationExpr> annotations, Type type, String name, Expression defaultValue) {
        super(line, column);
        this.modifiers = modifiers;
        this.annotations = annotations;
        this.type = type;
        this.name = name;
        this.defaultValue = defaultValue;
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
