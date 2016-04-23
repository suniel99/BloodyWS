/*
 * Created on 05/10/2006
 */
package gipsy.GIPC.intensional.SIPL.JOOIP.ast.body;

import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.AnnotationExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.type.Type;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.visitor.GenericVisitor;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.visitor.VoidVisitor;

import java.util.List;

/**
 * @author Julio Vilmar Gesser
 */
public final class FieldDeclaration extends BodyDeclaration {

    public final int modifiers;

    public final List<AnnotationExpr> annotations;

    public final Type type;

    public final List<VariableDeclarator> variables;

    public FieldDeclaration(int line, int column, int modifiers, List<AnnotationExpr> annotations, Type type, List<VariableDeclarator> variables) {
        super(line, column);
        this.modifiers = modifiers;
        this.annotations = annotations;
        this.type = type;
        this.variables = variables;
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
