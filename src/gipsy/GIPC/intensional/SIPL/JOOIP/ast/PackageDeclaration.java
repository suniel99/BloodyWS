/*
 * Created on 17/10/2007
 */
package gipsy.GIPC.intensional.SIPL.JOOIP.ast;

import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.AnnotationExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.NameExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.visitor.GenericVisitor;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.visitor.VoidVisitor;

import java.util.List;

/**
 * @author Julio Vilmar Gesser
 */
public final class PackageDeclaration extends Node {

    public final List<AnnotationExpr> annotations;

    public final NameExpr name;

    public PackageDeclaration(int line, int column, List<AnnotationExpr> annotations, NameExpr name) {
        super(line, column);
        this.annotations = annotations;
        this.name = name;
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
