/*
 * Created on 05/10/2006
 */
package gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr;

import gipsy.GIPC.intensional.SIPL.JOOIP.ast.type.Type;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.visitor.GenericVisitor;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.visitor.VoidVisitor;

import java.util.List;

/**
 * @author Julio Vilmar Gesser
 */
public final class ArrayCreationExpr extends Expression {

    public final Type type;

    public final List<Type> typeArgs;

    public final int arrayCount;

    public final ArrayInitializerExpr initializer;

    public final List<Expression> dimensions;

    public ArrayCreationExpr(int line, int column, Type type, List<Type> typeArgs, int arrayCount, ArrayInitializerExpr initializer) {
        super(line, column);
        this.type = type;
        this.typeArgs = typeArgs;
        this.arrayCount = arrayCount;
        this.initializer = initializer;
        this.dimensions = null;
    }

    public ArrayCreationExpr(int line, int column, Type type, List<Type> typeArgs, List<Expression> dimensions, int arrayCount) {
        super(line, column);
        this.type = type;
        this.typeArgs = typeArgs;
        this.arrayCount = arrayCount;
        this.dimensions = dimensions;
        this.initializer = null;
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
