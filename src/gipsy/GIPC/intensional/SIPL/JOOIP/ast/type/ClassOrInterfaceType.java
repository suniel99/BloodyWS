/*
 * Created on 05/10/2006
 */
package gipsy.GIPC.intensional.SIPL.JOOIP.ast.type;

import gipsy.GIPC.intensional.SIPL.JOOIP.ast.visitor.GenericVisitor;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.visitor.VoidVisitor;

import java.util.List;

/**
 * @author Julio Vilmar Gesser
 */
public final class ClassOrInterfaceType extends Type {

    public final ClassOrInterfaceType scope;

    public final String name;

    public final List<Type> typeArgs;

    public ClassOrInterfaceType(int line, int column, ClassOrInterfaceType scope, String name, List<Type> typeArgs) {
        super(line, column);
        this.scope = scope;
        this.name = name;
        this.typeArgs = typeArgs;
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
