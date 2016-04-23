/*
 * Created on 05/10/2006
 */
package gipsy.GIPC.intensional.SIPL.JOOIP.ast.visitor;

import gipsy.GIPC.intensional.SIPL.JOOIP.ast.CompilationUnit;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.ImportDeclaration;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.Node;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.PackageDeclaration;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.TypeParameter;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.body.AnnotationDeclaration;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.body.AnnotationMemberDeclaration;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.body.BodyDeclaration;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.body.ClassOrInterfaceDeclaration;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.body.ConstructorDeclaration;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.body.EmptyMemberDeclaration;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.body.EmptyTypeDeclaration;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.body.EnumConstantDeclaration;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.body.EnumDeclaration;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.body.FieldDeclaration;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.body.InitializerDeclaration;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.body.MethodDeclaration;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.body.ModifierSet;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.body.Parameter;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.body.TypeDeclaration;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.body.VariableDeclarator;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.body.VariableDeclaratorId;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.AnnotationExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.ArrayAccessExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.ArrayCreationExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.ArrayInitializerExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.AssignExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.BinaryExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.BooleanLiteralExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.CastExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.CharLiteralExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.ClassExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.ConditionalExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.DoubleLiteralExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.EnclosedExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.Expression;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.FieldAccessExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.InstanceOfExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.IntegerLiteralExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.IntegerLiteralMinValueExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.LongLiteralExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.LongLiteralMinValueExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.MarkerAnnotationExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.MemberValuePair;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.MethodCallExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.NameExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.NormalAnnotationExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.NullLiteralExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.ObjectCreationExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.QualifiedNameExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.SingleMemberAnnotationExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.StringLiteralExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.SuperExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.SuperMemberAccessExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.ThisExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.UnaryExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.expr.VariableDeclarationExpr;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.stmt.AssertStmt;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.stmt.BlockStmt;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.stmt.BreakStmt;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.stmt.CatchClause;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.stmt.ContinueStmt;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.stmt.DoStmt;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.stmt.EmptyStmt;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.stmt.ExplicitConstructorInvocationStmt;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.stmt.ExpressionStmt;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.stmt.ForStmt;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.stmt.ForeachStmt;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.stmt.IfStmt;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.stmt.LabeledStmt;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.stmt.ReturnStmt;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.stmt.Statement;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.stmt.SwitchEntryStmt;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.stmt.SwitchStmt;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.stmt.SynchronizedStmt;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.stmt.ThrowStmt;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.stmt.TryStmt;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.stmt.TypeDeclarationStmt;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.stmt.WhileStmt;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.type.ClassOrInterfaceType;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.type.PrimitiveType;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.type.ReferenceType;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.type.Type;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.type.VoidType;
import gipsy.GIPC.intensional.SIPL.JOOIP.ast.type.WildcardType;

import java.util.Iterator;
import java.util.List;

/**
 * @author Julio Vilmar Gesser
 */

public final class DumpVisitor implements VoidVisitor<Object> {

    private final SourcePrinter printer = new SourcePrinter();

    public String getSource() {
        return printer.getSource();
    }

    private void printModifiers(int modifiers) {
        if (ModifierSet.isPrivate(modifiers)) {
            printer.print("private ");
        }
        if (ModifierSet.isProtected(modifiers)) {
            printer.print("protected ");
        }
        if (ModifierSet.isPublic(modifiers)) {
            printer.print("public ");
        }
        if (ModifierSet.isAbstract(modifiers)) {
            printer.print("abstract ");
        }
        if (ModifierSet.isFinal(modifiers)) {
            printer.print("final ");
        }
        if (ModifierSet.isNative(modifiers)) {
            printer.print("native ");
        }
        if (ModifierSet.isStatic(modifiers)) {
            printer.print("static ");
        }
        if (ModifierSet.isStrictfp(modifiers)) {
            printer.print("strictfp ");
        }
        if (ModifierSet.isSynchronized(modifiers)) {
            printer.print("synchronized ");
        }
        if (ModifierSet.isTransient(modifiers)) {
            printer.print("transient ");
        }
        if (ModifierSet.isVolatile(modifiers)) {
            printer.print("volatile ");
        }
    }

    private void printMembers(List<BodyDeclaration> members, Object arg) {
        for (BodyDeclaration member : members) {
            printer.printLn();
            member.accept(this, arg);
            printer.printLn();
        }
    }

