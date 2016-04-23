/*
 * Created on 10/10/2006
 */
package gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr;

import gipsy.GIPC.intensional.SIPL.JOOIP.ast.Node;

/**
 * @author Julio Vilmar Gesser
 */
public abstract class Expression extends Node {

    public Expression(int line, int column) {
        super(line, column);
    }

}
