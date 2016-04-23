/*
 * Created on 05/10/2006
 */
package gipsy.GIPC.intensional.SIPL.JOOIP.ast.body;

import java.util.List;

/**
 * @author Julio Vilmar Gesser
 */
public abstract class TypeDeclaration extends BodyDeclaration {

    public final String name;

    public final int modifiers;

    public final List<BodyDeclaration> members;

    public TypeDeclaration(int line, int column, String name, int modifiers, List<BodyDeclaration> members) {
        super(line, column);
        this.name = name;
        this.modifiers = modifiers;
        this.members = members;
    }

}