    private void printMemberAnnotations(List<AnnotationExpr> annotations, Object arg) {
        if (annotations != null) {
            for (AnnotationExpr a : annotations) {
                a.accept(this, arg);
                printer.printLn();
            }
        }
    }

    private void printAnnotations(List<AnnotationExpr> annotations, Object arg) {
        if (annotations != null) {
            for (AnnotationExpr a : annotations) {
                a.accept(this, arg);
                printer.print(" ");
            }
        }
    }

    private void printTypeArgs(List<Type> args, Object arg) {
        if (args != null) {
            printer.print("<");
            for (Iterator<Type> i = args.iterator(); i.hasNext();) {
                Type t = i.next();
                t.accept(this, arg);
                if (i.hasNext()) {
                    printer.print(", ");
                }
            }
            printer.print(">");
        }
    }

    private void printTypeParameters(List<TypeParameter> args, Object arg) {
        if (args != null) {
            printer.print("<");
            for (Iterator<TypeParameter> i = args.iterator(); i.hasNext();) {
                TypeParameter t = i.next();
                t.accept(this, arg);
                if (i.hasNext()) {
                    printer.print(", ");
                }
            }
            printer.print(">");
        }
    }

    public void visit(Node n, Object arg) {
        throw new IllegalStateException(n.getClass().getName());
    }

    public void visit(CompilationUnit n, Object arg) {
        if (n.pakage != null) {
            n.pakage.accept(this, arg);
        }
        if (n.imports != null) {
            for (ImportDeclaration i : n.imports) {
                i.accept(this, arg);
            }
            printer.printLn();
        }
        if (n.types != null) {
            for (TypeDeclaration i : n.types) {
                i.accept(this, arg);
                printer.printLn();
            }
        }
    }

    public void visit(PackageDeclaration n, Object arg) {
        printAnnotations(n.annotations, arg);
        printer.print("package ");
        n.name.accept(this, arg);
        printer.printLn(";");
        printer.printLn();
    }

    public void visit(NameExpr n, Object arg) {
        printer.print(n.name);
    }

    public void visit(QualifiedNameExpr n, Object arg) {
        n.qualifier.accept(this, arg);
        printer.print(".");
        printer.print(n.name);
    }

    public void visit(ImportDeclaration n, Object arg) {
        printer.print("import ");
        if (n.isStatic) {
            printer.print("static ");
        }
        n.name.accept(this, arg);
        if (n.isAsterisk) {
            printer.print(".*");
        }
        printer.printLn(";");
    }

    public void visit(ClassOrInterfaceDeclaration n, Object arg) {
        printMemberAnnotations(n.annotations, arg);
        printModifiers(n.modifiers);

        if (n.isInterface) {
            printer.print("interface ");
        } else {
            printer.print("class ");
        }

        printer.print(n.name);

        printTypeParameters(n.typeParameters, arg);

        if (n.extendsList != null) {
            printer.print(" extends ");
            for (Iterator<ClassOrInterfaceType> i = n.extendsList.iterator(); i.hasNext();) {
                ClassOrInterfaceType c = i.next();
                c.accept(this, arg);
                if (i.hasNext()) {
                    printer.print(", ");
                }
            }
        }

        if (n.implementsList != null) {
            printer.print(" implements ");
            for (Iterator<ClassOrInterfaceType> i = n.implementsList.iterator(); i.hasNext();) {
                ClassOrInterfaceType c = i.next();
                c.accept(this, arg);
                if (i.hasNext()) {
                    printer.print(", ");
                }
            }
        }

        printer.printLn(" {");
        printer.indent();
        if (n.members != null) {
            printMembers(n.members, arg);
        }
        printer.unindent();
        printer.print("}");
    }

    public void visit(EmptyTypeDeclaration n, Object arg) {
        printer.print(";");
    }

    public void visit(ClassOrInterfaceType n, Object arg) {
        if (n.scope != null) {
            n.scope.accept(this, arg);
            printer.print(".");
        }
        printer.print(n.name);
        printTypeArgs(n.typeArgs, arg);
    }

    public void visit(TypeParameter n, Object arg) {
        printer.print(n.name);
        if (n.typeBound != null) {
            printer.print(" extends ");
            for (Iterator<ClassOrInterfaceType> i = n.typeBound.iterator(); i.hasNext();) {
                ClassOrInterfaceType c = i.next();
                c.accept(this, arg);
                if (i.hasNext()) {
                    printer.print(" & ");
                }
            }
        }
    }

