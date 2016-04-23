/*
 * Created on 05/10/2006
 */
package gipsy.GIPC.intensional.SIPL.JOOIP.ast.type;

import gipsy.GIPC.intensional.SIPL.JOOIP.ast.visitor.GenericVisitor;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.visitor.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class ReferenceType extends Type {

    public final Type type;

    public final int arrayCount;

    public ReferenceType(int line, int column, Type type, int arrayCount) {
        super(line, column);
        this.type = type;
        this.arrayCount = arrayCount;
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
