/*
 * Created on 05/10/2006
 */
package gipsy.GIPC.intensional.SIPL.JOOIP.ast;

import gipsy.GIPC.intensional.SIPL.JOOIP.ast.type.ClassOrInterfaceType;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.visitor.GenericVisitor;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.visitor.VoidVisitor;

import java.util.List;

/**
 * @author Julio Vilmar Gesser
 */
public final class TypeParameter extends Node {

    public final String name;

    public final List<ClassOrInterfaceType> typeBound;

    public TypeParameter(int line, int column, String name, List<ClassOrInterfaceType> typeBound) {
        super(line, column);
        this.name = name;
        this.typeBound = typeBound;
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