    public void visit(PrimitiveType n, Object arg) {
        switch (n.type) {
            case Boolean:
                printer.print("boolean");
                break;
            case Byte:
                printer.print("byte");
                break;
            case Char:
                printer.print("char");
                break;
            case Double:
                printer.print("double");
                break;
            case Float:
                printer.print("float");
                break;
            case Int:
                printer.print("int");
                break;
            case Long:
                printer.print("long");
                break;
            case Short:
                printer.print("short");
                break;
        }
    }

    public void visit(ReferenceType n, Object arg) {
        n.type.accept(this, arg);
        for (int i = 0; i < n.arrayCount; i++) {
            printer.print("[]");
        }
    }

    public void visit(WildcardType n, Object arg) {
        printer.print("?");
        if (n.ext != null) {
            printer.print(" extends ");
            n.ext.accept(this, arg);
        }
        if (n.sup != null) {
            printer.print(" super ");
            n.sup.accept(this, arg);
        }
    }

    public void visit(FieldDeclaration n, Object arg) {
        printMemberAnnotations(n.annotations, arg);
        printModifiers(n.modifiers);
        n.type.accept(this, arg);

        printer.print(" ");
        for (Iterator<VariableDeclarator> i = n.variables.iterator(); i.hasNext();) {
            VariableDeclarator var = i.next();
            var.accept(this, arg);
            if (i.hasNext()) {
                printer.print(", ");
            }
        }

        printer.print(";");
    }

    public void visit(VariableDeclarator n, Object arg) {
        n.id.accept(this, arg);
        if (n.init != null) {
            printer.print(" = ");
            n.init.accept(this, arg);
        }
    }

    public void visit(VariableDeclaratorId n, Object arg) {
        printer.print(n.name);
        for (int i = 0; i < n.arrayCount; i++) {
            printer.print("[]");
        }
    }

    public void visit(ArrayInitializerExpr n, Object arg) {
        printer.print("{");
        if (n.values != null) {
            printer.print(" ");
            for (Iterator<Expression> i = n.values.iterator(); i.hasNext();) {
                Expression expr = i.next();
                expr.accept(this, arg);
                if (i.hasNext()) {
                    printer.print(", ");
                }
            }
            printer.print(" ");
        }
        printer.print("}");
    }

    public void visit(VoidType n, Object arg) {
        printer.print("void");
    }

    public void visit(ArrayAccessExpr n, Object arg) {
        n.name.accept(this, arg);
        printer.print("[");
        n.index.accept(this, arg);
        printer.print("]");
    }

    public void visit(ArrayCreationExpr n, Object arg) {
        printer.print("new ");
        n.type.accept(this, arg);
        printTypeArgs(n.typeArgs, arg);

        if (n.dimensions != null) {
            for (Expression dim : n.dimensions) {
                printer.print("[");
                dim.accept(this, arg);
                printer.print("]");
            }
            for (int i = 0; i < n.arrayCount; i++) {
                printer.print("[]");
            }
        } else {
            for (int i = 0; i < n.arrayCount; i++) {
                printer.print("[]");
            }
            printer.print(" ");
            n.initializer.accept(this, arg);
        }
    }

    public void visit(AssignExpr n, Object arg) {
        n.target.accept(this, arg);
        printer.print(" ");
        switch (n.op) {
            case assign:
                printer.print("=");
                break;
            case and:
                printer.print("&=");
                break;
            case or:
                printer.print("|=");
                break;
            case xor:
                printer.print("^=");
                break;
            case plus:
                printer.print("+=");
                break;
            case minus:
                printer.print("-=");
                break;
            case rem:
                printer.print("%=");
                break;
            case slash:
                printer.print("/=");
                break;
            case star:
                printer.print("*=");
                break;
            case lShift:
                printer.print("<<=");
                break;
            case rSignedShift:
                printer.print(">>=");
                break;
            case rUnsignedShift:
                printer.print(">>>=");
                break;
        }
        printer.print(" ");
        n.value.accept(this, arg);
    }

    public void visit(BinaryExpr n, Object arg) {
        n.left.accept(this, arg);
        printer.print(" ");
        switch (n.op) {
            case or:
                printer.print("||");
                break;
            case and:
                printer.print("&&");
                break;
            case binOr:
                printer.print("|");
                break;
            case binAnd:
                printer.print("&");
                break;
            case xor:
                printer.print("^");
                break;
            case equals:
                printer.print("==");
                break;
            case notEquals:
                printer.print("!=");
                break;
            case less:
                printer.print("<");
                break;
            case greater:
                printer.print(">");
                break;
            case lessEquals:
                printer.print("<=");
                break;
            case greaterEquals:
                printer.print(">=");
                break;
            case lShift:
                printer.print("<<");
                break;
            case rSignedShift:
                printer.print(">>");
                break;
            case rUnsignedShift:
                printer.print(">>>");
                break;
            case plus:
                printer.print("+");
                break;
            case minus:
                printer.print("-");
                break;
            case times:
                printer.print("*");
                break;
            case divide:
                printer.print("/");
                break;
            case remainder:
                printer.print("%");
                break;
        }
        printer.print(" ");
        n.right.accept(this, arg);
    }

    public void visit(CastExpr n, Object arg) {
        printer.print("(");
        n.type.accept(this, arg);
        printer.print(") ");
        n.expr.accept(this, arg);
    }

    public void visit(ClassExpr n, Object arg) {
        n.type.accept(this, arg);
        printer.print(".class");
    }

    public void visit(ConditionalExpr n, Object arg) {
        n.condition.accept(this, arg);
        printer.print(" ? ");
        n.thenExpr.accept(this, arg);
        printer.print(" : ");
        n.elseExpr.accept(this, arg);
    }

    public void visit(EnclosedExpr n, Object arg) {
        printer.print("(");
        n.inner.accept(this, arg);
        printer.print(")");
    }

    public void visit(FieldAccessExpr n, Object arg) {
        n.scope.accept(this, arg);
        printer.print(".");
        printer.print(n.field);
    }

    public void visit(InstanceOfExpr n, Object arg) {
        n.expr.accept(this, arg);
        printer.print(" instanceof ");
        n.type.accept(this, arg);
    }

    public void visit(CharLiteralExpr n, Object arg) {
        printer.print("'");
        printer.print(n.value);
        printer.print("'");
    }

    public void visit(DoubleLiteralExpr n, Object arg) {
        printer.print(n.value);
    }

    public void visit(IntegerLiteralExpr n, Object arg) {
        printer.print(n.value);
    }

    public void visit(LongLiteralExpr n, Object arg) {
        printer.print(n.value);
    }

    public void visit(IntegerLiteralMinValueExpr n, Object arg) {
        printer.print(n.value);
    }

    public void visit(LongLiteralMinValueExpr n, Object arg) {
        printer.print(n.value);
    }

    public void visit(StringLiteralExpr n, Object arg) {
        printer.print("\"");
        printer.print(n.value);
        printer.print("\"");
    }

    public void visit(BooleanLiteralExpr n, Object arg) {
        printer.print(n.value.toString());
    }

    public void visit(NullLiteralExpr n, Object arg) {
        printer.print("null");
    }

    public void visit(ThisExpr n, Object arg) {
        if (n.classExpr != null) {
            n.classExpr.accept(this, arg);
            printer.print(".");
        }
        printer.print("this");
    }

    public void visit(SuperExpr n, Object arg) {
        if (n.classExpr != null) {
            n.classExpr.accept(this, arg);
            printer.print(".");
        }
        printer.print("super");
    }

    public void visit(MethodCallExpr n, Object arg) {
        if (n.scope != null) {
            n.scope.accept(this, arg);
            printer.print(".");
        }
        printTypeArgs(n.typeArgs, arg);
        printer.print(n.name);
        printer.print("(");
        if (n.args != null) {
            for (Iterator<Expression> i = n.args.iterator(); i.hasNext();) {
                Expression e = i.next();
                e.accept(this, arg);
                if (i.hasNext()) {
                    printer.print(", ");
                }
            }
        }
        printer.print(")");
    }

    public void visit(ObjectCreationExpr n, Object arg) {
        if (n.scope != null) {
            n.scope.accept(this, arg);
            printer.print(".");
        }

        printer.print("new ");

        printTypeArgs(n.typeArgs, arg);
        n.type.accept(this, arg);

        printer.print("(");
        if (n.args != null) {
            for (Iterator<Expression> i = n.args.iterator(); i.hasNext();) {
                Expression e = i.next();
                e.accept(this, arg);
                if (i.hasNext()) {
                    printer.print(", ");
                }
            }
        }
        printer.print(")");

        if (n.anonymousClassBody != null) {
            printer.printLn(" {");
            printer.indent();
            printMembers(n.anonymousClassBody, arg);
            printer.unindent();
            printer.print("}");
        }
    }

    public void visit(SuperMemberAccessExpr n, Object arg) {
        printer.print("super.");
        printer.print(n.name);
    }

    public void visit(UnaryExpr n, Object arg) {
        switch (n.op) {
            case positive:
                printer.print("+");
                break;
            case negative:
                printer.print("-");
                break;
            case inverse:
                printer.print("~");
                break;
            case not:
                printer.print("!");
                break;
            case preIncrement:
                printer.print("++");
                break;
            case preDecrement:
                printer.print("--");
                break;
        }

        n.expr.accept(this, arg);

        switch (n.op) {
            case posIncrement:
                printer.print("++");
                break;
            case posDecrement:
                printer.print("--");
                break;
        }
    }

    public void visit(ConstructorDeclaration n, Object arg) {
        printMemberAnnotations(n.annotations, arg);
        printModifiers(n.modifiers);

        printTypeParameters(n.typeParameters, arg);
        if (n.typeParameters != null) {
            printer.print(" ");
        }
        printer.print(n.name);

        printer.print("(");
        if (n.parameters != null) {
            for (Iterator<Parameter> i = n.parameters.iterator(); i.hasNext();) {
                Parameter p = i.next();
                p.accept(this, arg);
                if (i.hasNext()) {
                    printer.print(", ");
                }
            }
        }
        printer.print(")");

        if (n.throws_ != null) {
            printer.print(" throws ");
            for (Iterator<NameExpr> i = n.throws_.iterator(); i.hasNext();) {
                NameExpr name = i.next();
                name.accept(this, arg);
                if (i.hasNext()) {
                    printer.print(", ");
                }
            }
        }
        printer.print(" ");
        n.block.accept(this, arg);
    }

    public void visit(MethodDeclaration n, Object arg) {
        printMemberAnnotations(n.annotations, arg);
        printModifiers(n.modifiers);

        printTypeParameters(n.typeParameters, arg);
        if (n.typeParameters != null) {
            printer.print(" ");
        }

        n.type.accept(this, arg);
        printer.print(" ");
        printer.print(n.name);

        printer.print("(");
        if (n.parameters != null) {
            for (Iterator<Parameter> i = n.parameters.iterator(); i.hasNext();) {
                Parameter p = i.next();
                p.accept(this, arg);
                if (i.hasNext()) {
                    printer.print(", ");
                }
            }
        }
        printer.print(")");

        for (int i = 0; i < n.arrayCount; i++) {
            printer.print("[]");
        }

        if (n.throws_ != null) {
            printer.print(" throws ");
            for (Iterator<NameExpr> i = n.throws_.iterator(); i.hasNext();) {
                NameExpr name = i.next();
                name.accept(this, arg);
                if (i.hasNext()) {
                    printer.print(", ");
                }
            }
        }
        if (n.block == null) {
            printer.print(";");
        } else {
            printer.print(" ");
            n.block.accept(this, arg);
        }
    }

    public void visit(Parameter n, Object arg) {
        printAnnotations(n.annotations, arg);
        printModifiers(n.modifiers);

        n.type.accept(this, arg);
        if (n.isVarArgs) {
            printer.print("...");
        }
        printer.print(" ");
        n.id.accept(this, arg);
    }

    public void visit(ExplicitConstructorInvocationStmt n, Object arg) {
        if (n.isThis) {
            printTypeArgs(n.typeArgs, arg);
            printer.print("this");
        } else {
            if (n.expr != null) {
                n.expr.accept(this, arg);
                printer.print(".");
            }
            printTypeArgs(n.typeArgs, arg);
            printer.print("super");
        }
        printer.print("(");
        if (n.args != null) {
            for (Iterator<Expression> i = n.args.iterator(); i.hasNext();) {
                Expression e = i.next();
                e.accept(this, arg);
                if (i.hasNext()) {
                    printer.print(", ");
                }
            }
        }
        printer.print(");");
    }

    public void visit(VariableDeclarationExpr n, Object arg) {
        printAnnotations(n.annotations, arg);
        printModifiers(n.modifiers);

        n.type.accept(this, arg);
        printer.print(" ");

        for (Iterator<VariableDeclarator> i = n.vars.iterator(); i.hasNext();) {
            VariableDeclarator v = i.next();
            v.accept(this, arg);
            if (i.hasNext()) {
                printer.print(", ");
            }
        }
    }

    public void visit(TypeDeclarationStmt n, Object arg) {
        n.typeDecl.accept(this, arg);
    }

    public void visit(AssertStmt n, Object arg) {
        printer.print("assert ");
        n.check.accept(this, arg);
        if (n.msg != null) {
            printer.print(" : ");
            n.msg.accept(this, arg);
        }
        printer.print(";");
    }

    public void visit(BlockStmt n, Object arg) {
        printer.printLn("{");
        if (n.stmts != null) {
            printer.indent();
            for (Statement s : n.stmts) {
                s.accept(this, arg);
                printer.printLn();
            }
            printer.unindent();
        }
        printer.print("}");

    }

    public void visit(LabeledStmt n, Object arg) {
        printer.print(n.label);
        printer.print(": ");
        n.stmt.accept(this, arg);
    }

    public void visit(EmptyStmt n, Object arg) {
        printer.print(";");
    }

    public void visit(ExpressionStmt n, Object arg) {
        n.expr.accept(this, arg);
        printer.print(";");
    }

    public void visit(SwitchStmt n, Object arg) {
        printer.print("switch(");
        n.selector.accept(this, arg);
        printer.printLn(") {");
        if (n.entries != null) {
            printer.indent();
            for (SwitchEntryStmt e : n.entries) {
                e.accept(this, arg);
            }
            printer.unindent();
        }
        printer.print("}");

    }

    public void visit(SwitchEntryStmt n, Object arg) {
        if (n.label != null) {
            printer.print("case ");
            n.label.accept(this, arg);
            printer.print(":");
        } else {
            printer.print("default:");
        }
        printer.printLn();
        printer.indent();
        if (n.stmts != null) {
            for (Statement s : n.stmts) {
                s.accept(this, arg);
                printer.printLn();
            }
        }
        printer.unindent();
    }

    public void visit(BreakStmt n, Object arg) {
        printer.print("break");
        if (n.id != null) {
            printer.print(" ");
            printer.print(n.id);
        }
        printer.print(";");
    }

    public void visit(ReturnStmt n, Object arg) {
        printer.print("return");
        if (n.expr != null) {
            printer.print(" ");
            n.expr.accept(this, arg);
        }
        printer.print(";");
    }

    public void visit(EnumDeclaration n, Object arg) {
        printMemberAnnotations(n.annotations, arg);
        printModifiers(n.modifiers);

        printer.print("enum ");
        printer.print(n.name);

        if (n.implementsList != null) {
            printer.print(" implements ");
            for (Iterator<ClassOrInterfaceType> i = n.implementsList.iterator(); i.hasNext();) {
                ClassOrInterfaceType c = i.next();
                c.accept(this, arg);
                if (i.hasNext()) {
                    printer.print(", ");
                }
            }
        }

        printer.printLn(" {");
        printer.indent();
        if (n.entries != null) {
            printer.printLn();
            for (Iterator<EnumConstantDeclaration> i = n.entries.iterator(); i.hasNext();) {
                EnumConstantDeclaration e = i.next();
                e.accept(this, arg);
                if (i.hasNext()) {
                    printer.print(", ");
                }
            }
        }
        if (n.members != null) {
            printer.printLn(";");
            printMembers(n.members, arg);
        } else {
            printer.printLn();
        }
        printer.unindent();
        printer.print("}");
    }

    public void visit(EnumConstantDeclaration n, Object arg) {
        printMemberAnnotations(n.annotations, arg);
        printer.print(n.name);

        if (n.args != null) {
            printer.print("(");
            for (Iterator<Expression> i = n.args.iterator(); i.hasNext();) {
                Expression e = i.next();
                e.accept(this, arg);
                if (i.hasNext()) {
                    printer.print(", ");
                }
            }
            printer.print(")");
        }

        if (n.classBody != null) {
            printer.printLn(" {");
            printer.indent();
            printMembers(n.classBody, arg);
            printer.unindent();
            printer.printLn("}");
        }
    }

    public void visit(EmptyMemberDeclaration n, Object arg) {
        printer.print(";");
    }

    public void visit(InitializerDeclaration n, Object arg) {
        printer.print("static ");
        n.block.accept(this, arg);
    }

    public void visit(IfStmt n, Object arg) {
        printer.print("if (");
        n.condition.accept(this, arg);
        printer.print(") ");
        n.thenStmt.accept(this, arg);
        if (n.elseStmt != null) {
            printer.print(" else ");
            n.elseStmt.accept(this, arg);
        }
    }

    public void visit(WhileStmt n, Object arg) {
        printer.print("while (");
        n.condition.accept(this, arg);
        printer.print(") ");
        n.body.accept(this, arg);
    }

    public void visit(ContinueStmt n, Object arg) {
        printer.print("continue");
        if (n.id != null) {
            printer.print(" ");
            printer.print(n.id);
        }
        printer.print(";");
    }

    public void visit(DoStmt n, Object arg) {
        printer.print("do ");
        n.body.accept(this, arg);
        printer.print(" while (");
        n.condition.accept(this, arg);
        printer.print(");");
    }

    public void visit(ForeachStmt n, Object arg) {
        printer.print("for (");
        n.var.accept(this, arg);
        printer.print(" : ");
        n.iterable.accept(this, arg);
        printer.print(") ");
        n.body.accept(this, arg);
    }

    public void visit(ForStmt n, Object arg) {
        printer.print("for (");
        if (n.init != null) {
            for (Iterator<Expression> i = n.init.iterator(); i.hasNext();) {
                Expression e = i.next();
                e.accept(this, arg);
                if (i.hasNext()) {
                    printer.print(", ");
                }
            }
        }
        printer.print("; ");
        if (n.compare != null) {
            n.compare.accept(this, arg);
        }
        printer.print("; ");
        if (n.update != null) {
            for (Iterator<Expression> i = n.update.iterator(); i.hasNext();) {
                Expression e = i.next();
                e.accept(this, arg);
                if (i.hasNext()) {
                    printer.print(", ");
                }
            }
        }
        printer.print(") ");
        n.body.accept(this, arg);
    }

    public void visit(ThrowStmt n, Object arg) {
        printer.print("throw ");
        n.expr.accept(this, arg);
        printer.print(";");
    }

    public void visit(SynchronizedStmt n, Object arg) {
        printer.print("synchronized (");
        n.expr.accept(this, arg);
        printer.print(") ");
        n.block.accept(this, arg);
    }

    public void visit(TryStmt n, Object arg) {
        printer.print("try ");
        n.tryBlock.accept(this, arg);
        if (n.catchs != null) {
            for (CatchClause c : n.catchs) {
                c.accept(this, arg);
            }
        }
        if (n.finallyBlock != null) {
            printer.print(" finally ");
            n.finallyBlock.accept(this, arg);
        }
    }

    public void visit(CatchClause n, Object arg) {
        printer.print(" catch (");
        n.except.accept(this, arg);
        printer.print(") ");
        n.catchBlock.accept(this, arg);

    }

    public void visit(AnnotationDeclaration n, Object arg) {
        printMemberAnnotations(n.annotations, arg);
        printModifiers(n.modifiers);

        printer.print("@interface ");
        printer.print(n.name);
        printer.printLn(" {");
        printer.indent();
        if (n.members != null) {
            printMembers(n.members, arg);
        }
        printer.unindent();
        printer.print("}");
    }

    public void visit(AnnotationMemberDeclaration n, Object arg) {
        printMemberAnnotations(n.annotations, arg);
        printModifiers(n.modifiers);

        n.type.accept(this, arg);
        printer.print(" ");
        printer.print(n.name);
        printer.print("()");
        if (n.defaultValue != null) {
            printer.print(" default ");
            n.defaultValue.accept(this, arg);
        }
        printer.print(";");
    }

    public void visit(MarkerAnnotationExpr n, Object arg) {
        printer.print("@");
        n.name.accept(this, arg);
    }

    public void visit(SingleMemberAnnotationExpr n, Object arg) {
        printer.print("@");
        n.name.accept(this, arg);
        printer.print("(");
        n.memberValue.accept(this, arg);
        printer.print(")");
    }

    public void visit(NormalAnnotationExpr n, Object arg) {
        printer.print("@");
        n.name.accept(this, arg);
        printer.print("(");
        for (Iterator<MemberValuePair> i = n.pairs.iterator(); i.hasNext();) {
            MemberValuePair m = i.next();
            m.accept(this, arg);
            if (i.hasNext()) {
                printer.print(", ");
            }
        }
        printer.print(")");
    }

    public void visit(MemberValuePair n, Object arg) {
        printer.print(n.name);
        printer.print(" = ");
        n.value.accept(this, arg);
    }
}
